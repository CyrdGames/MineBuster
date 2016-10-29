package client;

import javax.swing.JPanel;

public abstract class GenericPanel extends JPanel{
    
    public ClientLock syncSend;
    
    protected void sendMessage(String message){
        syncSend.lock.lock();
        try {
            syncSend.message = message.getBytes();
            syncSend.condvar.signalAll();
        } finally {
            syncSend.lock.unlock();
        }
    }
    
    public abstract void processServerMsg(String serverMsg);
}
