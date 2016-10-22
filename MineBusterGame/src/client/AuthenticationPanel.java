package client;

import data.Account;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class AuthenticationPanel extends JPanel {
    
    public static Account account;
    
    public AuthenticationPanel() {
        super();
        JButton create = new JButton("Create Account");
        JButton login = new JButton("Login");
        JTextField username = new JTextField("");
        JPasswordField password = new JPasswordField("");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        
        username.setColumns(20);
        password.setColumns(20);
        
        create.addActionListener((ActionEvent e) -> {
            //TODO: determine constant 16
            if(e.getModifiers() == 16) {
                account = new Account(username.getText(), password.getPassword());
                account.createNewAccount();
            }            
        });
        
        login.addActionListener((ActionEvent e) -> {
            //TODO: determine constant 16
            if(e.getModifiers() == 16) {
                account = new Account(username.getText(), password.getPassword());
                if(account.login()) {
                    Client.setPanel("classic game");
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
