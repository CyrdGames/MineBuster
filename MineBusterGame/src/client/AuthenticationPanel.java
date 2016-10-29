package client;

import data.Account;
import data.Authentication;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class AuthenticationPanel extends GenericPanel {
    
    private String tempUsername;
    private char[] tempPassword;
    
    public AuthenticationPanel(ClientLock syncSend) {
        super();
        JButton create = new JButton("Create Account");
        JButton login = new JButton("Login");
        JTextField username = new JTextField("");
        JPasswordField password = new JPasswordField("");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        this.syncSend = syncSend;
        
        username.setColumns(20);
        password.setColumns(20);
        
        create.addActionListener((ActionEvent e) -> {
            //FIXME: determine constant 16
            if(e.getModifiers() == 16) {
//                PanelManager.initAccount(username.getText(), password.getPassword());
//                PanelManager.getAccount().createNewAccount();
                sendMessage("/createAccount " + username.getText() + " " + Authentication.createHash(password.getPassword()));
            }            
        });
        
        login.addActionListener((ActionEvent e) -> {
            //FIXME: determine constant 16
            if(e.getModifiers() == 16) {
                //FIXME: Temporary account disabling; uncomment after refactoring is complete
//                PanelManager.initAccount(username.getText(), password.getPassword());
//                if(PanelManager.getAccount().login()) {
//                    PanelManager.setPanel(PanelManager.SINGLE_PLAYER_CLASSIC);
//                }
                tempUsername = username.getText();
                tempPassword = password.getPassword();
                if (tempUsername.length() == 0 && tempPassword.length == 0){
                    PanelManager.setPanel(PanelManager.SINGLE_PLAYER_CLASSIC);
                } else{
                    sendMessage("/loginUser " + tempUsername);
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
    
    @Override
    public void processServerMsg(String serverMsg){
        String[] headerAndServer = serverMsg.split(" ", 2);
        String[] splitMsg;
        //FIXME: sending message in receive message thread currently; change implementation
        switch(headerAndServer[0].trim()){
            case "/loginUser_rsp":
                //FIXME: temporary
                splitMsg = headerAndServer[1].split(" ");
                splitMsg[0] = splitMsg[0].trim();
                splitMsg[1] = splitMsg[1].trim();
                sendMessage("/loginPass " + tempUsername + " " + Authentication.createHash(tempPassword, splitMsg[0], Integer.parseInt(splitMsg[1])));
                break;
            case "/loginPass_rsp":
                //FIXME: temporary
                String response = headerAndServer[1].trim();
                if (response.equals("success")){
                    PanelManager.setPanel(PanelManager.COOP_CLASSIC);
                } else {
                    System.out.println("response: " + response);
                }
                break;
            default:
                System.err.println("Unknown server message; cannot process");
        }
    }
}
