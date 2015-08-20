import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

/**
 * Created by Stefan on 8/12/2015.
 */
public class Sprite {

    // coordinates of sprite
    protected int x;
    protected int y;

    // intended movement in x and y directions each frame
    protected float speedX;
    protected float speedY;

    // sprite width and height
    protected int width;
    protected int height;

    // whether or not sprite is visible
    protected boolean vis;

    // sprite default image
    protected BufferedImage defaultImage;

    // what sprite actually looks like now (for animations)
    protected BufferedImage currentImage;

    // whether or not sprite is currently moving
    protected boolean moving;

    // sets sprite coordinates
    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
        vis = true;
        moving = false;
        speedX = 0.0f;
        speedY = 0.0f;
    }

    // initializes with image at (0,0)
    public Sprite(String imageName) {
        this(0, 0);
        loadDefaultImage(imageName);
    }

    // loads sprite's default image
    protected void loadDefaultImage(String imageName) {
        ImageIcon icon = new ImageIcon(imageName);
        try {
            defaultImage = ImageIO.read(new File(imageName));
        } catch(IOException e) {
            e.printStackTrace();
        }
        currentImage = defaultImage;
    }

    // returns dimensions of sprite image
    protected void getImageDimensions() {
        width = defaultImage.getWidth(null);
        height = defaultImage.getHeight(null);
    }

    public Image getCurrentImage() { return currentImage; }

    public int getX() { return x; }

    public int getY() { return y; }
    
    public void setX(int x) { this.x = x; }
    
    public void setY(int y) { this.y = y; }

    public boolean isVisible() { return vis; }

    public void setVisible(Boolean visible) { vis = visible; }

    // draws sprite at current coordinates on g
    public void render(Graphics2D g, ImageObserver o) {
        g.drawImage(currentImage, x, y, o);
    }

    //abstract void update();
}
