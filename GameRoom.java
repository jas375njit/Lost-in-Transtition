import java.io.*;
import java.net.*;
import java.util.*;

public class GameRoom{
  private static int [] wordLimit = {10,7,5};
  private static String [] wordList = {"Apple", "Eagle", "Pen", "School"};
  private ArrayList<Socket> players = new ArrayList<>();

public GameRoom (ArrayList<Socket> players){
  this.players = players;
}

public void addPlayer(Socket player){
  players.add(player);
}
  public int getPlayerCount(){
    return players.size();
  }

  public void game(){
    try{
      BufferedReader [] readers = new BufferedReader[players.size()];
      PrintWriter [] writers = new PrintWriter [players.size()];
      System.out.println("Lost in Transition: Game is about to start");

      String [] names = new String [players.size()];

      for (int i = 0; i < players.size(); i++){
        writers[i].println("Enter your name");
        names[i] = readers[i].readLine();
      }
      Random random = new Random();
      String originalWord = wordList[random.nextInt(wordList.length)];

      String current = originalWord;
      for (int i = 0; i < players.size() - 1; i++);{
        int limit = wordLimit[i];
        
      }

    } catch(Exception e){
      System.out.println("Game error");
    }
  }
}