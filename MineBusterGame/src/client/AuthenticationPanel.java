package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.*;

public class AuthenticationPanel extends JPanel {
    
    public AuthenticationPanel() {
        super();
        JButton create = new JButton("Create Account");
        JButton login = new JButton("Login");
        JTextField username = new JTextField("");
        JTextField password = new JTextField("");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        
        username.setColumns(20);
        password.setColumns(20);
        
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getModifiers());
                //TODO: determine constant 16
                if(e.getModifiers() == 16) {
                    System.out.println("Creating New Account");
                }
            }            
        });
        
        this.add(userLabel);
        this.add(username);
        this.add(passLabel);
        this.add(password);
        this.add(login);
        this.add(create);
    }
}
