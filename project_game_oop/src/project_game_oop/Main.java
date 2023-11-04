package project_game_oop;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flappy Bird");
            FlappyBirdGame game = new FlappyBirdGame();
            frame.add(game);
            frame.setSize(FlappyBirdGame.WIDTH, FlappyBirdGame.HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
