import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Sprite extends Component {
    public ArrayList<BufferedImage> images;
    public int width, height; // Actual width * height of image(s)
    public boolean isBg;
    public GamePanel gamePanel;

    public boolean show;
    public BufferedImage currentImage;
    public String direction = "down";
    public int counter = 0;
    public int counterMax = 10;
    public int num = 0;

    public Sprite(String imagePath, int width, int height, boolean isBg, GamePanel gamePanel) {
        this.images = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.isBg = isBg;
        this.show = false;

        try {
            this.currentImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.gamePanel = gamePanel;
        gamePanel.sprites.add(this);
    }

    public Sprite(ArrayList<String> imagePaths, int width, int height, boolean isBg, GamePanel gamePanel) {
        this.images = new ArrayList<>();
        try {
            for(String path : imagePaths) {
                this.images.add(ImageIO.read(getClass().getClassLoader().getResourceAsStream(path)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.width = width;
        this.height = height;
        this.isBg = isBg;
        this.show = false;
        this.currentImage = images.getFirst();
        this.gamePanel = gamePanel;
        gamePanel.sprites.add(this);
    }

    public void update() {
        if(!images.isEmpty()) {
            if (counter >= counterMax) {
                if (++num >= images.size()) num = 0;
                counter = 0;
            }
            currentImage = images.get(num);
        }
    }

    public void draw(Graphics2D g2) {
        //System.out.printf("Entity: %s, show: %s\n", this.entity, this.show);
        if (this.entity != null) {
            if (this.show) {
                if(this.entity.equals(gamePanel.doc)) g2.drawImage(currentImage, gamePanel.doc.cameraX, gamePanel.doc.cameraY, width * gamePanel.scale, height * gamePanel.scale, null);
                else g2.drawImage(this.currentImage, this.entity.getX() - gamePanel.doc.cameraX, this.entity.getY() - gamePanel.doc.cameraY, width * 4, height * 4, null);
            }
        }
    }
}
