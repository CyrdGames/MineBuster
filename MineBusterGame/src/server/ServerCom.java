package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCom extends Thread {
    
    private static DatagramSocket mainSocket;
    private static int id = 0;
    private static ArrayList<Integer> ports = new ArrayList();
    private static ArrayList<GroupCom> clients = new ArrayList();
    ServerMainPanel parent;
    public static ServerGameManager manager;
    public static InetAddress group;
    public static MulticastSocket s;
    byte[] server = new byte[512];
    
    public ServerCom(ServerMainPanel parent) throws SocketException, UnknownHostException {
        mainSocket = new DatagramSocket(4445);        
        System.out.println(InetAddress.getLocalHost());
        this.parent = parent;
        group = InetAddress.getByName("228.5.6.7");
        try {
            s = new MulticastSocket(6789);
            s.joinGroup(group);
        } catch (IOException ex) {
        }
        

    }
    
    public static void multicast(String st) {
        DatagramPacket p = new DatagramPacket(st.getBytes(), st.length(), group, 6789);
        try {
            s.send(p);
        } catch (IOException ex) {
        }
    }
    
    @Override
    public void run() {
        while(true) {
            manager = new ServerGameManager();
            
            try {
                DatagramPacket packet = new DatagramPacket(server, server.length);
                int port = generateNewPort();
                System.out.println("receiving");
                mainSocket.receive(packet);
                
                String received = new String(packet.getData(), 0, packet.getLength());
                if(!received.toLowerCase().startsWith("/requestport")) {
                    continue;
                }
                System.out.println(new String(packet.getData(), 0, packet.getLength()));
                
                server = ("/setPort " + Integer.toString(port)).getBytes();
                
                System.out.println("Sending: " + Arrays.toString(server));
                
                packet = new DatagramPacket(server, server.length, packet.getAddress(), packet.getPort());
                mainSocket.send(packet);
                                
                DatagramSocket clientSocket = new DatagramSocket(port);
                
                GroupCom client = new GroupCom(clientSocket, packet, port, id += 1, parent);
//                
                Thread clientThread = new Thread(client);
                clientThread.start();                
//                
                clients.add(client);
            } catch(Exception e) {
                System.out.println("error");
            }
        }   
    }
    
    public static int generateNewPort() {
        Random random = new Random();
        
        int rand = 1500;
        
        while(true) {
            rand = random.nextInt(8500) + 1500;
            if(!ports.contains(rand)) {
                ports.add(rand);
                break;
            }
        }
        System.out.println(rand);
        return rand;
    }
}
