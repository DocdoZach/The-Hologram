import java.awt.*;

public class Body extends Component {private Rectangle hitbox;
    private GamePanel gamePanel;
    private boolean collision;

    public Body(Rectangle hitbox, GamePanel gamePanel) {
        this.hitbox = hitbox;
        this.gamePanel = gamePanel;
        gamePanel.bodies.add(this);
    }

    public boolean isPositionValid() {
        int x = this.entity.x + this.hitbox.x;
        int y = this.entity.y + this.hitbox.y;
        if(gamePanel.tileManager.isTileSolid(x, y, this.hitbox.width - 4, this.hitbox.height - 4)) return false;

        for(Body body : gamePanel.bodies) {
            if(body != this && body.isCollidingWith(this)) return false;
        }
        return true;
    }

    public boolean isCollidingWith(Body other) {
        if(this.entity == null) {
            return false;
        }

        int x = this.entity.x + this.hitbox.x;
        int y = this.entity.y + this.hitbox.y;
        int otherX = other.entity.x + other.hitbox.x;
        int otherY = other.entity.y + other.hitbox.y;

        return x < otherX + other.hitbox.width && otherX < x + this.hitbox.width && y < otherY + other.hitbox.height && otherY < y + this.hitbox.height;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public boolean getCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
