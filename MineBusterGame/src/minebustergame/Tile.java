package minebustergame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tile {
    
    public static final int FOG = 12;
    public static final int BOMB = 10;
    public static final int FLAGGED = 9;
    public static final int UNREVEALED = 11;
    public static final int REVEALED = 12;
    
    public static int TILE_SIZE = 19;
    
    public boolean fogged;
    public boolean detected;
    public static boolean assist = false;
    
    private static final BufferedImage[] IMAGE = new BufferedImage[13];
    
    private int x, y;
    private int type; //0 -> 8, 9 - Bomb
    public int currentType;
    private int state; //0 - unrevealed     1 - flagged    2 - revealed
    private transient ArrayList<Tile> neighbours = null;

    public Tile(int x, int y, int size) {
        //System.out.println("inti");
        this.x = x;
        this.y = y;
        this.fogged = false;
        this.detected = false;
        this.state = UNREVEALED;
        this.type = this.currentType = -1;
        neighbours = new ArrayList();
        Tile.TILE_SIZE = size;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public int getType(){
        return this.type;
    }
    
    public int getState(){
        return this.state;
    }
    
    public ArrayList<Tile> getNeighbours(){
        return this.neighbours;
    }

    public void setType(int type) {
        this.type = type;
        this.currentType = type;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addNeighbour(Tile n) {
        if(neighbours == null) {
            neighbours = new ArrayList();
        }
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
            
            this.currentType = type;
        }
    }
    
    public void draw(Graphics2D g) {
        int drawType = currentType;
        if(!assist) {            
            drawType = type;
        }
        
        if(detected && type == BOMB && state != FLAGGED) {
            g.drawImage(IMAGE[BOMB], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        } else if(fogged && !detected) {
            g.drawImage(IMAGE[FOG], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        } else {
            switch(state){
                case UNREVEALED:
                    g.drawImage(IMAGE[UNREVEALED], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    break;
                case FLAGGED:
                    if(assist) {
                        g.drawImage(IMAGE[0], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    } else {
                        g.drawImage(IMAGE[FLAGGED], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                    break;
                case REVEALED:
                    g.drawImage(IMAGE[drawType], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    break;
                default:
                    System.err.println("Error: Unknown tile state [" + x + "][" + y + "]");
                    System.exit(0);
            }   
        }   
    }
    
    public static void generateImages(BufferedImage mainImage, int width, int height, int num) {
        for (int i = 0; i < num; i++){
            IMAGE[i] = mainImage.getSubimage(i * width, 0, width, height);
            IMAGE[i].createGraphics().scale(TILE_SIZE / 1.0 / width, TILE_SIZE / 1.0 / height);
        }
    }
}
