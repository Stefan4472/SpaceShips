package com.plainsimple.spaceships.helper;

/**
 * Created by Stefan on 8/27/2016.
 */
public enum RocketType {

    ROCKET(BitmapID.ROCKET, BitmapID.ROCKET_OVERLAY, 40, 40, 0.0067f);

    // BitmapID of fired bullet when it is shown on screen
    private BitmapID drawableId;
    // BitmapID of spaceship indicator overlay
    private BitmapID spaceshipOverlayId;
    // damage bullet does on contact with another sprite
    private int damage;
    // the number of frames that must pass before the Spaceship can fire again
    private int delay;
    // speed the bullet travels when fired
    private float speedX; // todo: acceleration class?

    RocketType(BitmapID drawableId, BitmapID spaceshipOverlayId, int damage, int delay, float speedX) {
        this.drawableId = drawableId;
        this.spaceshipOverlayId = spaceshipOverlayId;
        this.damage = damage;
        this.delay = delay;
        this.speedX = speedX;
    }

    public BitmapID getDrawableId() {
        return drawableId;
    }

    public BitmapID getSpaceshipOverlayId() {
        return spaceshipOverlayId;
    }

    public int getDamage() {
        return damage;
    }

    public int getDelay() {
        return delay;
    }

    public float getSpeedX() {
        return speedX;
    }
}
