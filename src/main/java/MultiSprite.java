import java.util.HashMap;

public class MultiSprite extends Component {
    public HashMap<String, Sprite> sprites;
    public Sprite currentSprite;

    public MultiSprite(Entity entity) {
        this.sprites = new HashMap<>();
        this.entity = entity;
    }

    public void useSprite(String key) {
        for(Sprite s : sprites.values()) {
            s.show = false;
        }
        sprites.get(key).show = true;
        this.currentSprite = sprites.get(key);
    }

    public void addSprite(String key, Sprite sprite) {
        sprite.entity = this.entity;
        sprites.put(key, sprite);
    }
}
