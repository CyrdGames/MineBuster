package server;

import javax.swing.JPanel;

public class ServerPanel extends JPanel {
    
    protected static ServerPanelManager manager;
    
    public ServerPanel() {
        super();
        manager = ServerPanelManager.getManager();
    }    
}
