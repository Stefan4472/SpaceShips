package plainsimple.spaceships;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stefan on 8/13/2015.
 */
public class Spaceship extends Sprite {

    // arrowkey direction in y
    private int dy;

    private final float MAX_SPEED_Y = 3.5f;

    private SpriteAnimation movingAnimation; // todo: resources static?
    private SpriteAnimation fireRocketAnimation;
    private SpriteAnimation explodeAnimation;

    private Bitmap rocketBitmap;
    private Bitmap bulletBitmap;

    // whether user has control over spaceship
    boolean controllable;

    public final int BULLET_LASER = 1; // todo: move to bullet class. Also, rocket types
    public final int BULLET_ION = 2;
    public final int BULLET_PLASMA = 3;

    private boolean firesBullets;
    // ms to wait between firing bullets
    private int bulletDelay = 100;
    private int bulletType = 10;
    private long lastFiredBullet;
    private boolean firingBullets;

    private boolean firesRockets;
    // ms to wait between firing rockets
    private int rocketDelay = 420;
    private int rocketType = 40;
    private long lastFiredRocket;
    private boolean firingRockets;

    // keeps track of fired bullets and rockets
    private ArrayList<Sprite> projectiles;

    public ArrayList<Sprite> getProjectiles() {
        return projectiles;
    }

    public void setControllable(boolean controllable) {
        this.controllable = controllable;
    }
    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setBullets(boolean firesBullets, int bulletType, int bulletDelay) {
        this.firesBullets = firesBullets;
        this.bulletType = bulletType;
        this.bulletDelay = bulletDelay;
    }

    public void setRockets(boolean firesRockets, int rocketType, int rocketDelay) {
        this.firesRockets = firesRockets;
        this.rocketType = rocketType;
        this.rocketType = rocketDelay;
    }

    // default constructor
    public Spaceship(Bitmap defaultImage, int x, int y, Board board) {
        super(defaultImage, x, y, board);
        initCraft();
    }

    private void initCraft() {
        projectiles = new ArrayList<>();
        lastFiredBullet = 0;
        lastFiredRocket = 0;

        damage = 100;
        controllable = true;
        collides = true;
        hitBox.setDimensions(33, 28);
        hitBox.setOffsets(12, 11);
    }

    // get spritesheet bitmaps and construct them
    public void injectResources(Bitmap movingSpriteSheet, Bitmap fireRocketSpriteSheet,
                                Bitmap explodeSpriteSheet, Bitmap rocketBitmap, Bitmap bulletBitmap) {
        movingAnimation = new SpriteAnimation(movingSpriteSheet, 50, 50, 5, true);
        fireRocketAnimation = new SpriteAnimation(fireRocketSpriteSheet, 50, 50, 8, false);
        explodeAnimation = new SpriteAnimation(explodeSpriteSheet, 50, 50, 5, false);
        this.rocketBitmap = rocketBitmap;
        this.bulletBitmap = bulletBitmap;
    }

    public void updateActions() {
        if (firingBullets && lastFiredBullet + bulletDelay <= System.currentTimeMillis()) {
            fireBullets();
            lastFiredBullet = System.currentTimeMillis();
        }

        if (firingRockets && lastFiredRocket + rocketDelay <= System.currentTimeMillis()) {
            fireRockets();
            lastFiredRocket = System.currentTimeMillis();
            fireRocketAnimation.start();
        }
    }

    // fires two rockets
    public void fireRockets() {
        projectiles.add(new Rocket(rocketBitmap, x + 43, y + 15, rocketType, board));
        projectiles.add(new Rocket(rocketBitmap, x + 43, y + 33, rocketType, board));
    }

    // fires two bullets
    public void fireBullets() {
        projectiles.add(new Bullet(bulletBitmap, x + 43, y + 15, bulletType, board));
        projectiles.add(new Bullet(bulletBitmap, x + 43, y + 33, bulletType, board));
    }

    public void updateSpeeds() {
        speedY = Math.abs(speedY);
        if (speedY < MAX_SPEED_Y) {
            speedY += 0.25;
        } else if (speedY > MAX_SPEED_Y) {
            speedY = MAX_SPEED_Y;
        }
        speedY *= dy;
    }

    public void handleCollision(Sprite s) {
        hp -= s.getDamage();
        if (hp < 0) {
            explodeAnimation.start();
            hp = 0;
            collision = true;
        }
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawBitmap(defaultImage, (float) x, (float) y, null);
        if (moving) {
            canvas.drawBitmap(movingAnimation.nextFrame(), (float) x, (float) y, null);
        }
        if (fireRocketAnimation.isPlaying()) {
            canvas.drawBitmap(fireRocketAnimation.nextFrame(), (float) x, (float) y, null);
        }
        if (explodeAnimation.isPlaying()) {
            canvas.drawBitmap(explodeAnimation.nextFrame(), (float) x, (float) y, null);
        }

    }

    // Sets direction of sprite based on key pressed.
    public void keyPressed(KeyEvent e) {
        if (controllable) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP) {
                dy = -1;
            } else if (key == KeyEvent.VK_DOWN) {
                dy = 1;
            } else if (key == KeyEvent.VK_SPACE && firesBullets) {
                firingBullets = true;
            } else if (key == KeyEvent.VK_X && firesRockets) {
                firingRockets = true;
            }
        }
    }

    // sets movement direction to zero once key is released
    public void keyReleased(KeyEvent e) {
        if (controllable) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP) {
                dy = 0;
            } else if (key == KeyEvent.VK_DOWN) {
                dy = 0;
            } else if (key == KeyEvent.VK_SPACE) {
                firingBullets = false;
            } else if (key == KeyEvent.VK_X) {
                firingRockets = false;
            }
        }
    }
}
