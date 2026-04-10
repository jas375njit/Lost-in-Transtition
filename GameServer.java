import java.net.*;

public class GameServer {
    public static void main(String[] args) {
        int port = 1728;
        ServerSocket listener;
        Socket connection;

        try {
            listener = new ServerSocket(port);
            System.out.println("Word Drift server started on port " + listener.getLocalPort());

            connection = listener.accept();
            listener.close();
            System.out.println("Client connected: " + connection.getInetAddress());

        } catch (Exception e) {
            System.out.println("ERROR: Connection could not be established.");
            return;
        }
    }
}