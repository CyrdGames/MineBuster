package minebustergame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import util.Perlin;

public class MineBusterGame {

    public static void main(String[] args) {
        int width = 32;
        int height = 16;
        int octaves = 2;
        float normalise = 0.52f;

        int numBombs = 100;

        Perlin noise = new Perlin(width, height, octaves, normalise);
        int[][] mask = noise.paintImage(noise.getData());

        Tile[][] tiles = new Tile[width][height];

        ArrayList<Tile> potentialBomb = new ArrayList();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Tile(i, j);
                if (mask[i][j] == 0) { //orange tiles
                    potentialBomb.add(tiles[i][j]);
                }
            }
        }

        setNeighbours(tiles);

        Collections.shuffle(potentialBomb);

        for (int i = 0; i < numBombs; i++) {
            try {
                potentialBomb.get(i).setType(Tile.BOMB);
            } catch (Exception e) {
                System.err.println("Too many bombs! ( > " + i + ")");
                break;
            }
        }

        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j].calculateType();
//                System.out.print(tiles[i][j].type + " ");
                switch (tiles[i][j].type) {
                    case 0:
                        g.setPaint(Color.WHITE);
                        break;
                    case 1:
                        g.setPaint(Color.BLUE);
                        break;
                    case 2:
                        g.setPaint(Color.GREEN);
                        break;
                    case 3:
                        g.setPaint(Color.RED);
                        break;
                    case 4:
                        g.setPaint(Color.ORANGE);
                        break;
                    case 5:
                        g.setPaint(Color.YELLOW);
                        break;
                    case 6:
                        g.setPaint(Color.cyan);
                        break;
                    case 7:
                        g.setPaint(Color.MAGENTA);
                        break;
                    case 8:
                        g.setPaint(Color.PINK);
                        break;
                    case 9:
                        g.setPaint(Color.BLACK);
                        break;
                }
                g.fillRect(i, j, 1, 1);
            }
//            System.out.println("");
        }

        try {
            ImageIO.write(im, "png", new File("field.png"));
        } catch (IOException ex) {
            Logger.getLogger(MineBusterGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setNeighbours(Tile[][] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (i + k >= 0 && j + l >= 0 && i + k < tiles.length && j + l < tiles[0].length && !(k == 0 && l == 0)) {
                            tiles[i][j].addNeighbour(tiles[i + k][j + l]);
                        }
                    }
                }
            }
        }
    }
}
