import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {
    GamePanel gamePanel;
    TileType[] tile;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile = new TileType[10];
        loadTileImage();
    }

    public void loadTileImage() {
        try {
            tile[0] = new TileType("grass", null, false);
            tile[0].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/grass_tile.png")));

            tile[1] = new TileType("dirt", null, false);
            tile[1].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/dirt_tile.png")));

            tile[2] = new TileType("water", null, true);
            tile[2].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/water_tile.png")));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        for(int tileX = 0; tileX < gamePanel.screenWidth; tileX += 32) {
            for(int tileY = 0; tileY < gamePanel.screenHeight; tileY += 32) {
                g2.drawImage(tile[0].getTileImage(), tileX, tileY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            }
        }
    }
}
