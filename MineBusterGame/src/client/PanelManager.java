package client;

import data.Account;
import javax.swing.JFrame;

public class PanelManager {
    
    public final static int LOGIN = 0;
    public final static int SINGLE_PLAYER_CLASSIC = 1;
    public final static int COOP_CLASSIC = 2;
    
    private static JFrame mainFrame;
    private static ClientLock syncSend;
    private static GenericPanel mainPanel;
    private static Account account;
    
    public static void init(JFrame mainF, ClientLock send){
        mainFrame = mainF;
        syncSend = send;
        mainPanel = null;
        account = null;
    }
    
    public static GenericPanel getMainPanel(){
        return mainPanel;
    }
    
    public static void setPanel(int panel) {
        System.out.println("setting panel");
        switch(panel) {
            case LOGIN:
                System.out.println("authentication panel selected");
                mainPanel = new AuthenticationPanel(syncSend);
                mainFrame.setContentPane(mainPanel);
                mainFrame.setTitle("MineBuster - Login");
                break;
            case SINGLE_PLAYER_CLASSIC:
                System.out.println("single player classic panel selected");
                mainPanel = new SinglePlayerPanel(syncSend);
                mainFrame.setContentPane(mainPanel);
                mainFrame.setTitle("MineBuster - Single Player Classic");
                break;
            case COOP_CLASSIC:
                //FIXME: Currently single player classic
                System.out.println("coop classic panel selected");
                mainPanel = new CoopClassicPanel(syncSend);
                mainFrame.setContentPane(mainPanel);
                mainFrame.setTitle("MineBuster - Coop Classic");
                break;
            default:
                System.err.print("Error: invalid panel selection");
        }
        
        //TODO: Replace with specific requirements for specific panels
        mainFrame.setSize(5,5);
        mainFrame.pack();
    }
    
    public static Account getAccount(){
        return account;
    }
    
    public static void initAccount(String username, char[] password){
        account = new Account(username, password);
    }
}