import java.awt.*;

/**
 * Created by Stefan on 8/20/2015.
 */
public class Obstacle extends Sprite {

    public Obstacle(String imageName) {
        super(imageName);
        initObstacle();
    }

    public Obstacle(String imageName, int x, int y) {
        super(imageName, x, y);
        initObstacle();
    }

    private void initObstacle() {
        hitBox.setDimensions(50, 50);
    }

    public void updateCurrentImage() {

    }

    public void updateActions() {

    }

    public void updateSpeedX() {

    }

    public void updateSpeedY() {

    }

    public void handleCollision(Sprite s) {

    }
}
