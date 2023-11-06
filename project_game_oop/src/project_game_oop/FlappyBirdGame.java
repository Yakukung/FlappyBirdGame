package project_game_oop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class FlappyBirdGame extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 640;

    private List<Pipe> pipes;
    private Bird bird;
    private int score;
    private boolean isGameOver;
    private boolean isPaused;
    private Timer timer;
    private int collisionCount = 5;


    private BufferedImage backgroundImage;

    private Clip jumpSound;
    private Clip scoreSound;
    private Clip gameOverSound;
    private Clip backgroundSound;

    public FlappyBirdGame() {
        pipes = new ArrayList<>();
        bird = new Bird();
        score = 0;
        isGameOver = false;
        isPaused = false;

        timer = new Timer(20, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !isPaused) {
                    bird.jump();
                    playJumpSound();
                } else if (isGameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resetGame();
                } else if (e.getKeyCode() == KeyEvent.VK_P && !isGameOver) {
                    togglePauseGame();
                }
            }
        });

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        try {
            backgroundImage = ImageIO.read(new File("background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (loadSounds()) {
            playJumpSound();
        } else {
            System.err.println("Failed to load one or more audio clips.");
        }
    }

    private boolean loadSounds() {
        try {
            AudioInputStream jumpStream = AudioSystem.getAudioInputStream(new File("Flying.wav"));
            jumpSound = AudioSystem.getClip();
            jumpSound.open(jumpStream);

            AudioInputStream scoreStream = AudioSystem.getAudioInputStream(new File("Score.wav"));
            scoreSound = AudioSystem.getClip();
            scoreSound.open(scoreStream);

            AudioInputStream gameOverStream = AudioSystem.getAudioInputStream(new File("GameOver.wav"));
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(gameOverStream);

            return true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void playJumpSound() {
        if (jumpSound != null) {
            jumpSound.setFramePosition(0);
            new Thread(() -> {
                try {
                    Thread.sleep(0);
                    jumpSound.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void playScoreSound() {
        if (scoreSound != null) {
            scoreSound.setFramePosition(0);
            new Thread(() -> {
                try {
                    Thread.sleep(0);
                    scoreSound.start();
                } catch (InterruptedException e) {
                e.printStackTrace();
                }
            }).start();
        }
    }

    private void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }

    private void togglePauseGame() {
        isPaused = !isPaused;
    }

    void playBackgroundSound() {
        try {
            AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(new File("backgroundSound.wav"));
            backgroundSound = AudioSystem.getClip();
            backgroundSound.open(backgroundStream);
            backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundSound.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
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
                playScoreSound();
            }

            if (bird.checkCollision(pipe)) {
                collisionCount--;
                if (collisionCount >= 1) {
                	pipes.clear();
                    bird.reset();
                } else if(collisionCount < 1) {
                    // ถ้าเกิดการชนครั้งที่ 5 ให้แสดง GameOver
                    isGameOver = true;
                    stopBackgroundSound();
                    playGameOverSound();
                }
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
        collisionCount = 5; // รีเซ็ตจำนวนการชน
        isGameOver = false;
        isPaused = false;
        playBackgroundSound();
    }

    private void stopBackgroundSound() {
        if (backgroundSound != null && backgroundSound.isRunning()) {
            backgroundSound.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            if (!isGameOver) {
                moveBird();
                movePipes();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);

        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        bird.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("CooperThin", Font.PLAIN, 30));
        g.drawString("คะแนน: " + score, 20, 40);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("CooperThin", Font.PLAIN, 20));
        String pauseMessage = "กดปุ่ม P เพื่อพัก/หยุดพัก";
        int messageWidth = g.getFontMetrics().stringWidth(pauseMessage);
        g.drawString(pauseMessage, WIDTH - messageWidth - 20, 40);
        
        g.setColor(Color.RED);
        g.setFont(new Font("CooperThin", Font.PLAIN, 30));
        g.drawString("ชีวิตที่เหลือ: " + collisionCount, 20, 80);


        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("CooperThin", Font.PLAIN, 40));
            g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2 - 20);

            g.setFont(new Font("CooperThin", Font.PLAIN, 20));
            g.drawString("กด Enter เพื่อเริ่มเกมใหม่", WIDTH / 2 - 115, HEIGHT / 2 + 20);
        }
        if (isPaused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("CooperThin", Font.PLAIN, 40));
            g.drawString("หยุดพักแล้ว", WIDTH / 2 - 100, HEIGHT / 2 - 20);
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
            game.playBackgroundSound();
        });
    }
}
