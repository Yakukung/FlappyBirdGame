package project_game_oop;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flappy Bird");
            MainMenu mainMenu = new MainMenu(frame);
            frame.add(mainMenu);
            frame.setSize(FlappyBirdGame.WIDTH, FlappyBirdGame.HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            
        });
    }
}
