package server;

import java.net.SocketException;
import java.net.UnknownHostException;

public class Server {
    public static void main(String[] args) throws SocketException, UnknownHostException {
        ServerPanelManager manager = ServerPanelManager.getManager();
        manager.setPanel(ServerPanelManager.AUTHENTICATION_PANEL);
    }
}
