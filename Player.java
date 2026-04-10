import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Player{
  Socket connection;
  BufferedReader incoming;
  PrintWriter outgoing;
  String messageOut;
  String messageIn;
  Scanner userInput = new Scanner(System.in);
}