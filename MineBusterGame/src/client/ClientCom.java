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
    private int numBytes;
    
    public ClientCom(ClientLock syncSend) {
            this.syncSend = syncSend;
            numBytes = 512;
    }
    
    @Override
    public void run(){
        try {
            this.address = InetAddress.getByName("192.168.12.109");
            this.socket = new DatagramSocket();
            
            performConnectionHandshake();
            
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //TODO: Arbitrary length of 100; determine appropriate size later
                    byte[] message;
                    String strMsg;
                    try{
                        while (true){
                                
                            message = new byte[numBytes];
                            
                            //Receive data packet from server
                            DatagramPacket packet = new DatagramPacket(message, message.length);
                            System.out.println("Receiving........");
                            socket.receive(packet);
                            
                            System.out.println("Port: " + socket.getPort());
                            
                            //TODO: Fix up prints and variable creation
                            strMsg = new String(packet.getData(), 0, packet.getLength());
                            System.out.println("Received server message (packet format): " + strMsg);
                            
                            strMsg = new String(message, 0, message.length);
                            System.out.println("Received server message (message format): " + strMsg);
                            
                            if (strMsg.startsWith("/loginPass_rsp success")){
                                numBytes = 262144;
                            }
                            else if (strMsg.startsWith("/getMineField_rsp")){
                                System.out.println("DOING MINEFIELD STUFF");
                                String[] splitMsg = strMsg.split(" ", 4);
                                GroupComReceive groupCom = new GroupComReceive(socket, Integer.parseInt(splitMsg[1]), splitMsg[2]);
                                groupCom.start();
                                numBytes = 512;
                            }
                            PanelManager.getMainPanel().processServerMsg(strMsg);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ClientCom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            DatagramPacket packet;
            
            while(true) {
                syncSend.lock.lock();
                try {
                    while (syncSend.message == null){
                        syncSend.condvar.await();
                    }
                    
                    System.out.println("Sync Send Condvar: " + new String(syncSend.message));
                    
                    //Send data packet to server
                    packet = new DatagramPacket(syncSend.message, syncSend.message.length, address, port);
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
    
    public void performConnectionHandshake() throws IOException{
        byte[] message = "/requestPort".getBytes();
        String[] splitMsg;
        
        System.out.println("Sending port request...");
        
        DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
        socket.send(packet);
        
        System.out.println("Port request sent");
        
        message = new byte[100];
        packet = new DatagramPacket(message, message.length, address, port);
        socket.receive(packet);
        splitMsg = (new String(message)).split(" ");
        
        System.out.println("received message: " + new String(message));
        
        if (splitMsg[0].equals("/setPort")){
            port = Integer.parseInt(splitMsg[1].trim());
            System.out.println("Set port to " + port);
        } else {
            System.err.println("Error: Connection Handshake failed");
            return;
        }
        
        message = "/confirmPort".getBytes();
        packet = new DatagramPacket(message, message.length, address, port);
        socket.send(packet);
        
        message = new byte[100];
        packet = new DatagramPacket(message, message.length, address, port);
        socket.receive(packet);
        splitMsg = (new String(message)).split(" ");
        
        System.out.println("received message: " + new String(message));
        
        if (!splitMsg[0].startsWith("/confirmConnection")){
            System.err.println("Error: Connection Handshake failed");
            return;
        }
        
        message = "/ackResponse".getBytes();
        packet = new DatagramPacket(message, message.length, address, port);
        socket.send(packet);
        
        message = "random message iz here".getBytes();
        packet = new DatagramPacket(message, message.length, address, port);
        socket.send(packet);
    }
    
}
