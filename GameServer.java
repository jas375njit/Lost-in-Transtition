import java.net.*;

public class GameServer {
    private static int port = 1728;
    private static ServerSocket listener;
    private static Socket connection;

    public static void main(String[] args) {
        try{
            listener = new ServerSocket(port);
            System.out.println("Los in Transition server started on port " + port);

            while (true){
                GameRoom room = new GameRoom();
                System.out.println("Waiting for players...");

            //This while loop connects both players (more players overtime) at the same time
            while (room.getPlayerCount() < 2){
                connection = listener.accept();
                room.addPlayer(connection);
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
