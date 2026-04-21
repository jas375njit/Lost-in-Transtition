import java.io.*;
import java.net.*;
import java.util.*;

public class GameRoom{
  private static int [] wordLimit = {10,7,5};
  private static String [] wordList = {"Apple", "Eagle", "Flashlight", "Computer", "Paper"};
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
      int amountOfPlayers = players.size();

      //Player 1 reader and writer
      BufferedReader readerPlayer1 = new BufferedReader(new InputStreamReader(players.get(0).getInputStream()));

      PrintWriter writerPlayer1 = new PrintWriter(players.get(0).getOutputStream(),true);

      //Player 2 reader and writer
      BufferedReader readerPlayer2 = new BufferedReader(new InputStreamReader(players.get(1).getInputStream()));

      PrintWriter writerPlayer2 = new PrintWriter(players.get(1).getOutputStream(),true);

      writerPlayer1.println("Lost in Transition: Game is about to start");
      writerPlayer2.println("Lost in Transition: Game is about to start");

      Random random = new Random();
      String word1 = wordList[random.nextInt(wordList.length)];
      String word2 = wordList[random.nextInt(wordList.length)];

      //Round 1

      writerPlayer1.println("Round 1 - Describe this word in 10 words or less withouth saying the word: " + word1);
      writerPlayer2.println("Round 1 - Describe this word in 10 words or less withouth saying the word: " + word2);

      String description1 = readerPlayer1.readLine();
      String description2 = readerPlayer2.readLine();

      //Round 2

      writerPlayer1.println("Round 2 - What is this? " + description2);
      writerPlayer2.println("Round 2 - What is this? " + description1);

      String guess1 = readerPlayer1.readLine();
      String guess2 = readerPlayer2.readLine();

      //Final Resutls

      writerPlayer1.println("Your original word was " + word1 + ". It was lost to " + guess2);

      writerPlayer2.println("Your original word was " + word2 + ". It was lost to " + guess1);

      for(int i = 0; i < amountOfPlayers; i++){
        players.get(i).close();
      }

    }catch(Exception e){
      System.out.println("Game error");
    }
  }
}