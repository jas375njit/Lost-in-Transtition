import java.io.*;
import java.net.*;

public class GameServer {
    private static int port = 1728;
    private static ServerSocket listener;
    private static Socket connection;

    public static void main(String[] args) {
        try {
            listener = new ServerSocket(port);
            System.out.println("Lost in Transition server started on port " + listener.getLocalPort());

            while (true) {
                GameRoom room = new GameRoom();
                System.out.println("Waiting for players...");

                connection = listener.accept();
                BufferedReader firstIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                int totalPlayers = Integer.parseInt(firstIn.readLine());
                room.addPlayer(connection, firstIn);
                System.out.println("Players Connected: " + room.getPlayerCount());

                //This while loop connects both players (more players overtime) at the same tim
                while (room.getPlayerCount() < totalPlayers) {
                    connection = listener.accept();
                    BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    room.addPlayer(connection, r);
                    System.out.println("Players Connected: " + room.getPlayerCount());
                }

                System.out.println("Starting game with " + room.getPlayerCount() + " players");
                new Thread(() -> room.game()).start();
            }

        } catch (Exception e) {
            System.out.println("ERROR: Connection could not be established.");
            return;
        }
    }
}