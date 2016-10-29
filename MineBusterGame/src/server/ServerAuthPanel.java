package server;

import data.Authentication;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class ServerAuthPanel extends ServerPanel {
    
    public ServerAuthPanel() {
        super();
        
        JPasswordField pass = new JPasswordField("");
        pass.setColumns(40);
        JButton login = new JButton("Login");
        
        login.addActionListener((ActionEvent e) -> {
            if(e.getModifiers() == 16) {
                adminLogin(pass.getPassword());
            }            
        });

        super.add(pass);
        super.add(login);
        super.setPreferredSize(new Dimension(600, 40));
    }    
    
    private static void adminLogin(char[] password) {
        if (Authentication.checkPassword(password, "21716:/PKxMm2iPkZzMVJ5jY/as701bHkgfbq+5p6PYLEO+PA=:F0Xio9HscZ7YrcNJglEfjcwectP7nfbTBIHCtK12DYY=")) {
            System.out.println("Access granted");
            manager.setPanel(ServerPanelManager.SERVER_PANEL);
        } else {
            System.out.println("go away");
        }
    }
}