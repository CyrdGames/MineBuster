package server;

import java.util.ArrayList;
import minebustergame.MineField;
import minebustergame.Tile;

public class ServerGameManager {
    private static Tile[][] tiles;
    public static MineField field;
    
    public final static int NUM_COLUMNS = 30;
    public final static int NUM_ROWS = 16;
    
    public static boolean checkNeighbours = true;
    private static boolean firstClick;
    
    public ServerGameManager() {
        field = new MineField(NUM_COLUMNS, NUM_ROWS);
        tiles = field.getField();        
        firstClick = true;   
    }
    
    public static void revealTile(int x, int y) {
        if(firstClick) {
            field.populateField(x, y);
            tiles = field.getField();
            firstClick = false;
            revealTile(x,y);
        } else if (tiles[x][y].getState() == Tile.UNREVEALED){
            tiles[x][y].setState(Tile.REVEALED);
            tiles[x][y].fogged = false;
            
            if(tiles[x][y].getType() == Tile.BOMB) {
                //TODO send bomb
            }
            
            if (tiles[x][y].getType() == 0){
                for (Tile t : tiles[x][y].getNeighbours()){
                    revealTile(t.getX(), t.getY());
                }
            }
        } else if (checkNeighbours && tiles[x][y].getState() == Tile.REVEALED){
            int numFlags = 0;
            checkNeighbours = false;
            ArrayList<Tile> neighbours = tiles[x][y].getNeighbours();
                        
            for(Tile t : neighbours) {
                if(t.getState() == Tile.FLAGGED) {
                    numFlags += 1;
                }  
            }
            
            if (tiles[x][y].getType() == numFlags && tiles[x][y].getType() != 0){
                for (Tile t : neighbours) {
                    revealTile(t.getX(), t.getY());
                }
            }
        }
    }
        
    public static void flagTile(int x, int y) {
        switch (tiles[x][y].getState()){
            case Tile.UNREVEALED:
                tiles[x][y].setState(Tile.FLAGGED);
                break;
            case Tile.FLAGGED:
                tiles[x][y].setState(Tile.UNREVEALED);
                break;
        }
        
//        field.generateFog(5);
//        field.sonar(x, y, 5);
//        field.assist();
    }    
    
    public static void setField(MineField f) {
        field = f;
        tiles = field.getField();
        firstClick = false;
    }    
}
    
