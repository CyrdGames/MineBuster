package client;

import javax.swing.JFrame;

public class Client {
    
    public static void runClient() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientLock syncSend = new ClientLock();
                ClientLock syncReceive = new ClientLock();
                ClientCom clientCom = new ClientCom(syncSend, syncReceive);
                clientCom.start();
                GamePanel game = new GamePanel(syncSend, syncReceive);
                AuthenticationPanel loginPanel = new AuthenticationPanel(syncSend, syncReceive);
                
                JFrame mainFrame = new JFrame("MineBuster - Login");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setContentPane(loginPanel);
                mainFrame.setVisible(true);
                mainFrame.pack();
            }
        });
    }
    
    public static void main(String[] args){
        runClient();
    }
}
