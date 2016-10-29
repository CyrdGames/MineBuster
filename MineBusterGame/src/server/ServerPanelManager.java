package server;

import javax.swing.*;

public class ServerPanelManager extends JFrame {
    
    private static ServerPanelManager manager;
    private static JPanel currentPanel;
    public static final int AUTHENTICATION_PANEL = 0;
    public static final int SERVER_PANEL = 1;
    
    private ServerPanelManager() {
        super("server");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.pack();
        super.setVisible(true);
    }    
    
    public void setPanel(int panel) {
        switch(panel) {
            case AUTHENTICATION_PANEL:
                currentPanel = new ServerAuthPanel();
                break;
            case SERVER_PANEL:
                currentPanel = new ServerMainPanel();
                break;
            default:
                currentPanel = new ServerAuthPanel();
        }
        
        super.setContentPane(currentPanel);
        super.pack();
    }
    
    @Override
    public void pack() {
        setSize(1, 1);
        super.pack();
    }
    
    public static ServerPanelManager getManager() {
        if(manager == null) {
            manager = new ServerPanelManager();
        }
        
        return manager;
    }
}