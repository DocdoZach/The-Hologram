import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class Player extends Entity {
    GamePanel gamePanel;
    KeyHandler keyHandler;
    TileManager tileManager;
    private int moveSpeed;
    private ArrayList<Item> inventory = new ArrayList<>();
    public int cameraX, cameraY;

    public Player(String name, int x, int y, ArrayList<Component> components, ArrayList<Item> inventory, GamePanel gamePanel, KeyHandler keyHandler, TileManager tileManager) {
        super(name, x, y, components);
        this.inventory = inventory;
        this.moveSpeed = 4;
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.tileManager = tileManager;
        this.cameraX = gamePanel.screenWidth / 2 - 24;
        this.cameraY = gamePanel.screenHeight / 2 - 40;
    }

    public void update() {
        int previousX = this.x;
        int previousY = this.y;

        if(keyHandler.ctrlPressed) moveSpeed = 8;
        else moveSpeed = 4;

        if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            if(keyHandler.upPressed) {
                getComponent(MultiSprite.class).useSprite("up");
                this.y -= moveSpeed;
            }
            if(keyHandler.downPressed) {
                getComponent(MultiSprite.class).useSprite("down");
                this.y += moveSpeed;
            }
            if(!this.getComponent(Body.class).isPositionValid()) this.y = previousY;

            if(keyHandler.leftPressed) {
                getComponent(MultiSprite.class).useSprite("left");
                this.x -= moveSpeed;
            }
            if(keyHandler.rightPressed) {
                getComponent(MultiSprite.class).useSprite("right");
                this.x += moveSpeed;
            }
            if(!this.getComponent(Body.class).isPositionValid()) this.x = previousX;

            if(keyHandler.upPressed && keyHandler.leftPressed && keyHandler.rightPressed) {
                getComponent(MultiSprite.class).useSprite("up");
            }
            if(keyHandler.downPressed && keyHandler.leftPressed && keyHandler.rightPressed) {
                getComponent(MultiSprite.class).useSprite("down");
            }

            this.getComponent(Body.class).setCollision(false);

            getComponent(MultiSprite.class).currentSprite.counter++;
        }

        if(moveSpeed == 4) {
            getComponent(MultiSprite.class).currentSprite.counterMax = 10;
        } else if(moveSpeed == 8) {
            getComponent(MultiSprite.class).currentSprite.counterMax = 5;
        }

        if(keyHandler.ePressed) {
            System.out.println("x, y: " + x + ", " + y + "\ncamera x, y: " + cameraX + ", " + cameraY);
        }

        int targetCameraX = gamePanel.screenWidth / 2 - 24;
        int targetCameraY = gamePanel.screenHeight / 2 - 40;

        if(this.x < gamePanel.screenWidth / 2 - 24) {
            targetCameraX = this.x;
        }
        else if(this.x > gamePanel.tileManager.currentMap.getMaxCol() * gamePanel.tileSize - gamePanel.screenWidth / 2 - 24) {
            targetCameraX = this.x - gamePanel.tileManager.currentMap.getMaxCol() * gamePanel.tileSize + gamePanel.screenWidth;
        }
        if(this.y < gamePanel.screenHeight / 2 - 40) {
            targetCameraY = this.y;
        }
        else if(this.y > gamePanel.tileManager.currentMap.getMaxRow() * gamePanel.tileSize - gamePanel.screenHeight / 2 - 40) {
            targetCameraY = this.y - gamePanel.tileManager.currentMap.getMaxRow() * gamePanel.tileSize + gamePanel.screenHeight;
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
}
