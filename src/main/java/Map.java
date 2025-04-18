import java.util.ArrayList;

public class Map {
    private ArrayList<TileType> tileTypes;
    private int tileSize;
    private String mapFile;
    private ArrayList<Entity> entities;

    public Map(ArrayList<TileType> tileTypes, int tileSize, String mapFile, ArrayList<Entity> entities) {
        this.tileTypes = tileTypes;
        this.tileSize = tileSize;
        this.mapFile = mapFile;
        this.entities = entities;
    }
}
