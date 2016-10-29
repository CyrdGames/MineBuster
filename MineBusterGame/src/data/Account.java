package data;

import client.PanelManager;
import client.SinglePlayerPanel;
import minebustergame.MineField;
import util.Serialization;

public class Account {

    String name;
    char[] password;
    boolean loggedIn = false;

    public Account(String name, char[] password) {
        this.name = name;
        this.password = password;
    }

    public void createNewAccount() {
        Database.init();
        String[] array = Authentication.createHash(password).split(":");
        Database.addNewAccount(name, array[0], array[1], array[2]);
        clearPassword();
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
            c = 0;
        }
    }
}
