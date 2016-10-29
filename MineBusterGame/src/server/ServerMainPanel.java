package server;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

public class ServerMainPanel extends ServerPanel {
    
    private final JScrollPane connections;
    public static ArrayList<JPanel> panels = new ArrayList();
    
    private ServerCom network;
    
    public ServerMainPanel() {
        super();
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        connections = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));
        panel.setMaximumSize(new Dimension(1080, 30));
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.add(new JLabel("ID"));
        panel.add(new JLabel("Port #"));
        panel.add(new JLabel("IP Address"));
        panel.add(new JLabel(""));
        
        super.add(panel);
        
        try {
            network = new ServerCom(this);
            network.start();
        } catch (SocketException | UnknownHostException ex) {
        }
    }
    
    public JScrollPane getPanel() {
        return connections;
    }
    
    public void addPanel(GroupCom client) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));
        panel.setMaximumSize(new Dimension(1080, 30));
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.add(new JLabel(Integer.toString(client.id)));
        panel.add(new JLabel("" + client.port + " - " + client.thisport));
        panel.add(new JLabel(client.address.getHostAddress()));
        
        JButton disconnect = new JButton("Disconnect");
        
        disconnect.addActionListener((ActionEvent e) -> {
            if(e.getModifiers() == 16) {
                client.connected = false;
                panels.remove(panel);
                super.remove(panel);
                manager.pack();
            }            
        });
                
        panel.add(disconnect);
        
        panels.add(panel);
        panel.setName(Integer.toString(client.id));
        super.add(panel);
        manager.pack();
    }
}
