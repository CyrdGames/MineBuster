//package client;
//
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.imageio.ImageIO;
//import minebustergame.MineField;
//import minebustergame.Tile;
//
//public class GameManager {
//    
//    private BufferedImage mainImage;
//    
//    private static Tile[][] tiles;
//    public static MineField field;
//    
//    public final static int NUM_COLUMNS = 30;
//    public final static int NUM_ROWS = 16;
//    
//    public boolean checkNeighbours = true;
//    private static boolean firstClick;
//    private static boolean assist = false;
//    
//    public GamePanel p;
//    
//    public GameManager(GamePanel panel){
//        try{
//            this.p = panel;
//            mainImage = ImageIO.read(new File("graphics/tiles.png"));
//            Tile.generateImages(mainImage, 32, 32, 13);
//        } catch (IOException e){
//            System.err.println(e);
//        }
//        
//        field = new MineField(NUM_COLUMNS, NUM_ROWS);
//        
//        tiles = field.getField();        
//        firstClick = true;       
//    }
//    
//    public void revealTile(int x, int y) {
//        if(firstClick) {
//            field.populateField(x, y);
//            tiles = field.getField();
//            firstClick = false;
//            revealTile(x,y);                        
//        } else if (tiles[x][y].getState() == Tile.UNREVEALED){
//            tiles[x][y].setState(Tile.REVEALED);
//            tiles[x][y].fogged = false;
//            
//            if(tiles[x][y].getType() == Tile.BOMB) {
//                try {
//                    revealAllBombs();
//                    p.listenerThread.sleep(10000l);
//                    System.exit(0);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            
//            if (tiles[x][y].getType() == 0){
//                for (Tile t : tiles[x][y].getNeighbours()){
//                    revealTile(t.getX(), t.getY());
//                }
//            }
//        } else if (checkNeighbours && tiles[x][y].getState() == Tile.REVEALED/* && tiles[x][y].getType() != Tile.BOMB*/){
//            int numFlags = 0;
//            checkNeighbours = false;
//            ArrayList<Tile> neighbours = tiles[x][y].getNeighbours();
//                        
//            for(Tile t : neighbours) {
//                if(t.getState() == Tile.FLAGGED) {
//                    numFlags += 1;
//                }  
//            }
//            
//            System.out.println("flags: " + numFlags);
//            if (tiles[x][y].getType() == numFlags && tiles[x][y].getType() != 0){
//                for (Tile t : neighbours) {
//                    revealTile(t.getX(), t.getY());
//                }
//            }
//        }
//    }
//    
//    private void revealAllBombs() {
//        for(int i = 0; i < NUM_COLUMNS; i++) {
//            for(int j = 0; j < NUM_ROWS; j++) {
//                if(tiles[i][j].getType() == Tile.BOMB) {
//                    tiles[i][j].setState(Tile.REVEALED);
//                }
//            }
//        }
//    }
//    
//    public void flagTile(int x, int y) {
//        switch (tiles[x][y].getState()){
//            case Tile.UNREVEALED:
//                tiles[x][y].setState(Tile.FLAGGED);
//                break;
//            case Tile.FLAGGED:
//                tiles[x][y].setState(Tile.UNREVEALED);
//                break;
//        }
//        
////        field.generateFog(5);
////        field.sonar(x, y, 5);
//        field.assist();
//    }
//    
//    public void toggleAssist() {
//        Tile.assist = !Tile.assist;
//    }
//    
//    public void draw(Graphics2D graphics){
//        for (int i = 0; i < NUM_COLUMNS; i++){
//            for (int j = 0; j < NUM_ROWS; j++){
//                tiles[i][j].draw(graphics);
//            }
//        }       
//    }
//    
//    public static void setField(MineField f) {
//        field = f;
//        tiles = field.getField();
//        firstClick = false;
//    }
//}
