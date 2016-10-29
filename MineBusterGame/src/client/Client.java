package client;

import javax.swing.JFrame;

public class Client {
    
    public static ClientLock syncSend;
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
                PanelManager.init(mainFrame, syncSend);
                PanelManager.setPanel(PanelManager.LOGIN);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
            }
        });
    }
    
    
    
    public static void main(String[] args){
        syncSend = new ClientLock();
        clientCom = new ClientCom(syncSend);
        game = new SinglePlayerPanel(syncSend);
        loginPanel = new AuthenticationPanel(syncSend);
        mainFrame = new JFrame("MineBuster");
        runClient();
    }
}
