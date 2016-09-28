package minebustergame;

import java.util.ArrayList;

public class Tile {

    public static final int BOMB = 9;

    public int x, y;
    public int type; //0 -> 8, 9 - Bomb
    public int state; //0 - unrevealed     1 - flagged    2 - revealed
    ArrayList<Tile> neighbours = new ArrayList();

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = 0;
        this.type = -1;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addNeighbour(Tile n) {
        neighbours.add(n);
    }

    public void calculateType() {
        if (this.type == -1) {
            this.type = 0;

            for (Tile t : neighbours) {
                if (t.type == Tile.BOMB) {
                    this.type += 1;
                }
            }
        }
    }
}
