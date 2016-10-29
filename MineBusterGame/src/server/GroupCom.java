package server;

import data.Account;
import data.Authentication;
import data.Database;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import util.Serialization;

public class GroupCom extends Thread {    
    DatagramSocket socket;
    DatagramPacket packet;
    BufferedReader in;
    InetAddress address;
    String received;
    int port, thisport;
    int id;
    byte[] bytes;
    boolean connected;
    
    ServerMainPanel parent;
    
    public GroupCom(DatagramSocket s, DatagramPacket p, int po, int idNumber, ServerMainPanel parent) {
        socket = s;
        packet = p;
        id = idNumber;
        address = packet.getAddress();
        port = packet.getPort();
        thisport = po;
        this.parent = parent;
    }
        
    @Override
    public void run() {        
        try {
            System.out.println("Starting client.");
                    
            connected = handshake();
            
            if(connected) {
                parent.addPanel(this);
            }
            
            while(connected) {
                bytes = new byte[512];
                packet = new DatagramPacket(bytes, bytes.length);
                socket.receive(packet);

                received = new String(packet.getData(), 0, packet.getLength());

                System.out.println("Client #" + id + " received: " + received);

                address = packet.getAddress();
                port = packet.getPort();

                bytes = process(received).getBytes();

                packet = new DatagramPacket(bytes, bytes.length, address, port);
                socket.send(packet);
            } 
            
            System.out.println("Client " + id + " has disconnected.");
        } catch(FileNotFoundException e) {
        } catch (IOException ex) {
        }        
    }
    
    private String process(String message) {
        String[] msg = message.split(" ");
        
        String str = "";
        
        switch(msg[0]) {
            case "/createAccount":
                str += "/createAccount_rsp";
                Account account = new Account(msg[1], msg[2]);
                boolean success = account.createNewAccount(connected);
                if(success) {
                    str += " success";
                } else {
                    str += " failure";
                }
                break;
            case "/loginUser":
                str += "/loginUser_rsp";
                Database.init();
                String hash = Database.getAccountInfo(msg[1]);
                str += " " + hash.split(":")[1];
                str += " " + hash.split(":")[0];
                break;
            case "/loginPass":
                str += "/loginPass_rsp";
                Database.init();
                String passHash = Database.getAccountInfo(msg[1]);
                if(passHash.split(":")[2].equals(msg[2])) {
                    str += " success";
                } else {
                    str += " failure";
                }
                break;
            case "/getMineField":
                str += "/getMineField_rsp 6789 228.5.6.7 ";
                str += Serialization.serializeToString(ServerGameManager.field);
                break;
            case "/click":
                str += "/updateTile";
                if("left".equals(msg[1])) {
                    ServerGameManager.revealTile(Integer.parseInt(msg[2]), Integer.parseInt(msg[3]));
                    str += " reveal " + msg[2] + " " + msg[3];
                } else {
                    ServerGameManager.flagTile(Integer.parseInt(msg[2]), Integer.parseInt(msg[3]));
                    str += " flag " + msg[2] + " " + msg[3];
                }
                
                ServerCom.multicast(str);
                return "/clickAck";
            case "/keyPress":
                str += "/updateTile pressKey";
                str += " " + msg[1];
                ServerCom.multicast(str);
                return "keyPressAck";
        }
        
        System.out.println("Client #" + id + " sending: " + str);
        
        return str;
    }
    
    
    private boolean handshake() throws IOException {
        bytes = new byte[512];
        packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        
        received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("received: " + received);
        if(!received.toLowerCase().startsWith("/confirmport")) {
            return false;
        }
        System.out.println("confirm port");
        bytes = ("/confirmConnection").getBytes();
        packet = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(packet);
        
        bytes = new byte[512];
        packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        
        received = new String(packet.getData(), 0, packet.getLength());
        
        return "/ackresponse".equals(received.toLowerCase());        
    }    
}
