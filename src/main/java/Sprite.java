import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite extends Component {
    public ArrayList<BufferedImage> images;
    public int width, height;
    public boolean isBg;

    public boolean show;
    public String direction = "down";
    public int counter = 0;
    public int num = 1;

    public Sprite(ArrayList<BufferedImage> images, int width, int height, boolean isBg) {
        this.images = images;
        this.width = width;
        this.height = height;
        this.isBg = isBg;
        this.show = true;
    }

    public void draw(Graphics2D g2) {
        if(this.show) {
            g2.drawImage(this.images.getFirst(), this.entity.getX(), this.entity.getY(), width * 4, height * 4, null);
        }
    }
}
