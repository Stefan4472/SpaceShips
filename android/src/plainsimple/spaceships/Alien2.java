package plainsimple.spaceships;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Stefan on 9/26/2015.
 */
public class Alien2 extends Alien { // todo: not implemented

    public Alien2(Bitmap defaultImage, float x, float y) {
        super(defaultImage, x, y);
        initAlien();
    }

    private void initAlien() {
        startingY = y;
        hp = 40 + (int) GameView.difficulty / 4;
        bulletDelay = 1_000 - GameView.difficulty;
        bulletSpeed = -3.0f - random.nextInt(5) / 5;
        hitBox.setOffsets(5, 5);
        hitBox.setDimensions(40, 40);
        damage = 100;
        speedX = -2.0f;
    }

    @Override
    void updateActions() {

    }

    @Override
    void updateSpeeds() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(defaultImage, x, y, null);
    }
    @Override
    void fireBullet(Sprite s) {

    }
}