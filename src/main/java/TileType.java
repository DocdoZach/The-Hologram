import java.awt.image.BufferedImage;

public class TileType {
    private String name;
    private BufferedImage tileImage;
    private boolean hasCollision = false;

    public TileType(String name, BufferedImage tileImage, Boolean hasCollision) {
        this.name = name;
        this.tileImage = tileImage;
        this.hasCollision = hasCollision;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BufferedImage getTileImage() {
        return tileImage;
    }
    public void setTileImage(BufferedImage tileImage) {
        this.tileImage = tileImage;
    }
    public boolean isHasCollision() {
        return hasCollision;
    }
    public void setHasCollision(boolean hasCollision) {
        this.hasCollision = hasCollision;
    }
}
