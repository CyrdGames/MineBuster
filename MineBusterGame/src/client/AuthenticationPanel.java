package client;

import data.Account;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class AuthenticationPanel extends JPanel {
    
    private ClientLock syncSend;
    //TODO: syncReceive may not be necessary
    private ClientLock syncReceive;
    public static Account account;
    
    public AuthenticationPanel(ClientLock syncSend, ClientLock syncReceive) {
        super();
        JButton create = new JButton("Create Account");
        JButton login = new JButton("Login");
        JTextField username = new JTextField("");
        JPasswordField password = new JPasswordField("");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        this.syncSend = syncSend;
        this.syncReceive = syncReceive;
        
        username.setColumns(20);
        password.setColumns(20);
        
        create.addActionListener((ActionEvent e) -> {
            //TODO: determine constant 16
            if(e.getModifiers() == 16) {
                account = new Account(username.getText(), password.getPassword());
                account.createNewAccount();
                //TODO: Properly implement message protocol
                sendMessage("/createAccount", username.getText(), new String(password.getPassword()));
            }            
        });
        
        login.addActionListener((ActionEvent e) -> {
            //TODO: determine constant 16
            if(e.getModifiers() == 16) {
                account = new Account(username.getText(), password.getPassword());
                if(account.login()) {
                    Client.setPanel("classic game");
                }
                //TODO: Properly implement message protocol
                sendMessage("/login", username.getText(), new String(password.getPassword()));
            }            
        });
        
        this.add(userLabel);
        this.add(username);
        this.add(passLabel);
        this.add(password);
        this.add(login);
        this.add(create);
    }
    
    private void sendMessage(String command, String username, String password){
        this.syncSend.lock.lock();
        try {
            this.syncSend.message = (command + " " + username + " " + password).getBytes();
            this.syncSend.condvar.signalAll();
        } finally {
            this.syncSend.lock.unlock();
        }
    }
}
