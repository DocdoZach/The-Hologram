import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

public class GamePanel extends JPanel implements Runnable {
    public final int originalTileSize = 8;
    public final int scale = 4;

    public final int tileSize = originalTileSize * scale; // 32x32 tile
    public final int maxScreenCol = 25;
    public final int maxScreenRow = 20;
    public final int screenWidth = tileSize * maxScreenCol; // 800 width
    public final int screenHeight = tileSize * maxScreenRow; // 640 height

    public int FPS = 60;

    public ArrayList<Sprite> sprites;

    ArrayList<Component> docComponents = new ArrayList<Component>();
    public Player doc;

    TileManager tileManager = new TileManager(this);
    public Map currentMap;
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    CollisionChecker collisionChecker = new CollisionChecker(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.DARK_GRAY);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        Character docCharacter = new Character(1, 15, 1, 1, 1);
        docComponents.add(docCharacter);
        BufferedImage up=null, up1=null, up2=null, down=null, down1=null, down2=null, left=null, left1=null, left2=null, right=null, right1=null, right2=null;
        Sprite docSprite = new Sprite(new ArrayList<>(Arrays.asList(up, up1, up2, down, down1, down2, left, left1, left2, right, right1, right2)), 12, 20, false);
        docComponents.add(docSprite);
        doc = new Player("Doc", screenWidth / 2 - 24, screenHeight / 2 - 40, docComponents, new ArrayList<Item>(), this, keyHandler);
        Body docBody = new Body(new Rectangle(12, 72, 24, 8), this);
        doc.addComponent(docBody);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {
            long currentTime = System.nanoTime();

            update();
            repaint();
            timer += (long) (nextDrawTime - currentTime);
            drawCount++;

            if(timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if(remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        doc.update();
        if(keyHandler.ePressed) {
            System.out.println(tileManager.isPointSolid(doc.getX(), doc.getY()));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileManager.draw(g2);
        doc.draw(g2);

        g2.dispose();
    }
}
