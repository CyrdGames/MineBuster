package client;

import javax.swing.JFrame;
import minebustergame.Tile;

public class Client {
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {                
                ClientCom c = new ClientCom();
                JFrame mainFrame = new JFrame("MineBuster");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(Tile.TILE_SIZE * GameManager.NUM_COLUMNS, Tile.TILE_SIZE * GameManager.NUM_ROWS);
                mainFrame.setContentPane(new GamePanel());
                mainFrame.pack();
                mainFrame.setVisible(true);
            }
        });
    }
}
