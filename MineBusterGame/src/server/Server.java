package server;

import data.Authentication;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

class GUI {
    public JFrame frame;
    JTextField pass;
    JButton login;
    JPanel panel;

    public class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            char[] pw;

            switch (e.getActionCommand()) {
                case "Login":
                    pw = pass.getText().toCharArray();
                    Server.adminLogin(pw);
                    break;
            }
        }
    }

    public void runGUI() {
        frame = new JFrame("server");
        frame.add(new ServerAuthPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

public class Server {

    private static DatagramSocket mainSocket;
    private static int id = 0;
    private static ArrayList<Integer> ports = new ArrayList();
    private static ArrayList<ServerThread> clients = new ArrayList();
    private static GUI gui;
    public static ServerMainPanel main = new ServerMainPanel();
    
    public static void run() {
        gui = new GUI();
        gui.runGUI();
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        run();
        
        byte[] server = new byte[512];
        mainSocket = new DatagramSocket(4445);
        
        System.out.println(InetAddress.getLocalHost());
        
        while(true) {
            try {
                DatagramPacket packet = new DatagramPacket(server, server.length);
                int port = generateNewPort();
                System.out.println("receiving");
                System.out.println("asra");
                mainSocket.receive(packet);
                
                System.out.println(new String(packet.getData(), 0, packet.getLength()));
                
                server = ("/setport " + Integer.toString(port)).getBytes();
                packet = new DatagramPacket(server, server.length, packet.getAddress(), packet.getPort());
                mainSocket.send(packet);
                                
                DatagramSocket clientSocket = new DatagramSocket(port);
                
                ServerThread client = new ServerThread(clientSocket, packet, port, id += 1);
                
                Thread clientThread = new Thread(client);
                clientThread.start();                
                
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
    
    public static void adminLogin(char[] password) {
        if (Authentication.checkPassword(password, "21716:/PKxMm2iPkZzMVJ5jY/as701bHkgfbq+5p6PYLEO+PA=:F0Xio9HscZ7YrcNJglEfjcwectP7nfbTBIHCtK12DYY=")) {
            System.out.println("Access granted");
            setPanel("main");            
        } else {
            System.out.println("go away");
        }
    }
    
    public static void pack() {
        gui.frame.setSize(1, 1);
        gui.frame.pack();
    }
    
    public static void setPanel(String panel) {        
        switch(panel) {
            case "main":
                gui.frame.setContentPane(main.getPanel());
                break;
        }
        
        gui.frame.pack();
    }
}

class ServerThread extends Thread {
    DatagramSocket socket;
    DatagramPacket packet;
    BufferedReader in;
    InetAddress address;
    String received;
    int port;
    int id;
    byte[] bytes;
    boolean connected;
    
    public ServerThread(DatagramSocket s, DatagramPacket p, int po, int idNumber) {
        socket = s;
        packet = p;
        id = idNumber;
        address = packet.getAddress();
        port = po;
        System.out.println(port + " " + packet.getPort());
    }
        
    @Override
    public void run() {        
        try {
            System.out.println("Starting client.");
            
            Server.main.addPanel(this);
            Server.pack();
            
            connected = true;
            
            testConnection();
            
            while(connected) {      
                if(testConnection()) {
                    bytes = new byte[512];
                    packet = new DatagramPacket(bytes, bytes.length);
                    socket.receive(packet);

                    received = new String(packet.getData(), 0, packet.getLength());

                    System.out.println(received);

                    address = packet.getAddress();
                    port = packet.getPort();

                    bytes = ("sending: " + received).getBytes();

                    packet = new DatagramPacket(bytes, bytes.length, address, port);
                    socket.send(packet);
                } else {
                    connected = false;
                }
            } 
            
            System.out.println("Client " + id + " has disconnected.");
        } catch(FileNotFoundException e) {
        } catch (IOException ex) {
        }        
    }
        
    private boolean testConnection() throws IOException {
        bytes = ("/syn").getBytes();
        packet = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(packet);
        
        bytes = new byte[512];
        packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        
        received = new String(packet.getData(), 0, packet.getLength());
        
        return "/ack".equals(received);        
    }    
}