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
    Map currentMap;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile = new TileType[20];
        loadTileImage();
    }

    public boolean isPointSolid(int x, int y) {
        int col = (x / 32);
        int row = (y / 32);

        if(x < 0 || y < 28 || col >= currentMap.getMaxCol() || row >= currentMap.getMaxRow() + 36) {
            return true;
        }

        if(y >= 1556) return false;
        return this.tile[mapTile[col][row]].isHasCollision();
    }

    public boolean isTileSolid(int x, int y, int width, int height) {
        int xChecks = (int)Math.ceil((double) width / gamePanel.tileSize);
        int yChecks = (int)Math.ceil((double) height / gamePanel.tileSize);
        for(int iy = 0; iy < yChecks; iy++) {
            for(int ix = 0; ix < xChecks; ix++) {
                int _x = ix * gamePanel.tileSize + x;
                int _y = iy * gamePanel.tileSize + y;
                if(isPointSolid(_x, _y)) return true;
            }
        }
        return isPointSolid(x + width, y) || isPointSolid(x, y + height) || isPointSolid(x + width, y + height);
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

        while(worldCol < mapTile[0].length && worldRow < mapTile.length) {
            int tileNum = mapTile[worldCol][worldRow];
            int x = worldCol * gamePanel.tileSize;
            int y = worldRow * gamePanel.tileSize;

            int screenX = x - gamePanel.doc.getX() + gamePanel.doc.cameraX;
            int screenY = y - gamePanel.doc.getY() + gamePanel.doc.cameraY;

            if(x + gamePanel.tileSize > gamePanel.doc.getX() - gamePanel.doc.cameraX && x - gamePanel.tileSize * 2 < gamePanel.doc.getX() + gamePanel.doc.cameraX &&
               y + gamePanel.tileSize > gamePanel.doc.getY() - gamePanel.doc.cameraY && y - gamePanel.tileSize * 3 < gamePanel.doc.getY() + gamePanel.doc.cameraY) {
                g2.drawImage(tile[tileNum].getTileImage(), screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            }

            worldCol++;

            if(worldCol == mapTile[0].length) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void loadMap(Map map) {
        try {
            int col = 0;
            int row = 0;
            mapTile = new int[map.getMaxCol()][map.getMaxRow()];

            InputStream inputStream = getClass().getResourceAsStream(map.getFilePath());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while(col < map.getMaxCol() && row < map.getMaxRow()) {
                String line = bufferedReader.readLine();

                while(col < map.getMaxCol()) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTile[col][row] = num;
                    col++;
                }
                if(col == map.getMaxCol()) {
                    col = 0;
                    row++;
                }
            }
            currentMap = map;
        } catch(Exception e) {

        }
    }
}
