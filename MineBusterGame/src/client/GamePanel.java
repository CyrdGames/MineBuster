package client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    
    private GameManager manager;
    private Graphics2D graphics;
    private BufferedImage mainImage;
    private TileClickListener mouseListener;
    public Thread listenerThread;
    
    private boolean running;
    
    public final static int WIDTH = 960;
    public final static int HEIGHT = 512;
    
    public GamePanel(){
        super();
        this.setFocusable(true);
        this.requestFocus();
        System.out.println("init");
        this.manager = new GameManager(this);
        this.mainImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.graphics = (Graphics2D) this.mainImage.getGraphics();
        this.running = true;
        this.mouseListener = new TileClickListener(this.manager);
        this.listenerThread = null;
    }
    
    @Override
    public void addNotify(){
        super.addNotify();
        if(this.listenerThread == null){
            System.out.println("ADDNOTIFY CALLED");
            addMouseListener(mouseListener);
            this.listenerThread = new Thread(this);
            this.listenerThread.start();
            System.out.println(listenerThread);
        }
    }
    
    @Override 
    public void run(){
        //this.init();
        
        System.out.println("Running:");
        while (running){
            //TODO: update
            this.manager.draw(this.graphics);
            this.drawToScreen();
        }
        
    }
    
//    private void init(){
//        System.out.println("init");
//        this.manager = new GameManager();
//        this.mainImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
//        this.graphics = (Graphics2D) this.mainImage.getGraphics();
//        this.running = true;
//        //this.mouseListener = new BobListener(this.manager);
//        this.listenerThread = null;
//    }
    
    private void drawToScreen(){
        Graphics screenGraphics = super.getGraphics();
        screenGraphics.drawImage(mainImage, 0, 0, WIDTH, HEIGHT, null);
        screenGraphics.dispose();
    }
}
