public abstract class Item {
    private String name;
    private int value;

    Item(String name, int value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return name + " has a value of " + value;
    }

    public boolean startsWithVowel() {
        return this.name.charAt(0) == 'a'
                || this.name.charAt(0) == 'e'
                || this.name.charAt(0) == 'i'
                || this.name.charAt(0) == 'o'
                || this.name.charAt(0) == 'u';
    }
}
