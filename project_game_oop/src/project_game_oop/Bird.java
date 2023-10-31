package project_game_oop;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bird {
    private int birdY;
    private int velocity;
    private BufferedImage birdImageTop;
    private BufferedImage birdImageBottom;
    private int birdImageIndex;
    
    private Timer imageChangeTimer;
    
    public Bird() {
        birdY = 150;
        velocity = 0;
        birdImageIndex = 0;

        try {
            birdImageTop = ImageIO.read(new File("bird_top.png"));
            birdImageBottom = ImageIO.read(new File("bird_bottom.png"));
            ImageIO.read(new File("background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // สร้าง Timer สำหรับการสลับภาพของนก
        imageChangeTimer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleBirdImage();
            }
        });
        imageChangeTimer.start(); // เริ่มต้นการทำงานของ Timer
    }
    
    private void toggleBirdImage() {
        if (birdImageIndex == 0) {
            birdImageIndex = 1;
        } else {
            birdImageIndex = 0;
        }
    }
    
    public void jump() {
        if (birdY > 0) {
            velocity = -10;
        }
    }

    public void move() {
        birdY += velocity;
        velocity += 1;

        if (birdY > FlappyBirdGame.HEIGHT - 50) {
            birdY = FlappyBirdGame.HEIGHT - 50;
            velocity = 0;
        }
    }

    public void draw(Graphics g) {
        if (birdImageIndex == 0) {
            g.drawImage(birdImageTop, 100, birdY, 50, 50, null);
        } else {
            g.drawImage(birdImageBottom, 100, birdY, 50, 50, null);
        }
    }

    public int getBirdY() {
        return birdY;
    }

    public Rectangle getBounds() {
        return new Rectangle(100, birdY, 50, 50);
    }

    public boolean checkCollision(Pipe pipe) {
        Rectangle birdBounds = getBounds();
        Rectangle topPipeBounds = new Rectangle(pipe.getX(), 0,120, pipe.getTopHeight());
        Rectangle bottomPipeBounds = new Rectangle(pipe.getX(), pipe.getBottomY(), 120, FlappyBirdGame.HEIGHT - pipe.getBottomY());

        return birdBounds.intersects(topPipeBounds) || birdBounds.intersects(bottomPipeBounds);
    }

    public void reset() {
        birdY = 150;
        velocity = 0;
    }
}
