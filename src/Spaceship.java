import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stefan on 8/13/2015.
 */
public class Spaceship extends Sprite {

    // direction in x and y
    private int dx;
    private int dy;

    // speed in x and y
    private float speedX;
    private float speedY;

    private final float MAX_SPEED_X = 9.0f;
    private final float MAX_SPEED_Y = 2.0f;

    private SpriteAnimation movingAnimation;
    private SpriteAnimation startMovingAnimation;
    private SpriteAnimation fireRocketAnimation;

    // ms to wait between firing bullets
    private final int BULLET_DELAY = 100;
    private long lastFiredBullet;
    private boolean firingBullets;

    // ms to wait between firing rockets
    private final int ROCKET_DELAY = 420;
    private long lastFiredRocket;
    private boolean firingRockets;

    // keeps track of fired rockets
    private ArrayList<Rocket> rockets;
    private ArrayList<Bullet> bullets;

    // default constructor
    public Spaceship(int x, int y) {
        super(x, y);
        initCraft();
    }

    private void initCraft() {
        try {
            currentImage = defaultImage = ImageIO.read(new File("spaceship.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            startMovingAnimation =
                    new SpriteAnimation("spaceship_starting_spritesheet.png", 50, 50, 1, false);
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            movingAnimation = new SpriteAnimation("spaceship_moving_spritesheet.png", 50, 50, 5, true);
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            fireRocketAnimation = new SpriteAnimation("spaceship_firing_spritesheet.png", 50, 50, 8, false);
        } catch(IOException e) {
            e.printStackTrace();
        }

        moving = false;
        speedX = 0.0f;
        speedY = 0.0f;

        getImageDimensions();

        rockets = new ArrayList<>();
        bullets = new ArrayList<>();
        lastFiredBullet = 0;
        lastFiredRocket = 0;
    }

    public ArrayList<Rocket> getRockets() { return rockets; }

    public ArrayList<Bullet> getBullets() { return bullets; }

    // calculates new x and y coordinates, handles animations,
    // fires rockets
    public void update() {
        // either accelerating or breaking
        if(dx != 0) {
            // sprite was previously not accelerating/breaking. Play startMovingAnimation
            if(moving == false && dx == 1) {
                currentImage = startMovingAnimation.start();
            } else { // sprite was previously moving play startMovingAnimation if it's playing or
                // spaceship is breaking but has not stopped completely
                if(startMovingAnimation.isPlaying() || dx == -1 && speedX != 0.0) {
                    currentImage = startMovingAnimation.nextFrame();
                } else if(dx == 1){ // Play moving animation as soon as startmoving animation is over
                    currentImage = movingAnimation.nextFrame(); // todo: animations
                }
            }
            moving = true;
        } else {
            currentImage = defaultImage;
            moving = false;
        }

        x += getSpeedX();
        if(speedX != 0) // can only move vertically when speed != 0
            y += dy * getSpeedY();

        if(firingBullets == true && lastFiredBullet + BULLET_DELAY <= System.currentTimeMillis()) {
            fireBullets();
            lastFiredBullet = System.currentTimeMillis();
        }
        
        if(firingRockets == true && lastFiredRocket + ROCKET_DELAY <= System.currentTimeMillis()) {
            fireRockets();
            lastFiredRocket = System.currentTimeMillis();
            currentImage = ImageUtil.layer(currentImage, fireRocketAnimation.nextFrame());
        }
        if(fireRocketAnimation.isPlaying())
            currentImage = ImageUtil.layer(currentImage, fireRocketAnimation.nextFrame());
    }

    // fires two rockets
    public void fireRockets() {
        Rocket r1 = new Rocket(x + 43, y + 15);
        Rocket r2 = new Rocket(x + 43, y + 33);
        rockets.add(r1);
        rockets.add(r2);
    }

    // fires two bullets
    public void fireBullets() {
        Bullet b1 = new Bullet(x + 43, y + 15);
        Bullet b2 = new Bullet(x + 43, y + 33);
        bullets.add(b1);
        bullets.add(b2);
    }

    // calculates and returns horizontal speed
    public float getSpeedX() {
        if(dx == 1)
            accelerate();
        else if(dx == 0)
            drift();
        else if(dx == -1)
            applyBreak();

        return speedX;
    }

    // calculates and returns vertical speed
    public float getSpeedY() {
        if(speedY < 1.0) {
            speedY += 0.25;
        } else if(speedY < MAX_SPEED_Y) {
            speedY += 0.10;
        } else if(speedY > MAX_SPEED_Y) {
            speedY = MAX_SPEED_Y;
        }
        return speedY;
    }

    // manages speed when dx = 1
    private void accelerate() {
        if(speedX <= 1.0) {
            speedX += 0.05;
        } else if(speedX <= 3.0) {
            speedX += 0.1;
        } else if(speedX <= 4.0) {
            speedX += 0.05;
        } else if(speedX <= 7.5){
            speedX += 0.0125;
        } else if(speedX <= MAX_SPEED_X){
            speedX += .001;
        } else if(speedX > MAX_SPEED_X) {
            speedX = MAX_SPEED_X;
        }
    }

    // manages speed when dx = -1
    private void applyBreak() {
        if(speedX >= 3.5) {
            speedX -= 0.05;
        } else if(speedX >= 2.0) {
            speedX -= 0.065;
        } else if(speedX >= 1.0) {
            speedX -= .025;
        } else {
            speedX = 0;
        }
    }

    // manages speed when dx = 0
    private void drift() {
        if (speedX > 8.5) {
            speedX -= 0.005;
        } else if (speedX > 7.5) {
            speedX -= 0.008;
        } else if (speedX > 5.0) {
            speedX -= 0.005;
        } else if (speedX > 2.0) {
            speedX -= 0.01;
        } else if (speedX >= 1.0) {
            speedX -= 0.025;
        } else {
            speedX = 0;
        }
    }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    // x-coordinate of center of sprite
    public int getCenterX() { return x + 31; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y;}

    // y-coordinate of center of sprite
    public int getCenterY() { return y + 25; }

    /**
     * Returns this sprite's currentImage.
     * @return
     */
    public Image getCurrentImage() {
        return currentImage;
    }

    /**
     * Sets direction of sprite based on key pressed.
     * @param e
     */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -1;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 1;
        }

        if(key == KeyEvent.VK_SPACE) {
            firingBullets = true;
        }

        if(key == KeyEvent.VK_X) {
            firingRockets = true;
        }
    }

    /**
     * Sets movement direction to zero once key is released
     * @param e
     */
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }

        if(key == KeyEvent.VK_SPACE) {
            firingBullets = false;
        }

        if(key == KeyEvent.VK_X) {
            firingRockets = false;
        }
    }
}
