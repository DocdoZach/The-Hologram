package main.java;

import java.util.ArrayList;

public class Player extends Entity {
    private ArrayList<Item> inventory = new ArrayList<>();

    public Player(String name, int x, int y, ArrayList<Component> components, ArrayList<Item> inventory) {
        super(name, x, y, components);
        this.inventory = inventory;
    }

    public Item getItem(int index) {
        return inventory.get(index);
    }

    public void addItem(Item item) {
        this.inventory.add(item);
    }

    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    public boolean hasItem(Item item1) {
        for(Item item2 : inventory) {
            if(item2 == item1) return true;
        }
        return false;
    }
}
