package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupComReceive extends Thread{
    
    private DatagramSocket globalSocket;
    private MulticastSocket groupSocket;
    
    private int port;
    private InetAddress groupId;
    
    public GroupComReceive(DatagramSocket globalSocket, int port, String groupId){
        this.globalSocket = globalSocket;
        this.port = port;
        try {
            this.groupId = InetAddress.getByName(groupId);
        } catch (UnknownHostException ex) {
            Logger.getLogger(GroupComReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        try {
            groupSocket = new MulticastSocket(port);
            groupSocket.joinGroup(groupId);
            
            byte[] message;
            
            while (true){
                
                String strMsg;
                message = new byte[512];

                //Receive data packet from server
                DatagramPacket packet = new DatagramPacket(message, message.length);
                System.out.println("Receiving........");
                groupSocket.receive(packet);

                System.out.println("Port: " + groupSocket.getPort());

                //TODO: Fix up prints and variable creation
                strMsg = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received group's server message (packet format): " + strMsg);

                strMsg = new String(message, 0, message.length);
                System.out.println("Received group's server message (message format): " + strMsg);

                if (strMsg.startsWith("/getMinefield_rsp")){
                    String[] splitMsg = strMsg.split(" ", 4);
                    GroupComReceive groupCom = new GroupComReceive(groupSocket, Integer.parseInt(splitMsg[1]), splitMsg[2]);
                    groupCom.start();
                }
                PanelManager.getMainPanel().processServerMsg(strMsg);
            }
        } catch (IOException ex) {
            Logger.getLogger(GroupComReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
