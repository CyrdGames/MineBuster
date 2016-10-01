package minebustergame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tile {

    public static final int BOMB = 10;
    public static final int FLAG = 9;
    public static final int UNREVEALED = 11;
    public static final int REVEALED = 12;
    
    private static final BufferedImage[] IMAGE = new BufferedImage[12];
    private static int TILE_SIZE;
    
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
        this.state = 2;
        if (this.type == -1) {
            this.type = 0;

            for (Tile t : neighbours) {
                if (t.type == Tile.BOMB) {
                    this.type += 1;
                }
            }
        }
    }
    
    public void draw(Graphics2D g) {
        if(state == 2) {
            g.drawImage(IMAGE[11], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        } else {
            g.drawImage(IMAGE[type], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        }
    }
    
    public static void generateImages(BufferedImage mainImage, int width, int height, int num, int scale) {
        TILE_SIZE = scale;
        for (int i = 0; i < num; i++){
            IMAGE[i] = mainImage.getSubimage(i * width, 0, width, height);
            IMAGE[i].createGraphics().scale(scale / 1.0 / width, scale / 1.0 / height);
        }
    }
}
