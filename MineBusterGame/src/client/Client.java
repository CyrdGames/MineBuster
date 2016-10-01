package client;

import javax.swing.JFrame;

public class Client {
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {                
                JFrame mainFrame = new JFrame("MineBuster");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(GameManager.TILE_SIZE * GameManager.WIDTH, GameManager.TILE_SIZE * GameManager.HEIGHT);
                mainFrame.setContentPane(new GamePanel());
                mainFrame.setVisible(true);
            }
        });
    }
}
