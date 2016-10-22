package client;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientLock {
    
    public Lock lock;
    public Condition condvar;
    public byte[] message;
    
    public ClientLock(){
        this.lock = new ReentrantLock();
        this.condvar = this.lock.newCondition();
        this.message = null;
    }
}
