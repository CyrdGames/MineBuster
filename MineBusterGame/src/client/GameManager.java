package client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameManager {
    
    private BufferedImage mainImage;
    private BufferedImage[] subImages;
    private int[][] tileState;
    
    public final static int FLAGGED = 9;
    public final static int MINE = 10;
    public final static int UNREVEALED = 11;
    public final static int TILE_SIZE = 19;
    
            //bgImage = ImageIO.read(new File("test_images/bluered_32x32.png"));
    public GameManager(){
        try{
            mainImage = ImageIO.read(new File("graphics/tiles.png"));
            subImages = new BufferedImage[12];
            for (int i = 0; i < 12; i++){
                subImages[i] = mainImage.getSubimage(i*32, 0, 32, 32);
                subImages[i].createGraphics().scale(TILE_SIZE/32.0, TILE_SIZE/32.0);
            }
        } catch (IOException e){
            System.err.println(e);
        }
        tileState = new int[GamePanel.WIDTH / TILE_SIZE][GamePanel.HEIGHT / TILE_SIZE];
        for (int i = 0; i < GamePanel.WIDTH / TILE_SIZE - 1; i++){
            for (int j = 0; j < GamePanel.HEIGHT / TILE_SIZE - 1; j++){
                tileState[i][j] = (int)(Math.random() * 12);
            }
        }
    }
    
    public void draw(Graphics2D graphics){
        for (int i = 0; i < GamePanel.WIDTH / TILE_SIZE - 1; i++){
            for (int j = 0; j < GamePanel.HEIGHT / TILE_SIZE - 1; j++){
                graphics.drawImage(subImages[tileState[i][j]], i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
//                graphics.drawI
                //graphics.drawImage
            }
        }
    }
    
    public void updateTileState(int x_tile, int y_tile, int state){
        this.tileState[x_tile][y_tile] = state;
    }
}
