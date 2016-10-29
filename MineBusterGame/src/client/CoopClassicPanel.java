package client;

import data.Authentication;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import minebustergame.MineField;
import minebustergame.Tile;
import util.Serialization;

public class CoopClassicPanel extends GamePanel{
    
    private Graphics2D graphics;
    private BufferedImage mainImage;
    private TileClickListener mouseListener;
    public Thread listenerThread;
    
    public final static int WIDTH = 960;
    public final static int HEIGHT = 512;
    
    private Tile[][] tiles;
    private MineField field;
    
    public final static int NUM_COLUMNS = 30;
    public final static int NUM_ROWS = 16;
    
    public boolean checkNeighbours;
    private static boolean firstClick;
    private static boolean assist;
    
    private long begTime;
    private JLabel labelTime;
    
    public CoopClassicPanel (ClientLock syncSend){
        super();
        setFocusable(true);
        this.requestFocus();
        
        System.out.println("init");
        
        this.syncSend = syncSend;
        this.mainImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.graphics = (Graphics2D) this.mainImage.getGraphics();
        this.running = true;
        this.mouseListener = new TileClickListener(this);
        this.listenerThread = null;
        
        try{
            BufferedImage tileImages = ImageIO.read(new File("graphics/tiles.png"));
            Tile.generateImages(tileImages, 32, 32, 13);
        } catch (IOException e){
            System.err.println(e);
        }
        
        field = new MineField(NUM_COLUMNS, NUM_ROWS);
        tiles = field.getField();
        checkNeighbours = true;
        firstClick = true;
        assist = false;
        
        begTime = System.currentTimeMillis();
        
        //labelTime = new JLabel("Time: " + (System.currentTimeMillis() - begTime));
    }
    
    @Override
    public void addNotify(){
        super.addNotify();
        if(this.listenerThread == null){
            addMouseListener(mouseListener);
            addKeyListener(mouseListener);
            this.listenerThread = new Thread(this);
            this.listenerThread.start();
        }
    }
    
    @Override
    public void run() {
        sendMessage("/getMineField");
        setFocusable(true);
        requestFocus();
        //this.add(labelTime);
        while (running){
            //FIXME: move label to appropriate location; potentially change
            //labelTime.setText("Time: " + (System.currentTimeMillis() - begTime));
            draw();
            //TODO: Implement proper framerate control
//            try {
//                listenerThread.wait((long)(1000/60));
//                this.wait((long)(1000/60));
//            } catch (InterruptedException ex) {
//                Logger.getLogger(SinglePlayerPanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }        
    }
    
    @Override
    protected void draw(){
        for (int i = 0; i < NUM_COLUMNS; i++){
            for (int j = 0; j < NUM_ROWS; j++){
                tiles[i][j].draw(graphics);
            }
        }
        
        Graphics screenGraphics = super.getGraphics();
        screenGraphics.drawImage(mainImage, 0, 0, WIDTH, HEIGHT, null);
        screenGraphics.dispose();    
    }
    
    /**
     * SINGLE PLAYER GAME AND TILE REVEAL LOGIC
     * TODO: Move game logic and revealing algorithms to server side;
     * only keep changing of tile state/type
     */
    
    public void revealTile(int x, int y) {
        if(firstClick) {
            field.populateField(x, y);
            tiles = field.getField();
            firstClick = false;
            revealTile(x,y);                        
        } else if (tiles[x][y].getState() == Tile.UNREVEALED){
            tiles[x][y].setState(Tile.REVEALED);
            tiles[x][y].fogged = false;
            
            if(tiles[x][y].getType() == Tile.BOMB) {
                revealAllBombs();
                //listenerThread.
                //mouseListener.
                int action = JOptionPane.showOptionDialog(this, "Game over!\nWould you like to play again?", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Play Again", "Return to Menu"}, null);
                switch(action){
                    case 0:
                        //TODO: Implement logic to play again
                        field = new MineField(NUM_COLUMNS, NUM_ROWS);
                        tiles = field.getField();
                        checkNeighbours = true;
                        firstClick = true;
                        break;
                    case 1:
                        //TODO: Implement logic to return to menu panel
                        break;
                    case JOptionPane.CLOSED_OPTION:
                        //TODO: Determine how to handle closing the option pane
                        break;
                    default:
                        System.err.println("Error: unknown option selected (this isn't possible... how did you even break this?");
                        break;
                }
            }
            
            if (tiles[x][y].getType() == 0){
                for (Tile t : tiles[x][y].getNeighbours()){
                    revealTile(t.getX(), t.getY());
                }
            }
        } else if (checkNeighbours && tiles[x][y].getState() == Tile.REVEALED/* && tiles[x][y].getType() != Tile.BOMB*/){
            int numFlags = 0;
            checkNeighbours = false;
            ArrayList<Tile> neighbours = tiles[x][y].getNeighbours();
                        
            for(Tile t : neighbours) {
                if(t.getState() == Tile.FLAGGED) {
                    numFlags += 1;
                }  
            }
            
            System.out.println("flags: " + numFlags);
            if (tiles[x][y].getType() == numFlags && tiles[x][y].getType() != 0){
                for (Tile t : neighbours) {
                    revealTile(t.getX(), t.getY());
                }
            }
        }
    }
    
    private void revealAllBombs() {
        for(int i = 0; i < NUM_COLUMNS; i++) {
            for(int j = 0; j < NUM_ROWS; j++) {
                if(tiles[i][j].getType() == Tile.BOMB) {
                    tiles[i][j].setState(Tile.REVEALED);
                }
            }
        }
    }
    
    public void flagTile(int x, int y) {
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
        field.assist();
    }
    
    public void toggleAssist() {
        Tile.assist = !Tile.assist;
    }
    
    public MineField getField(){
        return field;
    }
    
    private void processKeyPress(String key){
        switch(key){
            case "f":
                //FIXME: change constant
                field.generateFog(5);
        }
    }
    
    //FIXME: potentially change implementatino to further encapsulate method
    public void setField(MineField f) {
        field = f;
        tiles = field.getField();
        firstClick = false;
    }
    
    //TODO: Implement
    @Override
    public void processServerMsg(String serverMsg){
        String[] headerAndServer = serverMsg.split(" ", 2);
        String[] splitMsg;
        int tileX, tileY;
        
        switch(headerAndServer[0].trim()){
            case "/getMineField_rsp":
                splitMsg = headerAndServer[1].split(" ", 3);
                setField((MineField) Serialization.deserializeFromString(splitMsg[2].trim()));
                break;
            case "/clickAck":
                break;
            case "/keyPressAck":
                break;
            case "/updateTile":
                splitMsg = headerAndServer[1].split(" ");
                switch(splitMsg[0]){
                    case "reveal":
                        tileX = Integer.parseInt(splitMsg[1]);
                        tileY = Integer.parseInt(splitMsg[2].trim());
                        checkNeighbours = true;
                        revealTile(tileX, tileY);
                        break;
                    case "flag":
                        tileX = Integer.parseInt(splitMsg[1]);
                        tileY = Integer.parseInt(splitMsg[2].trim());
                        flagTile(tileX, tileY);
                    case "pressKey":
                        processKeyPress(splitMsg[1].trim());
                }
                break;
            default:
                System.err.println("Unknown server message; cannot process");
        }
    }
    
    @Override
    public void processEvent(String event){
        sendMessage(event);
    }
}
