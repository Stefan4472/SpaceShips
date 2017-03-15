package com.plainsimple.spaceships.sprite;

import android.content.Context;

import com.plainsimple.spaceships.helper.AnimCache;
import com.plainsimple.spaceships.helper.BitmapCache;
import com.plainsimple.spaceships.helper.BitmapID;
import com.plainsimple.spaceships.helper.Hitbox;

/**
 * Created by Stefan on 3/15/2017.
 */

public class Rocket2 extends Rocket {

    protected Rocket2(Context context, float x, float y) {
        super(BitmapCache.getData(BitmapID.ROCKET_2, context), x, y);
        speedX = 0.005f;
        hp = 35;
        hitBox = new Hitbox(x + getWidth() * 0.7f, y - getHeight() * 0.2f, x + getWidth() * 1.5f, y + getHeight() * 1.2f);

        move = AnimCache.get(BitmapID.ROCKET_MOVE, context);
        explode = AnimCache.get(BitmapID.EXPLOSION_1, context);

        move.start();
    }
}