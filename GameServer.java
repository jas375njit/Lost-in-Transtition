import java.io.*;
import java.net.*;

public class GameServer {
    private static int port = 1728;
    private static ServerSocket listener;

    public static void main(String[] args) {
        try {
            listener = new ServerSocket(port);
            System.out.println("Lost in Transition server started on port " + listener.getLocalPort());

            while (true) {
                GameRoom room = new GameRoom();
                System.out.println("Waiting for players...");

                Socket firstSocket = listener.accept();
                BufferedReader firstIn = new BufferedReader(new InputStreamReader(firstSocket.getInputStream()));
                int totalPlayers = Integer.parseInt(firstIn.readLine());
                room.addPlayer(firstSocket, firstIn);
                System.out.println("Players Connected: " + room.getPlayerCount());

                //This while loop connects both players (more players overtime) at the same time
                while (room.getPlayerCount() < totalPlayers) {
                    Socket s = listener.accept();
                    BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    room.addPlayer(s, r);
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