import java.util.HashMap;

public class MultiSprite extends Component {
    private HashMap<String, Sprite> sprites;
    private Sprite currentSprite;

    public MultiSprite(Entity entity) {
        this.sprites = new HashMap<>();
        this.entity = entity;
    }

    public void useSprite(String key) {
        for(Sprite s : sprites.values()) {
            s.setShow(false);
        }
        sprites.get(key).setShow(true);
        this.currentSprite = sprites.get(key);
    }

    public void addSprite(String key, Sprite sprite) {
        sprite.entity = this.entity;
        sprites.put(key, sprite);
    }

    public HashMap<String, Sprite> getSprites() {
        return this.sprites;
    }

    public Sprite getCurrentSprite() {
        return currentSprite;
    }

    public boolean checkCurrentSprite(String key) {
        return this.entity.getComponent(MultiSprite.class).getCurrentSprite().equals(this.entity.getComponent(MultiSprite.class).getSprites().get(key));
    }
}
