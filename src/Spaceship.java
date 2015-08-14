import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Stefan on 8/13/2015.
 */
public class Spaceship extends Sprite {

    /**
     * Movement vector in x-direction.
     */
    private int dx;

    /**
     * Movement vector in y-direction.
     */
    private int dy;

    // Sprite's image when moving
    private SpriteAnimation movingAnimation;

    private SpriteAnimation startMovingAnimation;

    private float acceleration;

    // keeps track of fired rockets
    private ArrayList<Rocket> rockets;

    /**
     * Default constructor, initializes sprite.
     */
    public Spaceship(int x, int y) {
        super(x, y);
        initCraft();
    }

    private void initCraft() {
        defaultImage = new ImageIcon("spaceship.png").getImage();

        startMovingAnimation = new SpriteAnimation(new File[] {
                new File("spaceship_starting1.png"),
                new File("spaceship_starting2.png"),
                new File("spaceship_starting3.png"),
                new File("spaceship_starting4.png"),
                new File("spaceship_starting5.png")
        }, false);

        movingAnimation = new SpriteAnimation(new File[] {
                new File("spaceship_moving1.png"),
                new File("spaceship_moving2.png")
        }, true);

        currentImage = defaultImage;

        moving = false;
        acceleration = 0;

        getImageDimensions();

        rockets = new ArrayList<>();

        x = 40;
        y = 60;
    }

    public ArrayList<Rocket> getRockets() { return rockets; }

    /**
     * Changes coordinates of sprite by adding x- and y-
     * vectors to current position.
     */
    public void move() {
        // sprite direction not equal to zero
        if(dx != 0 || dy != 0) {
            // sprite was previously not moving. Play startmoving animation and reset acceleration
            if(moving == false) {
                currentImage = startMovingAnimation.start();
            } else { // sprite was previously moving. Increase acceleration
                if(startMovingAnimation.isPlaying()) {
                    currentImage = startMovingAnimation.nextFrame();
                } else { // Play moving animation as soon as startmoving animation is over
                    currentImage = movingAnimation.nextFrame();
                }
            }
            moving = true;
        } else {
            currentImage = defaultImage;
            moving = false;
        }

        if(moving == true && acceleration < 0.1)
            acceleration += 0.01;
        else if(moving == true && acceleration < 2.0)
            acceleration += 0.05;
        else if(moving == false && acceleration >= 0.0)
            acceleration -= 0.05;

        x += dx + dx * acceleration; // todo: once user stops pressing arrow key, dx = 0, so total speed cuts to 0 instead of slowing down
        y += dy;
    }

    // fires rocket
    public void fire() {
        rockets.add(new Rocket(x + width, y + height / 2));
    }

    // returns x-coordinate of sprite's "control point"
    // this is the point from which we want to draw
    public int getX() {
        return x;
    }

    /**
     * Returns y-coordinate of sprite's current position.
     * @return
     */
    public int getY() {
        return y;
    }

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

        if(key == KeyEvent.VK_SPACE)
            fire();
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
    }
}