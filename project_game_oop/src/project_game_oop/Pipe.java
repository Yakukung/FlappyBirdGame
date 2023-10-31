package project_game_oop;

import java.awt.*;

public class Pipe {
    private int x;
    private int topHeight;
    private int gapHeight;

    public Pipe(int x, int topHeight, int gapHeight) {
        this.x = x;
        this.topHeight = topHeight;
        this.gapHeight = gapHeight;
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
        g.setColor(Color.GREEN);
        g.fillRect(x, 0, 100, topHeight);
        g.fillRect(x, getBottomY(), 100, FlappyBirdGame.HEIGHT - getBottomY());
    }
}
