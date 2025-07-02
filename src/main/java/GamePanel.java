import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class GamePanel extends JPanel implements Runnable {
    public static final boolean XENDY_DEBUG = true;
    public final int originalTileSize = 8;
    public final int scale = 4;

    public final int tileSize = originalTileSize * scale; // 32x32 tile
    public final int maxScreenCol = 25;
    public final int maxScreenRow = 20;
    public final int screenWidth = tileSize * maxScreenCol; // 800 width
    public final int screenHeight = tileSize * maxScreenRow; // 640 height

    public int FPS = 60;

    private boolean printMapTransitions = true;
    private Map beachMap, riverMap, houseSEMap, houseSWMap, houseNWMap, houseNEMap, patchMap, ruinsMap, westBeachMap, castleGateMap, eastBeachMap, deltaMap, lakeMap, easterBeachMap, towerMap;
    private ArrayList<Map> maps = new ArrayList<>();

    public volatile ArrayList<Sprite> sprites = new ArrayList<>();
    public static HashSet<Body> bodies = new HashSet<>();

    // Instantiate game objects
    private WeaponItem sword = new WeaponItem("Sword", 100, 10);
    private HealItem bread = new HealItem("Bread", 10, 20);
    private KeyItem topaz = new KeyItem("Topaz");
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

        doc = new Player("Doc", 480, 768, new ArrayList<Component>(), new ArrayList<Item>(), this, keyHandler, tileManager);
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
        for(Entity entity : beachMap.getEntities()) {
            Xendy.printfDebug("Loading initial entity %s with sprite %s and body %s%n", entity, entity.getComponent(Sprite.class), entity.getComponent(Body.class));
            entity.getComponent(Sprite.class).setShow(true);
            GamePanel.bodies.add(entity.getComponent(Body.class));
        }
        if (XENDY_DEBUG) {
            System.out.println("ACTIVE BODIES AFTER LOADING BEACHMAP:");
            GamePanel.bodies.forEach((e)->System.out.println(e.entity));
        }
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
        sprites.sort(Comparator.comparingInt(sprite -> sprite.isBg() ? Integer.MIN_VALUE : sprite.entity.getY() + sprite.getHEIGHT()*scale));
        for(Sprite sprite : sprites) {
            sprite.draw(g2);
        }

        g2.dispose();
    }

    public void checkMapExits(Player player) {

        // beach <-> river
        exitedMapVertically(doc, beachMap, riverMap, 0, 1600, -44, player.getX(), 1520);
        exitedMapVertically(doc, riverMap, beachMap, 0, 1600, 1528, player.getX(), -36);

        // enter/exit house SE
        exitedMapVertically(doc, riverMap, houseSEMap, 1084, 1108, 872, 376, 528);
        exitedMapVertically(doc, houseSEMap, riverMap, 340, 412, 560, 1096, 880);

        // enter/exit house SW
        exitedMapVertically(doc, riverMap, houseSWMap, 348, 372, 456, 376, 528);
        exitedMapVertically(doc, houseSWMap, riverMap, 340, 412, 560, 360, 464);

        // enter/exit house NW
        exitedMapVertically(doc, riverMap, houseNWMap, 540, 564, 200, 376, 528);
        exitedMapVertically(doc, houseNWMap, riverMap, 340, 412, 560, 552, 208);

        // enter/exit house NE
        exitedMapVertically(doc, riverMap, houseNEMap, 1404, 1428, 456, 376, 528);
        exitedMapVertically(doc, houseNEMap, riverMap, 340, 412, 560, 1416, 464);

        // river <-> patch
        exitedMapVertically(doc, riverMap, patchMap, 0, 1600, -44, player.getX(), 1520);
        exitedMapVertically(doc, patchMap, riverMap, 0, 1600, 1528, player.getX(), -36);

        // river <-> ruins
        exitedMapHorizontally(doc, riverMap, ruinsMap, 0, 1600, -12, 1556, player.getY());
        exitedMapHorizontally(doc, ruinsMap, riverMap, 0, 1600, 1564, -8, player.getY());

        // beach <-> west beach
        exitedMapHorizontally(doc, beachMap, westBeachMap, 0, 1600, -12, 1556, player.getY());
        exitedMapHorizontally(doc, westBeachMap, beachMap, 0, 1600, 1564, -8, player.getY());

        // ruins <-> west beach
        exitedMapVertically(doc, ruinsMap, westBeachMap, 0, 1600, 1528, player.getX(), -36);
        exitedMapVertically(doc, westBeachMap, ruinsMap, 0, 1600, -44, player.getX(), 1520);

        // patch <-> castle gate
        exitedMapHorizontally(doc, patchMap, castleGateMap, 0, 1600, -12, 1556, player.getY());
        exitedMapHorizontally(doc, castleGateMap, patchMap, 0, 1600, 1564, -8, player.getY());

        // ruins <-> castle gate
        exitedMapVertically(doc, ruinsMap, castleGateMap, 0, 1600, -44, player.getX(), 1520);
        exitedMapVertically(doc, castleGateMap, ruinsMap, 0, 1600, 1528, player.getX(), -36);

        // beach <-> east beach
        exitedMapHorizontally(doc, eastBeachMap, beachMap, 0, 1600, -12, 1556, player.getY());
        exitedMapHorizontally(doc, beachMap, eastBeachMap, 0, 1600, 1564, -8, player.getY());

        // east beach <-> delta
        exitedMapVertically(doc, eastBeachMap, deltaMap, 0, 1600, -44, player.getX(), 1520);
        exitedMapVertically(doc, deltaMap, eastBeachMap, 0, 1600, 1528, player.getX(), -36);

        // river <-> delta
        exitedMapHorizontally(doc, deltaMap, riverMap, 0, 1600, -12, 1556, player.getY());
        exitedMapHorizontally(doc, riverMap, deltaMap, 0, 1600, 1564, -8, player.getY());

        // patch <-> lake
        exitedMapHorizontally(doc, lakeMap, patchMap, 0, 1600, -12, 1556, player.getY());
        exitedMapHorizontally(doc, patchMap, lakeMap, 0, 1600, 1564, -8, player.getY());

        // delta <-> lake
        exitedMapVertically(doc, deltaMap, lakeMap, 0, 1600, -44, player.getX(), 1520);
        exitedMapVertically(doc, lakeMap, deltaMap, 0, 1600, 1528, player.getX(), -36);

        // east beach <-> easter beach
        exitedMapHorizontally(doc, easterBeachMap, eastBeachMap, 0, 1600, -12, 1556, player.getY());
        exitedMapHorizontally(doc, eastBeachMap, easterBeachMap, 0, 1600, 1564, -8, player.getY());

        // delta <-> tower
        exitedMapHorizontally(doc, towerMap, deltaMap, 0, 1600, -12, 1556, player.getY());
        exitedMapHorizontally(doc, deltaMap, towerMap, 0, 1600, 1564, -8, player.getY());

        // easter beach <-> tower
        exitedMapVertically(doc, easterBeachMap, towerMap, 0, 1600, -44, player.getX(), 1520);
        exitedMapVertically(doc, towerMap, easterBeachMap, 0, 1600, 1528, player.getX(), -36);
    }

    public Entity mapEntity(String kind, int x, int y) {
        mapLoadStateCounter++;
        switch (kind) {
            case "tree" -> {
                Entity tree = new Entity("tree", x, y, new ArrayList<>());
                Xendy.printDebug("Creating tree with entity " + tree + " with debug " + mapLoadStateDebugger + " at " + mapLoadStateCounter);
                Sprite treeSprite = new Sprite("sprites/tree.png", 32, 40, false, this);
                Body treeBody = new Body(new Rectangle(44, 100, 40, 60), this);
                tree.addComponent(treeSprite);
                tree.addComponent(treeBody);
                return tree;
            }
            case "house" -> {
                Entity house = new Entity("house", x, y, new ArrayList<>());
                Xendy.printDebug("Creating house with entity " + house + " with debug " + mapLoadStateDebugger + " at " + mapLoadStateCounter);
                Sprite houseSprite = new Sprite("sprites/house.png", 56, 56, false, this);
                Body houseBody = new Body(new Rectangle(32, 96, 160, 128), this);
                house.addComponent(houseSprite);
                house.addComponent(houseBody);
                return house;
            }
            case "well" -> {
                Entity well = new Entity("well", x, y, new ArrayList<>());
                Sprite wellSprite = new Sprite("sprites/well.png", 56, 56, false, this);
                Xendy.printDebug("Creating well with entity " + well + " with debug " + mapLoadStateDebugger + " at " + mapLoadStateCounter);
                Body wellBody = new Body(new Rectangle(32, 96, 160, 128), this);
                well.addComponent(wellSprite);
                well.addComponent(wellBody);
                return well;
            }
            case "tower top" -> {
                Entity towerTop = new Entity("tower top", x, y, new ArrayList<>());
                Sprite towerSpriteTop = new Sprite("sprites/tower_top.png", 52, 93, false, this);
                Xendy.printDebug("Creating tower top with entity " + towerTop + " with debug " + mapLoadStateDebugger + " at " + mapLoadStateCounter);
                Body towerBodyLeftDown = new Body(new Rectangle(0, 384, 84, 36), this);
                Body towerBodyRightDown = new Body(new Rectangle(124, 384, 84, 36), this);
                Body towerBodyLeftUp = new Body(new Rectangle(0, 332, 56, 48), this);
                Body towerBodyRightUp = new Body(new Rectangle(152, 332, 56, 48), this);
                Body towerBodyMiddle = new Body(new Rectangle(40, 296, 128, 76), this);
                towerTop.addComponent(towerSpriteTop);
                towerTop.addComponent(towerBodyLeftDown);
                towerTop.addComponent(towerBodyRightDown);
                towerTop.addComponent(towerBodyLeftUp);
                towerTop.addComponent(towerBodyRightUp);
                towerTop.addComponent(towerBodyMiddle);
                return towerTop;
            }
            case "tower bottom" -> {
                Entity towerBottom = new Entity("tower top", x, y, new ArrayList<>());
                Sprite towerSpriteBottom = new Sprite("sprites/tower_bottom.png", 52, 12, true, this);
                Xendy.printDebug("Creating tower top with entity " + towerBottom + " with debug " + mapLoadStateDebugger + " at " + mapLoadStateCounter);
                towerBottom.addComponent(towerSpriteBottom);
                return towerBottom;
            }
        }
        return new Entity();
    }

    private String mapLoadStateDebugger = "";
    private int mapLoadStateCounter = 0;
    public void setMaps() {
        mapLoadStateDebugger = "beach";
        mapLoadStateCounter = 0;
        this.beachMap = new Map("Beach", 50, 50, "maps/beach_map.txt", new ArrayList<>());
        this.beachMap.getEntities().add(mapEntity("tree", 788, 92));
        this.beachMap.getEntities().add(mapEntity("tree", 200, 188));
        this.maps.add(beachMap);

        mapLoadStateDebugger = "river";
        mapLoadStateCounter = 0;
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
        this.riverMap.getEntities().add(mapEntity("house", 1008, 720));
        this.riverMap.getEntities().add(mapEntity("house", 272, 304));
        this.riverMap.getEntities().add(mapEntity("house", 464, 48));
        this.riverMap.getEntities().add(mapEntity("house", 1328, 304));
        this.riverMap.getEntities().add(mapEntity("well", 784, 432));
        this.maps.add(riverMap);

        mapLoadStateDebugger = "houses";
        mapLoadStateCounter = 0;
        this.houseSEMap = new Map("House SE", 25, 20, "maps/houseSE_map.txt", new ArrayList<>());
        this.maps.add(riverMap);

        this.houseSWMap = new Map("House SW", 25, 20, "maps/houseSW_map.txt", new ArrayList<>());
        this.maps.add(houseSWMap);

        this.houseNWMap = new Map("House NW", 25, 20, "maps/houseNW_map.txt", new ArrayList<>());
        this.maps.add(houseNWMap);

        this.houseNEMap = new Map("House NE", 25, 20, "maps/houseNE_map.txt", new ArrayList<>());
        this.maps.add(houseNEMap);

        mapLoadStateDebugger = "patch";
        mapLoadStateCounter = 0;
        this.patchMap = new Map("Patch", 50, 50, "maps/patch_map.txt", new ArrayList<>());
        this.patchMap.getEntities().add(mapEntity("tree", 32, 1092));
        this.patchMap.getEntities().add(mapEntity("tree", 112, 700));
        this.patchMap.getEntities().add(mapEntity("tree", 316, 1124));
        this.patchMap.getEntities().add(mapEntity("tree", 400, 316));
        this.patchMap.getEntities().add(mapEntity("tree", 948, 540));
        this.patchMap.getEntities().add(mapEntity("tree", 1128, 868));
        this.maps.add(patchMap);

        mapLoadStateDebugger = "ruins";
        mapLoadStateCounter = 0;
        this.ruinsMap = new Map("Ruins", 50, 50, "maps/ruins_map.txt", new ArrayList<>());
        this.ruinsMap.getEntities().add(mapEntity("tree", 1336, 628));
        this.ruinsMap.getEntities().add(mapEntity("tree", 1112, 256));
        this.ruinsMap.getEntities().add(mapEntity("tree", 500, 168));
        this.ruinsMap.getEntities().add(mapEntity("tree", 180, 852));
        this.ruinsMap.getEntities().add(mapEntity("tree", 306, 548));
        this.ruinsMap.getEntities().add(mapEntity("tree", 884, 1016));
        this.ruinsMap.getEntities().add(mapEntity("tree", 1448, 1340));
        this.maps.add(ruinsMap);

        mapLoadStateDebugger = "west beach";
        mapLoadStateCounter = 0;
        this.westBeachMap = new Map("West Beach", 50, 50, "maps/west_beach_map.txt", new ArrayList<>());
        this.westBeachMap.getEntities().add(mapEntity("tree", 1144, 48));
        this.maps.add(westBeachMap);

        mapLoadStateDebugger = "castle gate";
        mapLoadStateCounter = 0;
        this.castleGateMap = new Map("Castle Gate", 50, 50, "maps/castle_gate_map.txt", new ArrayList<>());
        this.castleGateMap.getEntities().add(mapEntity("tree", 108, 1188));
        this.castleGateMap.getEntities().add(mapEntity("tree", 452, 952));
        this.castleGateMap.getEntities().add(mapEntity("tree", 820, 1366));
        this.castleGateMap.getEntities().add(mapEntity("tree", 1348, 1300));
        this.maps.add(castleGateMap);

        mapLoadStateDebugger = "east beach";
        mapLoadStateCounter = 0;
        this.eastBeachMap = new Map("East Beach", 50, 50, "maps/east_beach_map.txt", new ArrayList<>());
        this.eastBeachMap.getEntities().add(mapEntity("tree", 132, 176));
        this.eastBeachMap.getEntities().add(mapEntity("tree", 1296, 308));
        this.eastBeachMap.getEntities().add(mapEntity("tree", 448, 588));
        this.maps.add(eastBeachMap);

        mapLoadStateDebugger = "delta";
        mapLoadStateCounter = 0;
        this.deltaMap = new Map("Delta", 50, 50, "maps/delta_map.txt", new ArrayList<>());
        this.deltaMap.getEntities().add(mapEntity("tree", 788, 192));
        this.deltaMap.getEntities().add(mapEntity("tree", 1304, 596));
        this.deltaMap.getEntities().add(mapEntity("tree", 236, 684));
        this.deltaMap.getEntities().add(mapEntity("tree", 632, 1200));
        this.deltaMap.getEntities().add(mapEntity("tree", 1120, 1196));
        this.maps.add(deltaMap);

        mapLoadStateDebugger = "lake";
        mapLoadStateCounter = 0;
        this.lakeMap = new Map("Lake", 50, 50, "maps/lake_map.txt", new ArrayList<>());
        this.lakeMap.getEntities().add(mapEntity("tree", 516, 1132));
        this.lakeMap.getEntities().add(mapEntity("tree", 1244, 128));
        this.lakeMap.getEntities().add(mapEntity("tree", 748, 236));
        this.lakeMap.getEntities().add(mapEntity("tree", 144, 136));
        this.lakeMap.getEntities().add(mapEntity("tree", 116, 988));
        this.maps.add(lakeMap);

        mapLoadStateDebugger = "easter beach";
        mapLoadStateCounter = 0;
        this.easterBeachMap = new Map("Easter Beach", 50, 50, "maps/easter_beach_map.txt", new ArrayList<>());
        this.lakeMap.getEntities().add(mapEntity("tree", 984, 120));
        this.maps.add(easterBeachMap);

        mapLoadStateDebugger = "tower";
        mapLoadStateCounter = 0;
        this.towerMap = new Map("Tower", 50, 50, "maps/tower_map.txt", new ArrayList<>());
        this.towerMap.getEntities().add(mapEntity("tree", 1304, 88));
        this.towerMap.getEntities().add(mapEntity("tree", 216, 496));
        this.towerMap.getEntities().add(mapEntity("tree", 1096, 668));
        this.towerMap.getEntities().add(mapEntity("tree", 392, 1316));
        this.towerMap.getEntities().add(mapEntity("tower top", 648, 588));
        this.towerMap.getEntities().add(mapEntity("tower bottom", 648, 960));
        this.maps.add(towerMap);

        for(Map map : maps) {
            for(Entity entity : map.getEntities()) {
                if(entity.getComponent(Sprite.class) != null) {
                    entity.getComponent(Sprite.class).setShow(false);
                }
                Xendy.printDebug("Initially removing " + entity + " with body " + entity.getComponent(Body.class));
                for(Component component : entity.getComponents()) {
                    GamePanel.bodies.remove(component);
                }
            }
        }
    }

    public void exitedMapVertically(Player player, Map mapFrom, Map mapTo, int x1, int x2, int previousY, int nextX, int nextY) {
        if(tileManager.getCurrentMap().equals(mapFrom) && player.getX() >= x1 && player.getX() <= x2 && player.getY() == previousY) {
            if(printMapTransitions) System.out.println(mapFrom.getName() + " -> " + mapTo.getName());
            tileManager.loadMap(mapTo);
            player.setX(nextX);
            player.setY(nextY);
        }
    }

    public void exitedMapHorizontally(Player player, Map mapFrom, Map mapTo, int y1, int y2, int previousX, int nextX, int nextY) {
        if(tileManager.getCurrentMap().equals(mapFrom) && player.getY() >= y1 && player.getY() <= y2 && player.getX() == previousX) {
            if(printMapTransitions) System.out.println(mapFrom.getName() + " -> " + mapTo.getName());
            tileManager.loadMap(mapTo);
            player.setX(nextX);
            player.setY(nextY);
        }
    }
}
