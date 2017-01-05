package com.plainsimple.spaceships.sprite;

import android.graphics.Rect;
import android.util.Log;

import com.plainsimple.spaceships.helper.BitmapData;
import com.plainsimple.spaceships.helper.DrawImage;
import com.plainsimple.spaceships.helper.DrawParams;
import com.plainsimple.spaceships.helper.Hitbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 8/29/2015.
 */
public class AlienBullet extends Sprite {

    public AlienBullet(BitmapData bitmapData, float x, float y) { // todo: damage as a parameter?
        super(bitmapData, x, y);
        // todo: change size for better-looking hit detection? (see Issue #13)
        hitBox = new Hitbox(x, y, x + getWidth(), y + getHeight());
        damage = 10;
    }

    @Override
    public void updateActions() {
        if (!isInBounds()) {
            terminate = true;
            Log.d("Termination", "Removing AlienBullet at x = " + x);
        }
    }

    @Override
    public void updateSpeeds() {

    }

    @Override
    public void updateAnimations() {

    }

    @Override
    public void handleCollision(Sprite s) {
        if(s instanceof Spaceship) {
            collides = false;
            terminate = true;
        }
    }

    @Override
    public List<DrawParams> getDrawParams() {
        drawParams.clear();
        drawParams.add(new DrawImage(bitmapData.getId(), x, y));
        return drawParams;
    }
}
