package client;

public abstract class GamePanel extends GenericPanel implements Runnable {
    
    public boolean running;
    
    protected abstract void draw();
    public abstract void processEvent(String event);
}
