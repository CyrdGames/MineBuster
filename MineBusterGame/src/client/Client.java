package client;

import javax.swing.JFrame;

public class Client {
    public static GamePanel game = new GamePanel();
    public static AuthenticationPanel loginPanel = new AuthenticationPanel();
    public static JFrame mainFrame = new JFrame("MineBuster - Login");
    
    public static void runClient() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setContentPane(loginPanel);
                mainFrame.setSize(2160, 1080);
                mainFrame.setVisible(true);
                mainFrame.pack();
            }
        });
    }
    
    public static void setPanel(String panel) {
        switch(panel) {
            case "classic game":
                mainFrame.setContentPane(game);
                break;
            default:
        }
        
        mainFrame.setSize(5,5);
        mainFrame.pack();
    }
    
    public static void main(String[] args){
        runClient();
    }
}
