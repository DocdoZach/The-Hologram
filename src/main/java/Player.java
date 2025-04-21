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
    private int moveSpeed;
    private ArrayList<Item> inventory = new ArrayList<>();
    public int cameraX, cameraY;

    public Player(String name, int x, int y, ArrayList<Component> components, ArrayList<Item> inventory, GamePanel gamePanel, KeyHandler keyHandler) {
        super(name, x, y, components);
        this.inventory = inventory;
        this.moveSpeed = 4;
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.cameraX = gamePanel.screenWidth / 2 - 24;
        this.cameraY = gamePanel.screenHeight / 2 - 40;
        loadPlayerImage();
    }

    public void update() {
        int previousX = this.x;
        int previousY = this.y;

        if(keyHandler.ctrlPressed) moveSpeed = 8;
        else moveSpeed = 4;

        if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            if(keyHandler.upPressed) {
                getComponent(Sprite.class).direction = "up";
                this.y -= moveSpeed;
            }
            if(keyHandler.downPressed) {
                getComponent(Sprite.class).direction = "down";
                this.y += moveSpeed;
            }
            if(!this.getComponent(Body.class).isPositionValid()) this.y = previousY;

            if(keyHandler.leftPressed) {
                getComponent(Sprite.class).direction = "left";
                this.x -= moveSpeed;
            }
            if(keyHandler.rightPressed) {
                getComponent(Sprite.class).direction = "right";
                this.x += moveSpeed;
            }
            if(!this.getComponent(Body.class).isPositionValid()) this.x = previousX;

            if(keyHandler.upPressed && keyHandler.leftPressed && keyHandler.rightPressed) {
                getComponent(Sprite.class).direction = "up";
            }
            if(keyHandler.downPressed && keyHandler.leftPressed && keyHandler.rightPressed) {
                getComponent(Sprite.class).direction = "down";
            }

            this.getComponent(Body.class).setCollision(false);

            getComponent(Sprite.class).counter++;
        }
        if(moveSpeed == 4) {
            if (getComponent(Sprite.class).counter > 10) {
                if (getComponent(Sprite.class).num == 1) getComponent(Sprite.class).num = 2;
                else if (getComponent(Sprite.class).num == 2) getComponent(Sprite.class).num = 3;
                else if (getComponent(Sprite.class).num == 3) getComponent(Sprite.class).num = 4;
                else if (getComponent(Sprite.class).num == 4) getComponent(Sprite.class).num = 1;
                getComponent(Sprite.class).counter = 0;
            }
        } else if(moveSpeed == 8) {
            if (getComponent(Sprite.class).counter > 5) {
                if (getComponent(Sprite.class).num == 1) getComponent(Sprite.class).num = 2;
                else if (getComponent(Sprite.class).num == 2) getComponent(Sprite.class).num = 3;
                else if (getComponent(Sprite.class).num == 3) getComponent(Sprite.class).num = 4;
                else if (getComponent(Sprite.class).num == 4) getComponent(Sprite.class).num = 1;
                getComponent(Sprite.class).counter = 0;
            }
        }
        if(keyHandler.ePressed) System.out.println("x, y: " + x + ", " + y);
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch(getComponent(Sprite.class).direction) {
            case "up":
                if(getComponent(Sprite.class).num == 1) image = getComponent(Sprite.class).images.get(0);
                if(getComponent(Sprite.class).num == 2) image = getComponent(Sprite.class).images.get(1);
                if(getComponent(Sprite.class).num == 3) image = getComponent(Sprite.class).images.get(0);
                if(getComponent(Sprite.class).num == 4) image = getComponent(Sprite.class).images.get(2);
                break;
            case "down":
                if(getComponent(Sprite.class).num == 1) image = getComponent(Sprite.class).images.get(3);
                if(getComponent(Sprite.class).num == 2) image = getComponent(Sprite.class).images.get(4);
                if(getComponent(Sprite.class).num == 3) image = getComponent(Sprite.class).images.get(3);
                if(getComponent(Sprite.class).num == 4) image = getComponent(Sprite.class).images.get(5);
                break;
            case "left":
                if(getComponent(Sprite.class).num == 1) image = getComponent(Sprite.class).images.get(6);
                if(getComponent(Sprite.class).num == 2) image = getComponent(Sprite.class).images.get(7);
                if(getComponent(Sprite.class).num == 3) image = getComponent(Sprite.class).images.get(6);
                if(getComponent(Sprite.class).num == 4) image = getComponent(Sprite.class).images.get(7);
                break;
            case "right":
                if(getComponent(Sprite.class).num == 1) image = getComponent(Sprite.class).images.get(8);
                if(getComponent(Sprite.class).num == 2) image = getComponent(Sprite.class).images.get(9);
                if(getComponent(Sprite.class).num == 3) image = getComponent(Sprite.class).images.get(8);
                if(getComponent(Sprite.class).num == 4) image = getComponent(Sprite.class).images.get(9);
                break;
        }

        g2.drawImage(image, cameraX, cameraY, this.getComponent(Sprite.class).width * gamePanel.scale, this.getComponent(Sprite.class).height * gamePanel.scale, null);
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

    public void loadPlayerImage() {
        try {
            Sprite sprite = getComponent(Sprite.class);
            sprite.images.set(0, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_back.png")));
            sprite.images.set(1, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking_back-1.png")));
            sprite.images.set(2, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking_back-2.png")));
            sprite.images.set(3, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc.png")));
            sprite.images.set(4, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking-1.png")));
            sprite.images.set(5, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking-2.png")));
            sprite.images.set(6, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_left.png")));
            sprite.images.set(7, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking_left.png")));
            sprite.images.set(8, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_right.png")));
            sprite.images.set(9, ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking_right.png")));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
