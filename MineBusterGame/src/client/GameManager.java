package client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import minebustergame.MineField;
import minebustergame.Tile;

public class GameManager {
    
    private BufferedImage mainImage;
    
    private Tile[][] tiles;
    private MineField field;
    
    public final static int FLAGGED = 9;
    public final static int MINE = 10;
    public final static int UNREVEALED = 11;
    public final static int TILE_SIZE = 19;
    public final static int WIDTH = 30;
    public final static int HEIGHT = 16;
    
    public GameManager(){
        try{
            mainImage = ImageIO.read(new File("graphics/tiles.png"));
            Tile.generateImages(mainImage, 32, 32, 12, TILE_SIZE);
        } catch (IOException e){
            System.err.println(e);
        }
        
        field = new MineField(WIDTH, HEIGHT);
        tiles = field.getField();
    }
    
    public void revealTile(int x, int y) {
        tiles[x][y].setState(0);
    }
    
    public void draw(Graphics2D graphics){
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                tiles[i][j].draw(graphics);
            }
        }       
    }
}
