package project_game_oop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBirdGame extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    public static final int WIDTH =  1024;
    public static final int HEIGHT = 640;

    private List<Pipe> pipes;
    private Bird bird;
    private int score;
    private boolean isGameOver;
    private Timer timer;

    public FlappyBirdGame() {
        pipes = new ArrayList<>();
        bird = new Bird();
        score = 0;
        isGameOver = false;

        timer = new Timer(20, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        bird.jump();
                    }
                if (isGameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resetGame();
                }
            }
        });

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void jumpBird() {
        bird.jump();
    }

    public void moveBird() {
        bird.move();
    }

    public void movePipes() {
        for (int i = pipes.size() - 1; i >= 0; i--) {
            Pipe pipe = pipes.get(i);
            pipe.move();

            if (pipe.getX() + 100 < 0) {
                pipes.remove(i);
                score++;
            }

            if (bird.checkCollision(pipe)) {
                isGameOver = true;
            }
        }

        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < WIDTH - 300) {
            createPipe();
        }
    }

    public void createPipe() {
        Random random = new Random();
        int gapHeight = 300;
        int pipeY = random.nextInt(HEIGHT - gapHeight - 100);
        pipes.add(new Pipe(WIDTH, pipeY, gapHeight));
    }

    public void resetGame() {
        pipes.clear();
        bird.reset();
        score = 0;
        isGameOver = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            moveBird();
            movePipes();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        bird.draw(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 20, 20);

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2 - 20);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press Enter to Play Again", WIDTH / 2 - 140, HEIGHT / 2 + 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flappy Bird");
            FlappyBirdGame game = new FlappyBirdGame();
            frame.add(game);
            frame.setSize(WIDTH, HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
