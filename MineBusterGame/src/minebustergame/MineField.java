package minebustergame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import util.Perlin;

public class MineField {

    Tile[][] field;
    private int width, height;
    private int numBombs = 100;
    private int tileSize = 25;
    private boolean fogged = false;
    private int fogStack = 0;
    
    public MineField(int width, int height) {
        this.width = width;
        this.height = height;
        field = new Tile[width][height];
        initializeBlankField();
    }

    private void initializeBlankField() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = new Tile(i, j, tileSize);
                field[i][j].setState(Tile.UNREVEALED);
            }
        }
    }

    public void populateField(int x, int y) {
        int octaves = 2;
        float normalise = 0.62f;
        Perlin noise = new Perlin(width, height, octaves, normalise);
        int[][] mask = noise.paintImage(noise.getData());

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i + x >= 0 && j + y >= 0 && i + x < field.length && j + y < field[0].length && !(x == 0 && y == 0)) {
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

        checkBombCluster(potentialBomb);
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j].calculateType();
            }
        }       
        
        paintField();
    }

    public void generateFog(int seconds) {
        if(!fogged) {
            fogged = true;
            Perlin perlin = new Perlin(width, height, 5, 0.64f);
            int[][] mask = perlin.paintImage(perlin.getData());

            for(int i = 0; i < field.length; i++) {
                for(int j = 0; j < field[0].length; j++) {
                    if(mask[i][j] == 1) {
                        System.out.println("ya");
                        field[i][j].fogged = true;
                    }
                }
            }

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(fogStack < 1) {
                        for(int i = 0; i < field.length; i++) {
                            for(int j = 0; j < field[0].length; j++) {
                                field[i][j].fogged = false;
                            }
                        }
                        fogged = false;
                        timer.cancel();
                    } else {
                        fogStack -= 1;
                    }
                }
            }, 1000l * seconds, 1000l * seconds);
        } else {
            fogStack += 1;
        }
    }
    
    public void sonar(int x, int y, int radius) {
        for(int i = 0; i < field.length; i++) {
            for(int j = 0; j < field[0].length; j++) {
                if((i - x) * (i - x) + (j - y) * (j - y) < radius * radius) {
                    field[i][j].detected = true;
                }
            }
        }
    }
    
    public void assist() {        
        for(Tile[] t : field) {
            for(Tile ti : t) {
                if(ti.getState() == Tile.UNREVEALED || 
                        ti.getType() == Tile.BOMB || 
                        ti.getType() == 0 || 
                        ti.getState() == Tile.FLAGGED) {
                    continue;
                }
                int i = 0;
                for(Tile n : ti.getNeighbours()) {
                    if(n.getState() == Tile.FLAGGED) {
                        i += 1;
                    }
                }
                ti.currentType = Math.max(0, ti.getType() - i);
            }            
        }
    }
    
    private void checkBombCluster(ArrayList<Tile> bombs) {
        boolean good = false;
        int loops = 0;        
        int bombsToReplace;
        
        while(!good && loops < 100) {
            good = true;
            bombsToReplace = 0;            
            
            for(Tile t : bombs) {
                if(t.getType() != Tile.BOMB) {
                    continue;
                }
                
                int numBomb = 0;
                
                for(Tile t2: t.getNeighbours()) {                    
                    if(t2.getType() == Tile.BOMB) {
                        numBomb += 1;
                    }

                    if(numBomb == t.getNeighbours().size()) {
                        good = false;
                        t.setType(t.getNeighbours().size());
                        bombsToReplace += 1;
//                        System.out.println("Replacing bomb on: " + t.getX() + " " + t.getY() + " with: " + t.getNeighbours().size());
                    }
                }  
            }
            
            Collections.shuffle(bombs);
            
            for(int i = 0; i < bombsToReplace; i++) {
                for(Tile t : bombs) {
                    if(t.getType() == -1) {
                        t.setType(Tile.BOMB);
                        break;
                    }
                }
            }
            
            loops += 1;
        }
    }
    
    private void paintField() {
        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) im.getGraphics();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                switch (field[i][j].getType()) {
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
                        g.setPaint(Color.CYAN);
                        break;
                    case 5:
                        g.setPaint(Color.YELLOW);
                        break;
                    case 6:
                        g.setPaint(Color.MAGENTA);
                        break;
                    case 7:
                        g.setPaint(Color.ORANGE);
                        break;
                    case 8:
                        g.setPaint(Color.PINK);
                        break;
                    case 10:
                        g.setPaint(Color.BLACK);
                        break;
                }

                g.fillRect(i, j, 1, 1);
            }
        }
        
        try {
            ImageIO.write(im, "PNG", new File("field.png"));
        } catch (IOException ex) {
            Logger.getLogger(MineField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Tile[][] getField() {
        return field;
    }

    public void setNeighbours() {
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
