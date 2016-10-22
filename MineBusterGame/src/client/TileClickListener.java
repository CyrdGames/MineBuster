package client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import minebustergame.Tile;

public class TileClickListener implements MouseListener{
    
    private int numLeftClicks;
    private int numTileClicks;
    //LMB
    private int heldLeftX, heldLeftY;
    //RMB
    private int heldRightX, heldRightY;
    private GameManager manager;
    private ClientLock syncSend;
    
    public TileClickListener(){
        this.numLeftClicks = 0;
        this.heldLeftX = -1;
        this.heldLeftY = -1;
        this.heldRightX = -1;
        this.heldRightY = -1;
        this.numTileClicks = 0;
    }
    
    public TileClickListener(GameManager manager, ClientLock syncSend){
        this();
        this.manager = manager;
        this.syncSend = syncSend;
    }
    
    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    
    @Override
    public void mousePressed(MouseEvent e){
        System.out.println("Mouse PRESS detected");
        if (e.getButton() == MouseEvent.BUTTON1){
            this.heldLeftX = e.getX();
            this.heldLeftY = e.getY();
        } else if (e.getButton() == MouseEvent.BUTTON3){
            this.heldRightX = e.getX();
            this.heldRightY = e.getY();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        System.out.println("Mouse RELEASE detected");
        //boolean tileClicked;
        
        if (e.getButton() == MouseEvent.BUTTON1){
            if ( checkTileClick(e.getX(), e.getY(), this.heldLeftX, this.heldLeftY) ){
                int[] tile = determineTile(e.getX(), e.getY());
                sendMessage(e.getButton(), tile);
                this.manager.checkNeighbours = true;
                this.manager.revealTile(tile[0], tile[1]);
            }
            this.heldLeftX = -1;
            this.heldLeftY = -1;
        } else if (e.getButton() == MouseEvent.BUTTON3){
            if ( checkTileClick(e.getX(), e.getY(), this.heldRightX, this.heldRightY) ){
                int[] tile = determineTile(e.getX(), e.getY());
                sendMessage(e.getButton(), tile);
                this.manager.flagTile(tile[0], tile[1]);
//                this.manager.updateTileState(tile[0], tile[1], GameManager.FLAGGED);
            }
            this.heldRightX = -1;
            this.heldRightY = -1;
        } else if (e.getButton() == MouseEvent.BUTTON2){
            Tile.TILE_SIZE += 1;
        }
        int[] tile = determineTile(e.getX(), e.getY());
        System.out.println("Tile [" + tile[0] + "][" + tile[1] + "] pressed");
    }
    
    private boolean checkTileClick(int buttonX, int buttonY, int pressedX, int pressedY){
        int minX = pressedX - pressedX % Tile.TILE_SIZE;
        int maxX = minX + (Tile.TILE_SIZE - 1);
        int minY = pressedY - pressedY % Tile.TILE_SIZE;
        int maxY = minY + (Tile.TILE_SIZE - 1);
        if (buttonX >= minX && buttonX <= maxX && buttonY >= minY && buttonY <= maxY){
            System.out.println("Mouse pressed and released within same tile bound; increasing number of clicks");
            this.numTileClicks++;
            return true;
        }
        return false;
    }
    
    private int[] determineTile(int buttonX, int buttonY){
        int[] tile = new int[2];
        tile[0] = buttonX/Tile.TILE_SIZE;
        tile[1] = buttonY/Tile.TILE_SIZE;
        return tile;
    }
    
    private void sendMessage(int mouseButton, int[] tile){
        this.syncSend.lock.lock();
        try {
            this.syncSend.message = ("/click " + mouseButton + " " + tile[0] + " " + tile[1]).getBytes();
            this.syncSend.condvar.signalAll();
        } finally {
            this.syncSend.lock.unlock();
        }
    }
    
}
