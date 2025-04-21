import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite extends Component {
    public ArrayList<BufferedImage> images;
    public int width, height;
    public boolean isBg;

    public boolean show;
    public BufferedImage currentImage;
    public String direction = "down";
    public int counter = 0;
    public int num = 1;

    public Sprite(BufferedImage image, int width, int height, boolean isBg) {
        this.width = width;
        this.height = height;
        this.isBg = isBg;
        this.show = false;
        this.currentImage = image;
    }

    public Sprite(ArrayList<BufferedImage> images, int width, int height, boolean isBg) {
        this.images = images;
        this.width = width;
        this.height = height;
        this.isBg = isBg;
        this.show = false;
        this.currentImage = images.getFirst();
    }

    public void draw(Graphics2D g2) {
        if(this.show) {
            g2.drawImage(this.currentImage, this.entity.getX(), this.entity.getY(), width * 4, height * 4, null);
        }
    }
}
