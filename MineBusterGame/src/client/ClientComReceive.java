package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientComReceive extends Thread{
    
    
    
//    @Override
//    public void run() {
//        //TODO: Arbitrary length of 100; determine appropriate size later
//        byte[] message = new byte[100];
//        try{
//            while (true){
//
//                //Receive data packet from server
//                DatagramPacket packet = new DatagramPacket(message, message.length);
//                socket.receive(packet);
//
//                System.out.println("Port: " + socket.getPort());
////                            port = socket.getPort();
//
//                //TODO: Fix up prints and variable creation
//                String received = new String(packet.getData(), 0, packet.getLength());
//                System.out.println("Received server message: " + received);
//
//                received = new String(syncReceive.message, 0, syncReceive.message.length);
//                System.out.println("Received server message: " + received);
//
//                syncReceive.lock.lock();
//                try{
//                    String[] message = received.split(" ");
//                    if (message[0].equals("/setport")){
//                        port = Integer.parseInt(message[1].trim());
//                    }
//                    syncReceive.condvar.signalAll();
//                } finally {
//                    syncReceive.lock.unlock();
//                }
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(ClientCom.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
