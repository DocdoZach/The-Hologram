import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Sprite extends Component {
    private final ArrayList<BufferedImage> IMAGES;
    private final int WIDTH, HEIGHT; // Actual width * height of image(s)
    private boolean bg;
    private GamePanel gamePanel;

    private boolean show;
    private BufferedImage currentImage;
    private int counter = 0;
    private int counterMax = 10;
    private int num = 0;

    public Sprite(String imagePath, int width, int height, boolean bg, GamePanel gamePanel) {
        this.IMAGES = new ArrayList<>();
        this.WIDTH = width;
        this.HEIGHT = height;
        this.bg = bg;
        this.show = false;

        try {
            this.currentImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.gamePanel = gamePanel;
        gamePanel.sprites.add(this);
    }

    public Sprite(ArrayList<String> imagePaths, int width, int height, boolean bg, GamePanel gamePanel) {
        this.IMAGES = new ArrayList<>();
        try {
            for(String path : imagePaths) {
                this.IMAGES.add(ImageIO.read(getClass().getClassLoader().getResourceAsStream(path)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.WIDTH = width;
        this.HEIGHT = height;
        this.bg = bg;
        this.show = false;
        this.currentImage = IMAGES.getFirst();
        this.gamePanel = gamePanel;
        gamePanel.sprites.add(this);
    }

    public void update() {
        if(!IMAGES.isEmpty()) {
            if (counter >= counterMax) {
                if (++num >= IMAGES.size()) num = 0;
                counter = 0;
            }
            currentImage = IMAGES.get(num);
        }
    }

    public void draw(Graphics2D g2) {
        //System.out.printf("Entity: %s, show: %s\n", this.entity, this.show);
        if (this.entity != null) {
            if (this.show) {
                if(this.entity.equals(gamePanel.doc)) g2.drawImage(currentImage, gamePanel.doc.getCameraX(), gamePanel.doc.getCameraY(), WIDTH * gamePanel.scale, HEIGHT * gamePanel.scale, null);
                else {
                    int screenX = this.entity.getX() - gamePanel.doc.getX() + gamePanel.doc.getCameraX();
                    int screenY = this.entity.getY() - gamePanel.doc.getY() + gamePanel.doc.getCameraY();
                    g2.drawImage(this.currentImage, screenX, screenY, WIDTH * 4, HEIGHT * 4, null);
                }
            }
        }
    }

    public boolean isBg() {
        return bg;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void countUp() {
        counter++;
    }

    public int getCounterMax() {
        return counterMax;
    }

    public void setCounterMax(int counterMax) {
        this.counterMax = counterMax;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }
}
