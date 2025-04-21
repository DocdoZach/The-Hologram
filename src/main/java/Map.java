import java.util.ArrayList;

public class Map {
    private int maxCol, maxRow;
    private String filePath;
    private ArrayList<Entity> entities;

    public Map(int maxCol, int maxRow, String filePath, ArrayList<Entity> entities) {
        this.maxCol = maxCol;
        this.maxRow = maxRow;
        this.filePath = filePath;
        this.entities = entities;
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
}
