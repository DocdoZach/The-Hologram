import java.util.ArrayList;

public class Player extends Entity {
    private GamePanel gamePanel;
    private KeyHandler keyHandler;
    private int moveSpeed;
    private ArrayList<Item> inventory = new ArrayList<>();
    private int cameraX, cameraY;

    public Player(String name, int x, int y, ArrayList<Component> components, ArrayList<Item> inventory, GamePanel gamePanel, KeyHandler keyHandler, TileManager tileManager) {
        super(name, x, y, components);
        this.inventory = inventory;
        this.moveSpeed = 4;
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.cameraX = gamePanel.screenWidth / 2 - 24;
        this.cameraY = gamePanel.screenHeight / 2 - 40;
    }

    public void update() {
        int previousX = this.getX();
        int previousY = this.getY();

        if(keyHandler.getKeyPressed(Key.RUN)) moveSpeed = 8;
        else moveSpeed = 4;

        if(keyHandler.getKeyPressed(Key.UP) || keyHandler.getKeyPressed(Key.DOWN) || keyHandler.getKeyPressed(Key.LEFT) || keyHandler.getKeyPressed(Key.RIGHT)) {
            if(keyHandler.getKeyPressed(Key.UP)) {
                getComponent(MultiSprite.class).useSprite("up");
                moveY(-moveSpeed);
            }
            if(keyHandler.getKeyPressed(Key.DOWN)) {
                getComponent(MultiSprite.class).useSprite("down");
                moveY(moveSpeed);
            }
            if(!this.getComponent(Body.class).isPositionValid()) setY(previousY);

            if(keyHandler.getKeyPressed(Key.LEFT)) {
                getComponent(MultiSprite.class).useSprite("left");
                moveX(-moveSpeed);
            }
            if(keyHandler.getKeyPressed(Key.RIGHT)) {
                getComponent(MultiSprite.class).useSprite("right");
                moveX(moveSpeed);
            }
            if(!this.getComponent(Body.class).isPositionValid()) setX(previousX);

            if(keyHandler.getKeyPressed(Key.UP) && keyHandler.getKeyPressed(Key.LEFT) && keyHandler.getKeyPressed(Key.RIGHT)) {
                getComponent(MultiSprite.class).useSprite("up");
            }
            if(keyHandler.getKeyPressed(Key.DOWN) && keyHandler.getKeyPressed(Key.LEFT) && keyHandler.getKeyPressed(Key.RIGHT)) {
                getComponent(MultiSprite.class).useSprite("down");
            }

            this.getComponent(Body.class).setCollision(false);

            getComponent(MultiSprite.class).getCurrentSprite().countUp();
        }

        if(moveSpeed == 4) {
            getComponent(MultiSprite.class).getCurrentSprite().setCounterMax(10);
        } else if(moveSpeed == 8) {
            getComponent(MultiSprite.class).getCurrentSprite().setCounterMax(5);
        }

        if(keyHandler.getKeyPressed(Key.COORDS)) {
            System.out.println("x, y: " + getX() + ", " + getY() + "\ncamera x, y: " + cameraX + ", " + cameraY);
        }

        int targetCameraX = gamePanel.screenWidth / 2 - 24;
        int targetCameraY = gamePanel.screenHeight / 2 - 40;

        if(this.getX() < gamePanel.screenWidth / 2 - 24) {
            targetCameraX = this.getX();
        }
        else if(this.getX() > gamePanel.tileManager.getCurrentMap().getMaxCol() * gamePanel.tileSize - gamePanel.screenWidth / 2 - 24) {
            targetCameraX = this.getX() - gamePanel.tileManager.getCurrentMap().getMaxCol() * gamePanel.tileSize + gamePanel.screenWidth;
        }
        if(this.getY() < gamePanel.screenHeight / 2 - 40) {
            targetCameraY = this.getY();
        }
        else if(this.getY() > gamePanel.tileManager.getCurrentMap().getMaxRow() * gamePanel.tileSize - gamePanel.screenHeight / 2 - 40) {
            targetCameraY = this.getY() - gamePanel.tileManager.getCurrentMap().getMaxRow() * gamePanel.tileSize + gamePanel.screenHeight;
        }

        cameraX = targetCameraX;
        cameraY = targetCameraY;
    }

    public Item getItem(int index) {
        return inventory.get(index);
    }

    public void addItem(Item item) {
        this.inventory.add(item);
    }

    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    public boolean hasItem(Item item1) {
        for(Item item2 : inventory) {
            if(item2 == item1) return true;
        }
        return false;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }
}
