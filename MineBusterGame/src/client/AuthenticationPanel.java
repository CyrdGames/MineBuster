package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.*;

public class AuthenticationPanel extends JPanel {
    
    private ClientLock syncSend;
    //TODO: syncReceive may not be necessary
    private ClientLock syncReceive;
    
    public AuthenticationPanel(ClientLock syncSend, ClientLock syncReceive) {
        super();
        JButton create = new JButton("Create Account");
        JButton login = new JButton("Login");
        JTextField username = new JTextField("");
        JTextField password = new JTextField("");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        this.syncSend = syncSend;
        this.syncReceive = syncReceive;
        
        username.setColumns(20);
        password.setColumns(20);
        
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: determine constant 16
                if(e.getModifiers() == 16) {
                    sendMessage("testUsername", "testPassword");
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
    
    private void sendMessage(String username, String password){
        this.syncSend.lock.lock();
        try {
            this.syncSend.message = ("/createAccount " + username + " " + password).getBytes();
            this.syncSend.condvar.signalAll();
        } finally {
            this.syncSend.lock.unlock();
        }
    }
}
