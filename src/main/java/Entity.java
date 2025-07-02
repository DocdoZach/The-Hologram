import java.lang.reflect.Array;
import java.util.ArrayList;

public class Entity {
    private String name;
    private int x;
    private int y;
    private ArrayList<Component> components = new ArrayList<>();

    public Entity() {
        this.name = "None";
        this.x = 0;
        this.y = 0;
    }

    public Entity(ArrayList<Component> components) {
        this.name = "None";
        this.x = 0;
        this.y = 0;
        this.components = components;
    }

    public Entity(int x, int y, ArrayList<Component> components) {
        this.name = "None";
        this.x = x;
        this.y = y;
        this.components = components;
    }

    public Entity(String name, int x, int y, ArrayList<Component> components) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.components = components;
    }

    public Entity(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Entity(String name, ArrayList<Component> components) {
        this.name = name;
        this.x = 0;
        this.y = 0;
        this.components = components;
    }

    public Entity(String name) {
        this.name = name;
        this.x = 0;
        this.y = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveX(int distance) {
        x += distance;
    }

    public void moveY(int distance) {
        y += distance;
    }

    public void addComponent(Component component) {
        components.add(component);
        component.entity = this;
    }

    public <T extends Component> void removeComponent(Class<T> kind) {
        Component c = getComponent(kind);
        if(c != null) {
            c.entity = null;
            components.remove(c);
        }
    }

    public <T extends Component> boolean hasComponent(Class<T> kind) {
        for(Component component : components) {
            if(kind.isInstance(component)) {
                return true;
            }
        }
        return false;
    }

    public <T extends Component> T getComponent(Class<T> kind) {
        for(Component component : components) {
            if(kind.isInstance(component)) {
                return (T) component;
            }
        }
        return null;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public <T extends Component> ArrayList<T> getComponents(Class<T> kind) {
        ArrayList<Component> componentsOut = (ArrayList<Component>) components.clone();
        componentsOut.removeIf(component -> !kind.isInstance(component));
        return (ArrayList<T>) componentsOut;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", super.toString(), name);
    }
}
