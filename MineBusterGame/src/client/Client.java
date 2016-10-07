package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import minebustergame.Tile;

public class Client {
    
    public static void runClient() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {                
//                ClientCom c = new ClientCom();
                GamePanel game = new GamePanel();
                AuthenticationPanel loginPanel = new AuthenticationPanel();
                
                JFrame mainFrame = new JFrame("MineBuster - Login");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                mainFrame.setSize(Tile.TILE_SIZE * GameManager.NUM_COLUMNS, Tile.TILE_SIZE * GameManager.NUM_ROWS);
                mainFrame.setContentPane(loginPanel);
//                mainFrame.setSize(2160, 1080);
                mainFrame.setVisible(true);
                mainFrame.pack();
            }
        });
    }
    
    public static void main(String[] args){
        runClient();
    }
}
