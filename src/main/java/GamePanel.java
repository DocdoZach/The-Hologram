import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    public ArrayList<Map> maps;

    public ArrayList<Sprite> sprites;
    public static ArrayList<Body> bodies;

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

        maps = new ArrayList<>();
        sprites = new ArrayList<>();
        bodies = new ArrayList<>();

        Character docCharacter = new Character(1, 15, 1, 1, 1);

        Sprite docSpriteUp = new Sprite(new ArrayList<>(Arrays.asList(
                "player/doc_back.png",
                "player/doc_walking_back-1.png",
                "player/doc_back.png",
                "player/doc_walking_back-2.png")), 12, 20, false, this);
        Sprite docSpriteDown = new Sprite(new ArrayList<>(Arrays.asList(
                "player/doc.png",
                "player/doc_walking-1.png",
                "player/doc.png",
                "player/doc_walking-2.png")), 12, 20, false, this);
        Sprite docSpriteLeft = new Sprite(new ArrayList<>(Arrays.asList(
                "player/doc_left.png",
                "player/doc_walking_left.png",
                "player/doc_left.png",
                "player/doc_walking_left.png")), 12, 20, false, this);
        Sprite docSpriteRight = new Sprite(new ArrayList<>(Arrays.asList(
                "player/doc_right.png",
                "player/doc_walking_right.png",
                "player/doc_right.png",
                "player/doc_walking_right.png")), 12, 20, false, this);

        Body docBody = new Body(new Rectangle(12, 72, 24, 8), this);

        doc = new Player("Doc", 480, 768, docComponents, new ArrayList<Item>(), this, keyHandler, tileManager);
        doc.addComponent(docBody);
        doc.addComponent(docCharacter);

        MultiSprite docSprites = new MultiSprite(doc);
        docSprites.addSprite("up", docSpriteUp);
        docSprites.addSprite("down", docSpriteDown);
        docSprites.addSprite("left", docSpriteLeft);
        docSprites.addSprite("right", docSpriteRight);
        doc.addComponent(docSprites);
        doc.getComponent(MultiSprite.class).useSprite("down");

        setMaps();
        tileManager.loadMap(beachMap);
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
        for(Sprite sprite : sprites) {
            sprite.update();
        }
        doc.update();
        checkMapExits(doc);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileManager.draw(g2);
        for(Sprite sprite : sprites) {
            if (sprite.isBg) {
                sprites.remove(sprite);
                sprites.addFirst(sprite);
            }
        }
        for(Sprite sprite : sprites) {
            sprite.draw(g2);
        }

        g2.dispose();
    }

    public void checkMapExits(Player player) {

        // beach <-> river
        exitedMapVertically(doc, beachMap, riverMap, 0, 1600, -44, player.getX(), 1520);
        exitedMapVertically(doc, riverMap, beachMap, 0, 1600, 1556, player.getX(), 0);

        // enter/exit house SE
        exitedMapVertically(doc, riverMap, houseSEMap, 1084, 1108, 856, 376, 528);
        exitedMapVertically(doc, houseSEMap, riverMap, 340, 412, 560, 1096, 864);

        // enter/exit house SW
        exitedMapVertically(doc, riverMap, houseSWMap, 348, 372, 440, 376, 528);
        exitedMapVertically(doc, houseSWMap, riverMap, 340, 412, 560, 360, 448);

        // enter/exit house NW
        exitedMapVertically(doc, riverMap, houseNWMap, 540, 564, 200, 376, 528);
        exitedMapVertically(doc, houseNWMap, riverMap, 340, 412, 560, 552, 208);

        // enter/exit house NE
        exitedMapVertically(doc, riverMap, houseNEMap, 1404, 1428, 456, 376, 528);
        exitedMapVertically(doc, houseNEMap, riverMap, 340, 412, 560, 1416, 464);

        // river <-> patch
        exitedMapVertically(doc, riverMap, patchMap, 0, 1600, -44, player.getX(), 1520);
        exitedMapVertically(doc, patchMap, riverMap, 0, 1600, 1556, player.getX(), 0);
    }

    public Entity mapEntity(String kind, int x, int y) {
        Entity tree = new Entity("tree", x, y, new ArrayList<>());
        Sprite treeSprite = new Sprite("sprites/tree.png", 32, 40, false, this);
        Body treeBody = new Body(new Rectangle(44, 100, 40, 60), this);
        tree.addComponent(treeSprite);
        tree.addComponent(treeBody);
        if (kind.equals("tree")) return tree;

        Entity house = new Entity("house", x, y, new ArrayList<>());
        Sprite houseSprite = new Sprite("sprites/house.png", 56, 56, false, this);
        Body houseBody = new Body(new Rectangle(32, 96, 160, 128), this);
        house.addComponent(houseSprite);
        house.addComponent(houseBody);
        if (kind.equals("house")) return house;

        Entity well = new Entity("well", x, y, new ArrayList<>());
        Sprite wellSprite = new Sprite("sprites/well.png", 56, 56, false, this);
        Body wellBody = new Body(new Rectangle(32, 96, 160, 128), this);
        house.addComponent(wellSprite);
        house.addComponent(wellBody);
        if (kind.equals("well")) return well;

        return new Entity();
    }

    public void setMaps() {

        this.beachMap = new Map("Beach", 50, 50, "maps/beach_map.txt", new ArrayList<>());
        this.beachMap.getEntities().add(mapEntity("tree", 788, 92));
        this.beachMap.getEntities().add(mapEntity("tree", 200, 188));
        this.maps.add(beachMap);

        this.riverMap = new Map("River", 50, 50, "maps/river_map.txt", new ArrayList<>());
        this.riverMap.getEntities().add(mapEntity("tree", 364, 616));
        this.riverMap.getEntities().add(mapEntity("tree", 480, 848));
        this.riverMap.getEntities().add(mapEntity("tree", 300, 1112));
        this.riverMap.getEntities().add(mapEntity("tree", 716, 916));
        this.riverMap.getEntities().add(mapEntity("tree", 232, 440));
        this.riverMap.getEntities().add(mapEntity("tree", 1180, 816));
        this.riverMap.getEntities().add(mapEntity("tree", 636, 512));
        this.riverMap.getEntities().add(mapEntity("tree", 892, 376));
        this.riverMap.getEntities().add(mapEntity("tree", 24, 144));
        this.riverMap.getEntities().add(mapEntity("tree", 1356, 1060));
        this.riverMap.getEntities().add(mapEntity("house", 1008, 704));
        this.riverMap.getEntities().add(mapEntity("house", 272, 288));
        this.riverMap.getEntities().add(mapEntity("house", 464, 48));
        this.riverMap.getEntities().add(mapEntity("house", 1328, 304));
        this.riverMap.getEntities().add(mapEntity("well", 784, 432));
        this.maps.add(riverMap);

        this.houseSEMap = new Map("House SE", 25, 20, "maps/houseSE_map.txt", new ArrayList<>());
        this.maps.add(riverMap);

        this.houseSWMap = new Map("House SW", 25, 20, "maps/houseSW_map.txt", new ArrayList<>());
        this.maps.add(houseSWMap);

        this.houseNWMap = new Map("House NW", 25, 20, "maps/houseNW_map.txt", new ArrayList<>());
        this.maps.add(houseNWMap);

        this.houseNEMap = new Map("House NE", 25, 20, "maps/houseNE_map.txt", new ArrayList<>());
        this.maps.add(houseNEMap);

        this.patchMap = new Map("Patch", 50, 50, "maps/patch_map.txt", new ArrayList<>());
        this.patchMap.getEntities().add(mapEntity("tree", 32, 1092));
        this.patchMap.getEntities().add(mapEntity("tree", 112, 628));
        this.patchMap.getEntities().add(mapEntity("tree", 316, 1124));
        this.patchMap.getEntities().add(mapEntity("tree", 340, 280));
        this.patchMap.getEntities().add(mapEntity("tree", 948, 540));
        this.patchMap.getEntities().add(mapEntity("tree", 1128, 868));
        this.maps.add(patchMap);

        this.ruinsMap = new Map("Ruins", 50, 50, "maps/ruins_map.txt", new ArrayList<>());
        this.maps.add(ruinsMap);

        this.westBeachMap = new Map("West Beach", 50, 50, "maps/west_beach_map.txt", new ArrayList<>());
        this.maps.add(westBeachMap);

        this.castleGateMap = new Map("Castle Gate", 50, 50, "maps/castle_gate_map.txt", new ArrayList<>());
        this.maps.add(castleGateMap);

        this.eastBeachMap = new Map("East Beach", 50, 50, "maps/east_beach_map.txt", new ArrayList<>());
        this.maps.add(eastBeachMap);

        this.deltaMap = new Map("Delta", 50, 50, "maps/delta_map.txt", new ArrayList<>());
        this.maps.add(deltaMap);

        this.lakeMap = new Map("Lake", 50, 50, "maps/lake_map.txt", new ArrayList<>());
        this.maps.add(lakeMap);

        this.easterBeachMap = new Map("Easter Beach", 50, 50, "maps/easter_beach_map.txt", new ArrayList<>());
        this.maps.add(easterBeachMap);

        this.towerMap = new Map("Tower", 50, 50, "maps/tower_map.txt", new ArrayList<>());
        this.maps.add(towerMap);

        for(Map map : maps) {
            map.toggleMap(beachMap);
        }
        for(Entity entity : beachMap.getEntities()) {
            bodies.add(entity.getComponent(Body.class));
        }
    }

    public void exitedMapVertically(Player player, Map mapFrom, Map mapTo, int x1, int x2, int previousY, int nextX, int nextY) {
        if(tileManager.currentMap.equals(mapFrom) && player.getX() >= x1 && player.getX() <= x2 && player.getY() == previousY) {
            if(printMapTransitions) System.out.println(mapFrom.getName() + " -> " + mapTo.getName());
            tileManager.loadMap(mapTo);
            player.setX(nextX);
            player.setY(nextY);
        }
    }

    public void exitedMapHorizontally(Player player, Map mapFrom, Map mapTo, int y1, int y2, int previousX, int nextX, int nextY) {
        if(tileManager.currentMap.equals(mapFrom) && player.getY() >= y1 && player.getY() <= y2 && player.getX() == previousX) {
            if(printMapTransitions) System.out.println(mapFrom.getName() + " -> " + mapTo.getName());
            tileManager.loadMap(mapTo);
            player.setX(nextX);
            player.setY(nextY);
        }
    }
}
