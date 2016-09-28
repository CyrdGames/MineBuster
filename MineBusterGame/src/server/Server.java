package server;

import data.Account;
import data.Authentication;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

class GUI {

    JFrame frame;
    JTextField user, pass;
    JButton create, login;
    JPanel panel;

    public class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name, pw;
            Account account;

            switch (e.getActionCommand()) {
                case "Create Account":
                    name = user.getText();
                    pw = pass.getText();
                    account = new Account(name, pw);
                    account.createNewAccount();
                    break;
                case "Login":
                    name = user.getText();
                    pw = pass.getText();
                    account = new Account(name, pw);
                    account.login();
                    break;
            }
        }
    }

    public void runGUI() {
        frame = new JFrame("server");

        user = new JTextField("Username");
        pass = new JTextField("Password");

        create = new JButton("Create Account");
        create.addActionListener(new Listener());

        login = new JButton("Login");
        login.addActionListener(new Listener());

        panel = new JPanel();

        panel.add(user);
        panel.add(pass);

        panel.add(create);
        panel.add(login);

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

public class Server {

    public static void run() {
        GUI gui = new GUI();
        gui.runGUI();
    }

    public static void main(String[] args) {
        run();

        Scanner in = new Scanner(System.in);

        String pw = in.nextLine();
        adminLogin(pw);
    }

    public static void adminLogin(String password) {
        if (Authentication.checkPassword(password, "21716:/PKxMm2iPkZzMVJ5jY/as701bHkgfbq+5p6PYLEO+PA=:F0Xio9HscZ7YrcNJglEfjcwectP7nfbTBIHCtK12DYY=")) {
            System.out.println("Access granted");
        } else {
            System.out.println("go away");
        }
    }
}
