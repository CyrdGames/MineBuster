package minebustergame;

import java.util.ArrayList;
import java.util.Collections;
import util.Perlin;

public class MineField {
    
    Tile[][] field;
    private int width, height;
    private int numBombs = 100;
    
    public MineField(int width, int height) {
        System.out.println("new field");
        this.width = width;
        this.height = height;
        field = new Tile[width][height];
        initializeBlankField();
    }
    
    private void initializeBlankField() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = new Tile(i, j);
                field[i][j].setState(Tile.UNREVEALED);
            }
        }
    }
    
    public void populateField(int x, int y) {
        int octaves = 2;
        float normalise = 0.62f;

        Perlin noise = new Perlin(width, height, octaves, normalise);
        int[][] mask = noise.paintImage(noise.getData());
        

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(i + x >= 0 && j + y >= 0 && i + x < field.length && j + y < field[0].length && !(x == 0 && y == 0)) {
                    mask[x + i][y + j] = 1;                    
                }
            }
        }
        
        ArrayList<Tile> potentialBomb = new ArrayList();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (mask[i][j] == 0) { //orange tiles
                    potentialBomb.add(field[i][j]);
                }
            }
        }

        setNeighbours();

        Collections.shuffle(potentialBomb);

        for (int i = 0; i < numBombs; i++) {
            try {
                potentialBomb.get(i).setType(Tile.BOMB);
            } catch (Exception e) {
                System.err.println("Too many bombs! ( > " + i + ")");
                break;
            }
        }
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j].calculateType();
            }
        }
    }
    
    public Tile[][] getField() {
        return field;
    }
    
    private void setNeighbours() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (i + k >= 0 && j + l >= 0 && i + k < field.length && j + l < field[0].length && !(k == 0 && l == 0)) {
                            field[i][j].addNeighbour(field[i + k][j + l]);
                        }
                    }
                }
            }
        }
    }
}
