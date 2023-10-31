package project_game_oop;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private List<Pipe> pipes;
    private Bird bird;
    private int score;

    public GamePanel(List<Pipe> pipes, Bird bird) {
        this.pipes = pipes;
        this.bird = bird;
        this.score = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Pipe pipe : pipes) {
            drawPipe(g, pipe);
        }
        bird.draw(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 20, 20);
    }

    public void drawPipe(Graphics g, Pipe pipe) {
        g.setColor(Color.GREEN);
        g.fillRect(pipe.getX(), 0, 100, pipe.getTopHeight());
        g.fillRect(pipe.getX(), pipe.getBottomY(), 100, FlappyBirdGame.HEIGHT - pipe.getBottomY());
    }

    public void setScore(int score) {
        this.score = score;
    }
}
