import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Stefan on 8/12/2015.
 */
public class Canvas extends JLabel {

    /* Image to be displayed on Canvas */
    private BufferedImage display;

    /* Construct JPanel, initialize display to dimensions */
    public Canvas(int width, int height) {
        super();
        display = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(width, height));
    }

    /* Draws display onto Canvas */
    public void update() {
        setIcon(new ImageIcon(display));
    }

    public void setBackgroundColor(Color c) {
        Graphics2D image = display.createGraphics();
        image.setColor(c);
        image.fillRect(0, 0, display.getWidth(), display.getHeight());
        update();
    }
}
