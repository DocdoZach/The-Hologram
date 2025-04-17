package main.java;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Instantiate game objects
        WeaponItem sword = new WeaponItem("Sword", 100, 10);
        HealItem bread = new HealItem("Bread", 10, 20);
        KeyItem topaz = new KeyItem("Topaz");

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