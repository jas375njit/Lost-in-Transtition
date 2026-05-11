import java.io.*;
import java.net.*;
import java.util.*;

public class GameRoom {
    private static int[] wordLimit = {10, 7, 5};
    private static String[] wordList = {
        "Apple", "Eagle", "Flashlight", "Computer", "Paper", "Pencil", "Clock", "Ocean"
    };
    private ArrayList<Socket> players = new ArrayList<>();
    private ArrayList<BufferedReader> prebuiltReaders = new ArrayList<>();

    public GameRoom() {
    }

    public void addPlayer(Socket player, BufferedReader reader) {
        players.add(player);
        prebuiltReaders.add(reader);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void game() {
        int amountOfPlayers = players.size();
        PrintWriter[] writers = new PrintWriter[amountOfPlayers];
        BufferedReader[] readers = new BufferedReader[amountOfPlayers];

        try {
            WinningSelection scorer = new WinningSelection();

            for (int i = 0; i < amountOfPlayers; i++) {
                writers[i] = new PrintWriter(players.get(i).getOutputStream(), true);
                readers[i] = prebuiltReaders.get(i);
            }

            Random random = new Random();
            String[] words = new String[amountOfPlayers];
            for (int i = 0; i < amountOfPlayers; i++) {
                words[i] = wordList[random.nextInt(wordList.length)];
            }

            // Tell each player their number
            for (int i = 0; i < amountOfPlayers; i++) {
                writers[i].println("PLAYER:" + (i + 1));
            }

            // Round 1
            for (int i = 0; i < amountOfPlayers; i++) {
                writers[i].println("Lost in Transition: Game is about to start");
                writers[i].println("Round 1 - Describe this word in 10 words or less without saying the actual word: " + words[i]);
            }

            String[] current = new String[amountOfPlayers];
            for (int i = 0; i < amountOfPlayers; i++) {
                current[i] = readers[i].readLine();
            }

            Integer[] originIndex = new Integer[amountOfPlayers];
            for (int i = 0; i < amountOfPlayers; i++) originIndex[i] = i;

            int round = 2;
            while (round <= amountOfPlayers) {
                Deque<String> contentQueue = new ArrayDeque<>(Arrays.asList(current));
                contentQueue.addFirst(contentQueue.pollLast());

                Deque<Integer> originQueue = new ArrayDeque<>(Arrays.asList(originIndex));
                originQueue.addFirst(originQueue.pollLast());

                for (int i = 0; i < amountOfPlayers; i++) {
                    current[i] = contentQueue.poll();
                    originIndex[i] = originQueue.poll();
                }

                if (round % 2 == 0) {
                    //Round 2
                    for (int i = 0; i < amountOfPlayers; i++) {
                        writers[i].println("Round " + round + " - What word does this describe - " + current[i]);
                    }
                } else {
                    //Round 1
                    for (int i = 0; i < amountOfPlayers; i++) {
                        writers[i].println("Round " + round + " - Describe this in 10 words or less without saying the actual word: " + current[i]);
                    }
                }

                String[] responses = new String[amountOfPlayers];
                for (int i = 0; i < amountOfPlayers; i++) {
                    responses[i] = readers[i].readLine();
                }
                current = responses;
                round++;
            }

            int[] scores = new int[amountOfPlayers];
            for (int i = 0; i < amountOfPlayers; i++) {
                scores[i] = WinningSelection.getScore(words[originIndex[i]], current[i]);
                System.out.println("Player " + (i + 1) + " | word=" + words[originIndex[i]] + " | guess=" + current[i] + " | score=" + scores[i]);
            }

            //Final Results
            String[] finalGuessForWord = new String[amountOfPlayers];
            for (int i = 0; i < amountOfPlayers; i++) {
                finalGuessForWord[originIndex[i]] = current[i];
            }

            int maxScore = Arrays.stream(scores).max().getAsInt();
            List<Integer> winners = new ArrayList<>();
            for (int i = 0; i < amountOfPlayers; i++) {
                if (scores[i] == maxScore) winners.add(i + 1);
            }

            String result;
            if (winners.size() > 1) {
                result = "It's a tie between Players " + winners + " with score " + maxScore + "!";
            } else {
                result = "Player " + winners.get(0) + " wins with score " + maxScore + "!";
            }

            for (int i = 0; i < amountOfPlayers; i++) {
                writers[i].println("Your Initial Word was " + words[i] + ". It was lost to " + finalGuessForWord[i]);
                writers[i].println(result);
            }

            for (int i = 0; i < amountOfPlayers; i++) {
                players.get(i).close();
            }

        } catch (Exception e) {
            System.out.println("A player disconnected. Ending game");
            try {
                for (PrintWriter w : writers) if (w != null) w.println("A player disconnected. Game over.");
                for (Socket s : players) try { s.close(); } catch (Exception ex) {}
            } catch (Exception ex) {}
        }
    }
}