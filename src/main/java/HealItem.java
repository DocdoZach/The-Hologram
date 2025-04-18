public class HealItem extends Item {
    private int hp;

    public HealItem(String name, int value, int hp) {
        super(name, value);
        this.hp = hp;
    }
}
