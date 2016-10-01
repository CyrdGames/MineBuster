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
        populateField();
    }
    
    private void populateField() {
        System.out.println("pop");
        int octaves = 2;
        float normalise = 0.62f;

        Perlin noise = new Perlin(width, height, octaves, normalise);
        int[][] mask = noise.paintImage(noise.getData());

        ArrayList<Tile> potentialBomb = new ArrayList();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = new Tile(i, j);
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
                System.out.println(field[i][j].type);
            }
        }
        System.out.println("pop done");
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
