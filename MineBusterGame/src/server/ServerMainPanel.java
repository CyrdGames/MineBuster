package server;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

public class ServerMainPanel extends JPanel {
    
    private final JScrollPane connections;
    public ArrayList<JPanel> panels = new ArrayList();
    private final JPanel mainPanel = new JPanel();
    
    public ServerMainPanel() {
        super();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        connections = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));
        panel.setMaximumSize(new Dimension(1080, 30));
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.add(new JLabel("ID"));
        panel.add(new JLabel("Port #"));
        panel.add(new JLabel("IP Address"));
        panel.add(new JLabel(""));
        
        mainPanel.add(panel);
    }
    
    public JScrollPane getPanel() {
        return connections;
    }
    
    public void addPanel(ServerThread client) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));
        panel.setMaximumSize(new Dimension(1080, 30));
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.add(new JLabel(Integer.toString(client.id)));
        panel.add(new JLabel(Integer.toString(client.port)));
        panel.add(new JLabel(client.address.getHostAddress()));
        
        JButton disconnect = new JButton("Disconnect");
        
        panel.add(disconnect);
        
        panels.add(panel);
        panel.setName(Integer.toString(client.id));
        System.out.println(panel.getName());
        mainPanel.add(panel);
    }
}
