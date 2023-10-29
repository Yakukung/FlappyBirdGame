package project_game_oop;

import java.awt.Rectangle;

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
}
