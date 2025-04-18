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

    public Player(String name, int x, int y, ArrayList<Component> components, ArrayList<Item> inventory, GamePanel gamePanel, KeyHandler keyHandler) {
        super(name, x, y, components);
        this.inventory = inventory;
        this.moveSpeed = 4;
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        getPlayerImage();
    }

    public void update() {
        if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            if(keyHandler.upPressed) {
                getComponent(Sprite.class).direction = "up";
                this.y -= moveSpeed;
            }
            if(keyHandler.downPressed) {
                getComponent(Sprite.class).direction = "down";
                this.y += moveSpeed;
            }
            if(keyHandler.leftPressed) {
                getComponent(Sprite.class).direction = "left";
                this.x -= moveSpeed;
            }
            if(keyHandler.rightPressed) {
                getComponent(Sprite.class).direction = "right";
                this.x += moveSpeed;
            }
            getComponent(Sprite.class).counter++;
        }

        if(getComponent(Sprite.class).counter > 12) {
            if(getComponent(Sprite.class).num == 1) getComponent(Sprite.class).num = 2;
            else if(getComponent(Sprite.class).num == 2) getComponent(Sprite.class).num = 3;
            else if(getComponent(Sprite.class).num == 3) getComponent(Sprite.class).num = 4;
            else if(getComponent(Sprite.class).num == 4) getComponent(Sprite.class).num = 1;
            getComponent(Sprite.class).counter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch(getComponent(Sprite.class).direction) {
            case "up":
                if(getComponent(Sprite.class).num == 1) image = getComponent(Sprite.class).up;
                if(getComponent(Sprite.class).num == 2) image = getComponent(Sprite.class).up1;
                if(getComponent(Sprite.class).num == 3) image = getComponent(Sprite.class).up;
                if(getComponent(Sprite.class).num == 4) image = getComponent(Sprite.class).up2;
                break;
            case "down":
                if(getComponent(Sprite.class).num == 1) image = getComponent(Sprite.class).down;
                if(getComponent(Sprite.class).num == 2) image = getComponent(Sprite.class).down1;
                if(getComponent(Sprite.class).num == 3) image = getComponent(Sprite.class).down;
                if(getComponent(Sprite.class).num == 4) image = getComponent(Sprite.class).down2;
                break;
            case "left":
                if(getComponent(Sprite.class).num == 1) image = getComponent(Sprite.class).left;
                if(getComponent(Sprite.class).num == 2) image = getComponent(Sprite.class).left1;
                if(getComponent(Sprite.class).num == 3) image = getComponent(Sprite.class).left;
                if(getComponent(Sprite.class).num == 4) image = getComponent(Sprite.class).left1;
                break;
            case "right":
                if(getComponent(Sprite.class).num == 1) image = getComponent(Sprite.class).right;
                if(getComponent(Sprite.class).num == 2) image = getComponent(Sprite.class).right1;
                if(getComponent(Sprite.class).num == 3) image = getComponent(Sprite.class).right;
                if(getComponent(Sprite.class).num == 4) image = getComponent(Sprite.class).right1;
                break;
        }

        g2.drawImage(image, x, y, 48, 80, null);
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

    public void getPlayerImage() {
        try {
            Sprite sprite = getComponent(Sprite.class);
            sprite.up = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_back.png"));
            sprite.up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking_back-1.png"));
            sprite.up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking_back-2.png"));
            sprite.down = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc.png"));
            sprite.down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking-1.png"));
            sprite.down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking-2.png"));
            sprite.left = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_left.png"));
            sprite.left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking_left.png"));
            sprite.right = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_right.png"));
            sprite.right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/doc_walking_right.png"));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
