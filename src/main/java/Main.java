package main.java;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        WeaponItem sword = new WeaponItem("Sword", 100, 10);
        HealItem bread = new HealItem("Bread", 10, 20);
        KeyItem topaz = new KeyItem("Topaz");
        Player doc = new Player("Doc", 0, 0, new ArrayList<Component>(), new ArrayList<Item>());
        Character docCharacter = new Character(1, 15, 1, 1, 1);
        doc.addComponent(docCharacter);

        System.out.println(doc.getComponent(Character.class).getMaxHp());

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("THE HOLOGR△M");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}