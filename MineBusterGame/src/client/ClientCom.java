package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCom extends Thread{
    
    private InetAddress address;
    private int port = 4445;
    private DatagramSocket socket;
    private ClientLock syncSend;
    private ClientLock syncReceive;
    
    public ClientCom(ClientLock syncSend, ClientLock syncReceive) {
            this.syncSend = syncSend;
            this.syncReceive = syncReceive;
    }
    
    @Override
    public void run(){
        try {
            this.address = InetAddress.getByName("192.168.3.194");
            this.socket = new DatagramSocket();
            
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //TODO: Arbitrary length of 100; determine appropriate size later
                    syncReceive.message = new byte[100];
                    try{
                        while (true){
                                
                            //Receive data packet from server
                            DatagramPacket packet = new DatagramPacket(syncReceive.message, syncReceive.message.length);
                            socket.receive(packet);
                            
                            System.out.println("Port: " + socket.getPort());
//                            port = socket.getPort();
                            
                            //TODO: Fix up prints and variable creation
                            String received = new String(packet.getData(), 0, packet.getLength());
                            System.out.println("Received server message: " + received);
                            
                            received = new String(syncReceive.message, 0, syncReceive.message.length);
                            System.out.println("Received server message: " + received);

                            syncReceive.lock.lock();
                            try{
                                String[] message = received.split(" ");
                                if (message[0].equals("/setport")){
                                    port = Integer.parseInt(message[1].trim());
                                }
                                syncReceive.condvar.signalAll();
                            } finally {
                                syncReceive.lock.unlock();
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ClientCom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            while(true) {
                syncSend.lock.lock();
                try {
                    while (syncSend.message == null){
                        syncSend.condvar.await();
                    }
                    
                    System.out.println("Sync Send Condvar: " + new String(syncSend.message));
                    
                    //Send data packet to server
                    DatagramPacket packet = new DatagramPacket(syncSend.message, syncSend.message.length, address, port);
                    socket.send(packet);
                    
                    System.out.println("Sent message request");
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientCom.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    syncSend.message = null;
                    syncSend.lock.unlock();
                }
            }
            
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(ClientCom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientCom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
