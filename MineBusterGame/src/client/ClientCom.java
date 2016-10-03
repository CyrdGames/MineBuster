package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCom {
    
    InetAddress address;
    int port = 4445;
    DatagramSocket socket;
    Scanner in = new Scanner(System.in);
    
    public ClientCom() {
        
        try {
            this.address = InetAddress.getByName("192.168.12.108");
            this.socket = new DatagramSocket();
            
            while(true) {
                byte[] data = in.nextLine().getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
                
                System.out.println("Sent");
                                
                packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                
                System.out.println("Received: " + received);
                
            }
            
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(ClientCom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
