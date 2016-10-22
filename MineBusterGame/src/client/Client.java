package client;

import javax.swing.JFrame;

public class Client {
    
    public static ClientLock syncSend;
    public static ClientLock syncReceive;
    public static ClientCom clientCom;
    public static GamePanel game;
    public static AuthenticationPanel loginPanel;
    public static JFrame mainFrame;
    
    public static void runClient() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                clientCom.start();
                JFrame mainFrame = new JFrame("MineBuster - Login");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setContentPane(loginPanel);
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
        syncSend = new ClientLock();
        syncReceive = new ClientLock();
        clientCom = new ClientCom(syncSend, syncReceive);
        game = new GamePanel(syncSend, syncReceive);
        loginPanel = new AuthenticationPanel(syncSend, syncReceive);
        mainFrame = new JFrame("MineBuster - Login");
        runClient();
    }
}
