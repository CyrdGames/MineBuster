package data;

import client.PanelManager;
import client.SinglePlayerPanel;
import minebustergame.MineField;
import util.Serialization;

public class Account {

    String name;
    char[] password;
    String hash;
    boolean loggedIn = false;
    boolean duplicate = false;

    public Account(String name, char[] password) {
        this.name = name;
        this.password = password;
    }
    
    public Account(String name, String hash) {
        this.name = name;
        this.hash = hash;
        this.password = new char[1];
    }

    public boolean createNewAccount() {
        Database.init();
        String[] array = Authentication.createHash(password).split(":");
        duplicate = Database.addNewAccount(name, array[0], array[1], array[2]);
        clearPassword();
        return duplicate;
    }

    public boolean createNewAccount(boolean isHash) {
        Database.init();
        String[] array = hash.split(":");
        duplicate = Database.addNewAccount(name, array[0], array[1], array[2]);
        clearPassword();        
        return duplicate;
    }
    
    public boolean login() {
        Database.init();
        String hash = Database.getAccountInfo(name);

        if (!hash.isEmpty() && Authentication.checkPassword(password, hash)) {
            clearPassword();
            System.out.println("Welcome, " + name + ".");
            loggedIn = true;
            return true;
        } else {
            clearPassword();
            System.out.println("Go away, '" + name + "'.");
            return false;
        }
    }
    
    public void save() {
        Serialization.serialize(((SinglePlayerPanel) PanelManager.getMainPanel()).getField(), name);
    }
    
    public void load() {
        MineField f = (MineField)Serialization.deserialize(name);
        f.setNeighbours();
        ((SinglePlayerPanel) PanelManager.getMainPanel()).setField(f);
    }
    
    private void clearPassword() {
        for(char c : password) {
            c = (char)0;
        }
    }
}
