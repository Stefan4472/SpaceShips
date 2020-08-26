package com.plainsimple.spaceships.sprite;

import com.plainsimple.spaceships.engine.EventID;
import com.plainsimple.spaceships.engine.GameContext;
import com.plainsimple.spaceships.engine.UpdateContext;
import com.plainsimple.spaceships.helper.BitmapData;
import com.plainsimple.spaceships.helper.BitmapID;
import com.plainsimple.spaceships.helper.DrawImage;
import com.plainsimple.spaceships.helper.DrawParams;
import com.plainsimple.spaceships.helper.HealthBarAnimation;
import com.plainsimple.spaceships.helper.LoseHealthAnimation;
import com.plainsimple.spaceships.helper.Rectangle;
import com.plainsimple.spaceships.helper.SpriteAnimation;
import com.plainsimple.spaceships.util.ProtectedQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * An Alien is an enemy sprite that flies across the screen
 * in a sine wave. Its hp is calculated based on the difficulty,
 * and Aliens become steadily stronger as difficulty increases.
 * An Alien fires AlienBullets at the Spaceship, with this behavior
 * determined by the bulletDelay. These bullets are stored in
 * the projectiles LinkedList, which is accessible and clearable
 * via the getAndClearProjectiles() method.
 * The Alien also has a HealthBarAnimation, which
 * displays when it loses health, and additionally displays
 * LoseHealthAnimations when it loses damage.
 */
public class Alien extends Sprite {

    // frames to wait between firing bullets
    private int bulletDelay;
    // number of frames since last bullet was fired
    private int framesSinceLastBullet = 0;
    // number of bullets left alien can fire
    private int bulletsLeft;

    // frames since alien was constructed
    // used for calculating trajectory
    private int elapsedFrames = 1;

    // starting y-coordinate
    // used as a reference for calculating trajectory
    private float startingY;

    // defines sine wave that describes alien's trajectory
    private int amplitude;
    private int period;
    private int vShift;
    private int hShift;

    private Spaceship spaceship;
    private int difficulty;

    private static final BitmapID BITMAP_ID = BitmapID.ALIEN;

    // DrawParams to draw Alien and Explosion
    private DrawImage DRAW_ALIEN;
    private DrawImage DRAW_EXPLOSION;

    private BitmapData bulletBitmapData;
    private SpriteAnimation explodeAnimation;
    // draws animated healthbar above Alien if Alien is damaged
    private HealthBarAnimation healthBarAnimation;
    // stores any running animations showing health leaving alien
    private List<LoseHealthAnimation> loseHealthAnimations = new LinkedList<>();

    public Alien(int spriteId, float x, float y, float scrollSpeed, Spaceship spaceship,
            int difficulty, GameContext gameContext) {
        super(spriteId, SpriteType.ALIEN, x, y, BITMAP_ID, gameContext);
        speedX = scrollSpeed / 2.5f;
        this.spaceship = spaceship;

        bulletBitmapData = gameContext.getBitmapCache().getData(BitmapID.ALIEN_BULLET);
        explodeAnimation = gameContext.getAnimCache().get(BitmapID.SPACESHIP_EXPLODE);

        DRAW_ALIEN = new DrawImage(BITMAP_ID);
        DRAW_EXPLOSION = new DrawImage(explodeAnimation.getBitmapID());

        this.difficulty = difficulty;

        initAlien();
    }

    private void initAlien() {
        startingY = y;
        amplitude = 70 + random.nextInt(60);
        period = 250 + random.nextInt(100);
        vShift = random.nextInt(20);
        hShift = -random.nextInt(3);
        hp = 10 + difficulty / 100;
        bulletDelay = 20;
        framesSinceLastBullet = -bulletDelay;
        bulletsLeft = 4; // todo: implement AlienBullet FireManager?
        hitBox = new Rectangle(x + getWidth() * 0.2f, y + getHeight() * 0.2f, x + getWidth() * 0.8f, y + getHeight() * 0.8f);
        healthBarAnimation = new HealthBarAnimation(getWidth(), getHeight(), hp);
    }

    @Override
    public void updateActions(UpdateContext updateContext) {
        // terminate after explosion or if out of bounds
        if (explodeAnimation.hasPlayed() || !isInBounds()) {
            terminate = true;
            updateContext.createdEvents.push(EventID.ALIEN_DIED);
        } else {
            framesSinceLastBullet++;
            // rules for firing: alien has waited long enough, spaceship is alive, alien
            // has bullets left to fire, and alien is on right half of the screen.
            // To slightly randomize fire rate there is also only a 30% chance it will fire
            // in this frame, even if all conditions are met
            if (canFire()) {
                fireBullet(spaceship, updateContext);
                framesSinceLastBullet = 0;
                bulletsLeft--;
            }
        }
    }

    private boolean canFire() {
        return framesSinceLastBullet >= bulletDelay && !
                spaceship.terminate() &&
                bulletsLeft > 0 &&
                getP(0.3f) &&
                x > gameContext.getGameWidthPx() / 2;
    }
    // fires bullet at sprite with small randomized inaccuracy, based on
    // current coordinates. Bullet initialized halfway down the alien on the left side
    public void fireBullet(Sprite s, UpdateContext updateContext) {
        updateContext.createdChildren.push(gameContext.createAlienBullet(
                bulletBitmapData,
                x,
                y + (int) (getHeight() * 0.5),
                s.getHitboxCenter().getX(),
                s.getHitboxCenter().getY() + (random.nextBoolean() ? -1 : +1) * random.nextInt(50)
        ));
    }

    @Override
    public void updateSpeeds() { // todo: comment, improve
        float projected_y;
        // if sprite in top half of screen, start flying down. Else start flying up
        if (startingY <= 150) {
            projected_y = amplitude * (float) Math.sin(2 * Math.PI / period * (elapsedFrames + hShift)) + startingY + vShift;
        } else { // todo: flying up
            projected_y = amplitude * (float) Math.sin(2 * Math.PI / period * (elapsedFrames + hShift)) + startingY + vShift;
        }
        speedY = (projected_y - y) / 600.0f;
        elapsedFrames++;
    }

    @Override
    public void updateAnimations() {
        if (explodeAnimation.isPlaying()) {
            explodeAnimation.incrementFrame();
        }
    }

    @Override
    public void handleCollision(Sprite s, int damage) {
        takeDamage(damage);
        // increment score and start HealthBarAnimation and LoseHealthAnimations
        // if Alien took damage and isn't dead.
        if (!dead && damage > 0) {
//            GameView.incrementScore(damage);
            healthBarAnimation.start();
            loseHealthAnimations.add(new LoseHealthAnimation(getWidth(), getHeight(),
                    s.getX() - x, s.getY() - y, damage));
        }
        // if hp has hit zero and dead is false, set it to true.
        // This means hp has hit zero for the first time, and
        // Alien was "killed" by the collision. Start explodeAnimation.
        if (hp == 0 && !dead) {
            dead = true;
            explodeAnimation.start();
        }
    }

    @Override
    public void getDrawParams(ProtectedQueue<DrawParams> drawQueue) {
        // only draw alien if it is not in last frame of explode animation
        if (explodeAnimation.getFramesLeft() >= 1) {
            DRAW_ALIEN.setCanvasX0(x);
            DRAW_ALIEN.setCanvasY0(y);
            drawQueue.push(DRAW_ALIEN);
        }
        // Update and draw loseHealthAnimations
        for (LoseHealthAnimation anim : loseHealthAnimations) {
            if (!anim.isFinished()) {
                anim.update();
                anim.getDrawParams(x, y, drawQueue);
            }
        }
        // update and draw healthBarAnimation if showing
        if (healthBarAnimation.isShowing()) {
            healthBarAnimation.update();
            healthBarAnimation.getDrawParams(x, y, hp, drawQueue);
        }
        // add explodeAnimation params if showing
        if (explodeAnimation.isPlaying()) {
            DRAW_EXPLOSION.setCanvasX0(x);
            DRAW_EXPLOSION.setCanvasY0(y);
            DRAW_EXPLOSION.setDrawRegion(explodeAnimation.getCurrentFrameSrc());
            drawQueue.push(DRAW_EXPLOSION);
        }
    }
}
