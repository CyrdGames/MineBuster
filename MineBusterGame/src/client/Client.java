package client;

import javax.swing.JFrame;

public class Client {
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                JFrame mainFrame = new JFrame("MineBuster");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(960, 512);
                mainFrame.setResizable(false);

                GamePanel mainPanel = new GamePanel();
                
                mainFrame.setContentPane(mainPanel);
                System.out.println("added");
                mainFrame.setVisible(true);
            }
        });
    }
}
