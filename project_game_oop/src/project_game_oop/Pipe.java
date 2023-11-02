package project_game_oop;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Pipe {
    private int x;
    private int topHeight;
    private int gapHeight;
    private BufferedImage pipeImage;
    private BufferedImage rotatedPipeImage; // ภาพท่อหมุน 180 องศา

    public Pipe(int x, int topHeight, int gapHeight) {
        this.x = x;
        this.topHeight = topHeight;
        this.gapHeight = gapHeight;

        try {
            pipeImage = ImageIO.read(new File("pipe.png"));
            rotatedPipeImage = rotateImage(pipeImage, 180, gapHeight); // หมุนภาพท่อ 180 องศา
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move() {
        x -= 7;
    }

    public int getX() {
        return x;
    }

    public int getTopHeight() {
        return topHeight;
    }

    public int getBottomY() {
        return topHeight + gapHeight;
    }

    public void draw(Graphics g) {
        g.drawImage(rotatedPipeImage, x, 0, 100, topHeight, null); // ใช้ภาพท่อหมุน 180 องศา
        g.drawImage(pipeImage, x, getBottomY(), 100, FlappyBirdGame.HEIGHT - (topHeight + gapHeight), null); // ฝั่งล่างไม่ต้องหมุน
    }


    // เมทอดสำหรับหมุนภาพ
    private BufferedImage rotateImage(BufferedImage originalImage, int degrees, int newHeight) {
        int width = originalImage.getWidth();
        BufferedImage rotatedImage = new BufferedImage(width, newHeight, originalImage.getType()); // กำหนดความสูงใหม่

        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.rotate(Math.toRadians(degrees), width / 2, newHeight / 2); // ปรับความสูง
        g2d.drawImage(originalImage, null, 0, 0);

        return rotatedImage;
    }

}
