package com.plainsimple.spaceships.sprite;

import android.graphics.Rect;
import android.util.Log;

import com.plainsimple.spaceships.activity.GameActivity;
import com.plainsimple.spaceships.helper.BitmapData;
import com.plainsimple.spaceships.helper.DrawParams;
import com.plainsimple.spaceships.helper.DrawSubImage;
import com.plainsimple.spaceships.helper.Hitbox;
import com.plainsimple.spaceships.helper.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 8/28/2015.
 */
public class Coin extends Sprite {

    private SpriteAnimation spin;
    private SpriteAnimation disappear;

    public Coin(BitmapData bitmapData, SpriteAnimation spinAnimation, SpriteAnimation disappearAnimation, float x, float y) {
        super(bitmapData, x, y);
        //spin = new SpriteAnimation(spinAnimation, width, height, 5, true);
        spin = spinAnimation;
        spin.start();
        //disappear = new SpriteAnimation(disappearAnimation, width, height, 1, false);
        disappear = disappearAnimation;
        initObstacle();
    }

    private void initObstacle() {
        hitBox = new Hitbox(x + getWidth() * 0.3f, y + getHeight() * 0.1f, x + getWidth() * 0.7f, y + getHeight() * 0.9f);
    }

    @Override
    public void updateActions() {
        if (disappear.hasPlayed()) {
            terminate = true;
        }
        if (!isInBounds()) {
            terminate = true;
            Log.d("Termination", "Removing Coin at x = " + x);
        }
    }

    @Override
    public void updateSpeeds() {

    }

    @Override
    public void updateAnimations() {
        if (disappear.isPlaying()) {
            disappear.incrementFrame();
        } else {
            spin.incrementFrame();
        }
    }

    @Override
    public void handleCollision(Sprite s) {
        if (s instanceof Spaceship) {
            //disappear.start();
            GameActivity.incrementScore(GameActivity.COIN_VALUE);
            collides = false;
        }
    }

    @Override
    public List<DrawParams> getDrawParams() {
        drawParams.clear();
        /*if (disappear.isPlaying()) {
            Rect source = disappear.getCurrentFrameSrc();
            params.add(new DrawParams(disappear.getBitmapID(), x, y, source.left, source.top, source.right, source.bottom));
        } else {*/
            Rect source = spin.getCurrentFrameSrc();
            drawParams.add(new DrawSubImage(spin.getBitmapID(), x, y, source.left, source.top, source.right, source.bottom));
        //}
        return drawParams;
    }
}
