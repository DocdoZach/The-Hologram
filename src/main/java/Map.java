import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
    private String name;
    private int maxCol, maxRow;
    private String filePath;
    private ArrayList<Entity> entities;

    public Map(String name, int maxCol, int maxRow, String filePath, ArrayList<Entity> entities) {
        this.name = name;
        this.maxCol = maxCol;
        this.maxRow = maxRow;
        this.filePath = filePath;
        this.entities = entities;
    }

    public void toggleMap(Map newMap) {
        for(Entity entity : this.entities) {
            if(entity.getComponent(Sprite.class) != null) {
                entity.getComponent(Sprite.class).show = false;
            }
            GamePanel.bodies.remove(entity.getComponent(Body.class));
        }
        for(Entity entity : newMap.entities) {
            entity.getComponent(Sprite.class).show = true;
            GamePanel.bodies.add(entity.getComponent(Body.class));
        }
    }
    public String getName() {
        return name;
    }
    public void setName() {
        this.name = name;
    }
    public int getMaxCol() {
        return maxCol;
    }
    public void setMaxCol(int maxCol) {
        this.maxCol = maxCol;
    }
    public int getMaxRow() {
        return maxRow;
    }
    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public ArrayList<Entity> getEntities() {
        return entities;
    }
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }
}
