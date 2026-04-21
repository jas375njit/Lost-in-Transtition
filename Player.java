import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Random;

public class Player {
  Socket connection = new Socket();
  BufferedReader incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));

  PrintWriter outgoing = new PrintWriter((connection.getOutputStream(), true));
  String messageOut;
  String messageIn;
  Scanner userInput = new Scanner(System.in);
  Random rand = new Random();
  int randomNumber = rand.nextInt((10-1) + 1) + 1;
  public static void main (String[]args){
  try {
    System.out.println ("Connecting to Server...");
    System.out.println ("Waiting for other players to connect...");

  } catch(Exception e){
    System.out.println("ERROR: Connection was not established");
    return;
  }

  //Actual game and moving around of words
  try {
    String wordGiven = initialWord[randomNumber];
    while(loop < amountOfPlayers){
      //This loop iterates as many players there is, defining how many round there will be played
      System.out.println("Define this word in 10 words");
      System.out.println(wordGiven);

      
    }
  } catch (Exception e){

  }
  }
}