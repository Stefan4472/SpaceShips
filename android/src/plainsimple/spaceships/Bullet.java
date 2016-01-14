package plainsimple.spaceships;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Stefan on 8/17/2015.
 */
public class Bullet extends Sprite {

    private SpriteAnimation bulletFiring;
    // delay between fired bullets
    private int delay;

    // bulletType defines bullet damage and sprite
    public final static int LASER = 1;
    public final static int ION = 2;
    public final static int PLASMA = 3;
    public final static int PLUTONIUM = 4;

    // todo: when resources can be different defaultImage shouldn't be a parameter
    public Bullet(Bitmap defaultImage, float x, float y, int bulletType) {
        super(defaultImage, x, y);
        switch(bulletType) {
            case LASER: // todo: figure out how to load resource in each case
                damage = 10;
                delay = 150;
                hitBox.setDimensions((int) (width * 1.5f), height);
                speedX = 0.01f;
                break;
            case ION:
                damage = 20;
                delay = 130;
                hitBox.setDimensions((int) (width * 1.5f), height);
                speedX = 0.011f;
                break;
            case PLASMA:
                damage = 30;
                delay = 100;
                hitBox.setDimensions((int) (width * 1.5f), height);
                speedX = 0.012f;
                break;
            case PLUTONIUM:
                damage = 40;
                delay = 170;
                hitBox.setDimensions((int) (width * 1.5f), height);
                speedX = 0.013f;
                break;
            default:
                System.out.println("Invalid bulletType (" + bulletType + ")");
                break;
        }
    }

    // returns delay (ms) given bulletType
    public static int getDelay(int bulletType) {
        switch(bulletType) {
            case LASER:
                return 150;
            case ION:
                return 130;
            case PLASMA:
                return 100;
            case PLUTONIUM:
                return 170;
            default:
                return -1;
        }
    }

    public void updateActions() {

    }

    public void updateSpeeds() {

    }

    public void handleCollision(Sprite s) {
        collision = true;
        if (s instanceof Obstacle || s instanceof Alien) {
            vis = false;
        }
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawBitmap(defaultImage, x, y, null);
    }
}
