import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    private boolean printMapTransitions = false;
    public Map beachMap, riverMap, houseSEMap, houseSWMap, houseNWMap, houseNEMap, patchMap, ruinsMap, westBeachMap, castleGateMap, eastBeachMap, deltaMap, lakeMap, easterBeachMap, towerMap;

    public ArrayList<Sprite> sprites;

    ArrayList<Component> docComponents = new ArrayList<Component>();
    public Player doc;

    TileManager tileManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.DARK_GRAY);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        setMaps();

        Character docCharacter = new Character(1, 15, 1, 1, 1);
        docComponents.add(docCharacter);
        BufferedImage up=null, up1=null, up2=null, down=null, down1=null, down2=null, left=null, left1=null, left2=null, right=null, right1=null, right2=null;
        Sprite docSprite = new Sprite(new ArrayList<>(Arrays.asList(up, up1, up2, down, down1, down2, left, left1, left2, right, right1, right2)), 12, 20, false);
        docComponents.add(docSprite);
        doc = new Player("Doc", screenWidth / 2 - 24, screenHeight / 2 - 40, docComponents, new ArrayList<Item>(), this, keyHandler, tileManager);
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
        checkMapExits(doc);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileManager.draw(g2);
        doc.draw(g2);

        g2.dispose();
    }

    public void checkMapExits(Player player) {

        // beach <-> river
        if(tileManager.currentMap.getFilePath().equals("maps/beach_map.txt") && player.getX() >= 0 && player.getX() <= 1600 && player.getY() == -44) {
            if(printMapTransitions) System.out.println("Beach -> River");
            tileManager.loadMap(riverMap);
            player.setY(1520);
        }
        if(tileManager.currentMap.getFilePath().equals("maps/river_map.txt") && player.getX() >= 0 && player.getX() <= 1600 && player.getY() == 1556) {
            if(printMapTransitions) System.out.println("River -> Beach");
            tileManager.loadMap(beachMap);
            player.setY(0);
        }

        // enter/exit house SE
        if(tileManager.currentMap.getFilePath().equals("maps/river_map.txt") && player.getX() >= 1084 && player.getX() <= 1108 && player.getY() == 856) {
            if(printMapTransitions) System.out.println("River -> House SE");
            tileManager.loadMap(houseSEMap);
            player.setX(376);
            player.setY(528);
        }
        if(tileManager.currentMap.getFilePath().equals("maps/houseSE_map.txt") && player.getX() >= 340 && player.getX() <= 412 && player.getY() == 560) {
            if(printMapTransitions) System.out.println("House SE -> River");
            tileManager.loadMap(riverMap);
            player.setX(1096);
            player.setY(864);
        }

        // enter/exit house SW
        if(tileManager.currentMap.getFilePath().equals("maps/river_map.txt") && player.getX() >= 348 && player.getX() <= 372 && player.getY() == 440) {
            if(printMapTransitions) System.out.println("River -> House SW");
            tileManager.loadMap(houseSWMap);
            player.setX(376);
            player.setY(528);
        }
        if(tileManager.currentMap.getFilePath().equals("maps/houseSW_map.txt") && player.getX() >= 340 && player.getX() <= 412 && player.getY() == 560) {
            if(printMapTransitions) System.out.println("House SW -> River");
            tileManager.loadMap(riverMap);
            player.setX(360);
            player.setY(448);
        }

        // enter/exit house NW
        if(tileManager.currentMap.getFilePath().equals("maps/river_map.txt") && player.getX() >= 540 && player.getX() <= 564 && player.getY() == 200) {
            if(printMapTransitions) System.out.println("River -> House NW");
            tileManager.loadMap(houseNWMap);
            player.setX(376);
            player.setY(528);
        }
        if(tileManager.currentMap.getFilePath().equals("maps/houseNW_map.txt") && player.getX() >= 340 && player.getX() <= 412 && player.getY() == 560) {
            if(printMapTransitions) System.out.println("House NW -> River");
            tileManager.loadMap(riverMap);
            player.setX(552);
            player.setY(208);
        }

        // enter/exit house NE
        if(tileManager.currentMap.getFilePath().equals("maps/river_map.txt") && player.getX() >= 1404 && player.getX() <= 1428 && player.getY() == 456) {
            if(printMapTransitions) System.out.println("River -> House NE");
            tileManager.loadMap(houseNEMap);
            player.setX(376);
            player.setY(528);
        }
        if(tileManager.currentMap.getFilePath().equals("maps/houseNE_map.txt") && player.getX() >= 340 && player.getX() <= 412 && player.getY() == 560) {
            if(printMapTransitions) System.out.println("House NE -> River");
            tileManager.loadMap(riverMap);
            player.setX(1416);
            player.setY(464);
        }

        // river <-> patch
        if(tileManager.currentMap.getFilePath().equals("maps/river_map.txt") && player.getX() >= 0 && player.getX() <= 1600 && player.getY() == -44) {
            if(printMapTransitions) System.out.println("River -> Patch");
            tileManager.loadMap(patchMap);
            player.setY(1520);
        }
        if(tileManager.currentMap.getFilePath().equals("maps/patch_map.txt") && player.getX() >= 0 && player.getX() <= 1600 && player.getY() == 1556) {
            if(printMapTransitions) System.out.println("Patch -> River");
            tileManager.loadMap(riverMap);
            player.setY(0);
        }
    }

    public void setMaps() {
        this.beachMap = new Map(50, 50, "maps/beach_map.txt", new ArrayList<>());

        this.riverMap = new Map(50, 50, "maps/river_map.txt", new ArrayList<>());

        this.houseSEMap = new Map(25, 20, "maps/houseSE_map.txt", new ArrayList<>());

        this.houseSWMap = new Map(25, 20, "maps/houseSW_map.txt", new ArrayList<>());

        this.houseNWMap = new Map(25, 20, "maps/houseNW_map.txt", new ArrayList<>());

        this.houseNEMap = new Map(25, 20, "maps/houseNE_map.txt", new ArrayList<>());

        this.patchMap = new Map(50, 50, "maps/patch_map.txt", new ArrayList<>());

        this.ruinsMap = new Map(50, 50, "maps/ruins_map.txt", new ArrayList<>());

        this.westBeachMap = new Map(50, 50, "maps/west_beach_map.txt", new ArrayList<>());

        this.castleGateMap = new Map(50, 50, "maps/castle_gate_map.txt", new ArrayList<>());

        this.eastBeachMap = new Map(50, 50, "maps/east_beach_map.txt", new ArrayList<>());

        this.deltaMap = new Map(50, 50, "maps/delta_map.txt", new ArrayList<>());

        this.lakeMap = new Map(50, 50, "maps/lake_map.txt", new ArrayList<>());

        this.easterBeachMap = new Map(50, 50, "maps/easter_beach_map.txt", new ArrayList<>());

        this.towerMap = new Map(50, 50, "maps/tower_map.txt", new ArrayList<>());
    }
}
