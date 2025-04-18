public class WeaponItem extends Item {
    private int atk;

    public WeaponItem(String name, int value, int atk) {
        super(name, value);
        this.atk = atk;
    }
}
