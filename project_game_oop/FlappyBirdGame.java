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
    private int birdY;
    private int velocity;
    private List<Pipe> pipes;
    private boolean isGameStarted;
    private boolean isGameOver;
    private int score;

    public FlappyBirdGame() {
        birdY = 150;
        velocity = 0;
        pipes = new ArrayList<>();
        isGameStarted = false;
        isGameOver = false;
        score = 0;

        Timer timer = new Timer(20, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!isGameStarted) {
                        isGameStarted = true;
                        pipes.clear();
                        score = 0;
                    }
                    jump();
                }
                if (isGameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resetGame();
                }
            }
        });
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void jump() {
        if (birdY > 0) {
            velocity = -10;
        }
    }

    public void moveBird() {
        birdY += velocity;
        velocity += 1;

        if (birdY > getHeight() - 50) {
            birdY = getHeight() - 50;
            velocity = 0;
        }
    }

    public void movePipes() {
        for (int i = pipes.size() - 1; i >= 0; i--) {
            Pipe pipe = pipes.get(i);
            pipe.move();

            if (pipe.getX() + 100 < 0) {
                pipes.remove(i);
                score++;
            }

            checkCollision(pipe); // เรียก checkCollision และส่ง Pipe เป็นอาร์กิวเมนต์
        }

        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < 640) {
            createPipe();
        }
    }

    public void createPipe() {
        Random random = new Random();
        int gapHeight = random.nextInt(200) + 100;
        int pipeY = random.nextInt(getHeight() - gapHeight - 100);
        pipes.add(new Pipe(1280, pipeY, gapHeight));
    }

    public void resetGame() {
        birdY = 150;
        velocity = 0;
        pipes.clear();
        isGameOver = false;
        score = 0;
    }

    public void checkCollision(Pipe pipe) {
        Rectangle birdBounds = new Rectangle(100, birdY, 50, 50);
        Rectangle topPipeBounds = new Rectangle(pipe.getX(), 0, 100, pipe.getTopHeight());
        Rectangle bottomPipeBounds = new Rectangle(pipe.getX(), pipe.getBottomY(), 100, getHeight() - pipe.getBottomY());

        if (birdBounds.intersects(topPipeBounds) || birdBounds.intersects(bottomPipeBounds)) {
            isGameOver = true;
            showGameOverMessage();
        }
    }

    private void showGameOverMessage() {
        SwingUtilities.invokeLater(() -> {
            repaint();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameStarted && !isGameOver) {
            moveBird();
            movePipes();
            checkCollision(pipes.get(pipes.size() - 1)); // เรียก checkCollision และส่ง Pipe ล่าสุดเป็นอาร์กิวเมนต์
        }
        if (isGameOver) {
            // ไม่ต้องเรียก repaint() ที่นี่
        } else {
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBird(g);
        for (Pipe pipe : pipes) {
            drawPipe(g, pipe);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 20, 20);

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            g.drawString("Game Over", 300, 300);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press Enter to Play Again", 280, 350);
        }
    }

    public void drawBird(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(100, birdY, 50, 50);
    }

    public void drawPipe(Graphics g, Pipe pipe) {
        g.setColor(Color.GREEN);
        g.fillRect(pipe.getX(), 0, 100, pipe.getTopHeight());
        g.fillRect(pipe.getX(), pipe.getBottomY(), 100, getHeight() - pipe.getBottomY());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flappy Bird");
            FlappyBirdGame game = new FlappyBirdGame();
            frame.add(game);
            frame.setSize(1280, 800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
