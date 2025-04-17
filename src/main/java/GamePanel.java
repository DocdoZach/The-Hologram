package main.java;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    private final int originalTileSize = 8;
    private final int scale = 4;

    private final int tileSize = originalTileSize * scale; // 32x32 tile
    private final int maxScreenCol = 25;
    private final int maxScreenRow = 20;
    private final int screenWidth = tileSize * maxScreenCol; // 800 width
    private final int screenHeight = tileSize * maxScreenRow; // 640 height

    private int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    ArrayList<Component> docComponents = new ArrayList<Component>();
    public static Player doc;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.DARK_GRAY);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        Character docCharacter = new Character(1, 15, 1, 1, 1);
        docComponents.add(docCharacter);
        doc = new Player("Doc", 0, 0, docComponents, new ArrayList<Item>());
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
                System.out.println(doc.getX() + ", " + doc.getY());
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
        if(keyHandler.upPressed) {
            doc.setY(doc.getY() - doc.getMoveSpeed());
        }
        if(keyHandler.downPressed) {
            doc.setY(doc.getY() + doc.getMoveSpeed());
        }
        if(keyHandler.leftPressed) {
            doc.setX(doc.getX() - doc.getMoveSpeed());
        }
        if(keyHandler.rightPressed) {
            doc.setX(doc.getX() + doc.getMoveSpeed());
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);

        g2.fillRect(doc.getX(), doc.getY(), 48, 80);

        g2.dispose();
    }
}
