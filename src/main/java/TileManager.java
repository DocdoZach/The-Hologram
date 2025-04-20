import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gamePanel;
    TileType[] tile;
    int[][] mapTile;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile = new TileType[20];
        mapTile = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
        loadTileImage();
        loadMap("maps/river_map.txt");
    }

    public void loadTileImage() {
        try {
            tile[0] = null;

            tile[1] = new TileType("grass", null, false);
            tile[1].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/grass_tile.png")));

            tile[2] = new TileType("dirt", null, false);
            tile[2].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/dirt_tile.png")));

            tile[3] = new TileType("water", null, true);
            tile[3].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/water_tile.png")));

            tile[4] = new TileType("wood", null, false);
            tile[4].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/wood_tile.png")));

            tile[5] = new TileType("brick", null, true);
            tile[5].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/brick_tile.png")));

            tile[6] = new TileType("dark_grass", null, false);
            tile[6].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/dark_grass_tile.png")));

            tile[7] = new TileType("black", null, true);
            tile[7].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/black_tile.png")));

            tile[8] = new TileType("sand", null, false);
            tile[8].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/sand_tile.png")));

            tile[9] = new TileType("planks", null, false);
            tile[9].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/planks_tile.png")));

            tile[10] = new TileType("stone", null, true);
            tile[10].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/stone_tile.png")));

            tile[11] = new TileType("stone_brick", null, true);
            tile[11].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/stone_brick_tile.png")));

            tile[12] = new TileType("red_brick", null, true);
            tile[12].setTileImage(ImageIO.read(getClass().getResourceAsStream("tiles/red_brick_tile.png")));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
            int tileNum = mapTile[worldCol][worldRow];
            int x = worldCol * gamePanel.getTileSize();
            int y = worldRow * gamePanel.getTileSize();

            int screenX = x - gamePanel.doc.getX() + gamePanel.doc.cameraX;
            int screenY = y - gamePanel.doc.getY() + gamePanel.doc.cameraY;

            if(x + gamePanel.getTileSize() > gamePanel.doc.getX() - gamePanel.doc.cameraX && x - gamePanel.getTileSize() * 2 < gamePanel.doc.getX() + gamePanel.doc.cameraX &&
               y + gamePanel.getTileSize() > gamePanel.doc.getY() - gamePanel.doc.cameraY && y - gamePanel.getTileSize() * 3 < gamePanel.doc.getY() + gamePanel.doc.cameraY) {
                g2.drawImage(tile[tileNum].getTileImage(), screenX, screenY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            }

            worldCol++;

            if(worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void loadMap(String map) {
        try {
            int col = 0;
            int row = 0;

            InputStream inputStream = getClass().getResourceAsStream(map);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while(col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
                String line = bufferedReader.readLine();

                while(col < gamePanel.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTile[col][row] = num;
                    col++;
                }
                if(col == gamePanel.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            gamePanel.currentMap = map;
        } catch(Exception e) {

        }
    }
}
