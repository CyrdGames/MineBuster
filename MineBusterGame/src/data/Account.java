package data;

public class Account {

    String name, password;
    boolean loggedIn = false;

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void createNewAccount() {
        Database.init();
        String[] array = Authentication.createHash(this.password).split(":");
        Database.addNewAccount(name, array[0], array[1], array[2]);
    }

    public boolean login() {
        Database.init();
        String hash = Database.getAccountInfo(name);

        if (!hash.isEmpty() && Authentication.checkPassword(password, hash)) {
            System.out.println("Welcome, " + name + ".");
            loggedIn = true;
            return true;
        } else {
            System.out.println("Go away, '" + name + "'.");
            return false;
        }
    }
}
