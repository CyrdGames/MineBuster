package server;

import java.awt.event.ActionEvent;
import javax.swing.*;

public class ServerAuthPanel extends JPanel {
    
    public ServerAuthPanel() {
        super();
        
        JPasswordField pass = new JPasswordField("");
        pass.setColumns(40);
        
        JButton login = new JButton("Login");
        
        login.addActionListener((ActionEvent e) -> {
            //TODO: determine constant 16
            if(e.getModifiers() == 16) {
                Server.adminLogin(pass.getPassword());
            }            
        });

        this.add(pass);
        this.add(login);
    }
}