package main.java;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 8;
    final int scale = 4;

    final int tileSize = originalTileSize * scale; // 32x32 tile
    final int maxScreenCol = 25;
    final int maxScreenRow = 20;
    final int screenWidth = tileSize * maxScreenCol; // 800 width
    final int screenHeight = tileSize * maxScreenRow; // 640 height

    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.DARK_GRAY);
        this.setDoubleBuffered(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while(gameThread != null) {
            // Update
            update();

            // Draw
            repaint();
        }
    }

    public void update() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);

        g2.fillRect(100, 100, 48, 80);

        g2.dispose();
    }
}
