import java.net.*;
import java.util.Scanner;

public class WinningSelection {

    public static int getScore(String word, String guess) {
        if (guess == null || guess.isEmpty()) return 0;

        if (word.equals(guess)) return 1000000;

        try {
            //Fetches the api into the application
            URL url = new URL("https://api.datamuse.com/words?ml=" + word.replace(" ", "+"));
            
            try (Scanner sc = new Scanner(url.openStream())) {
                String data = sc.useDelimiter("\\A").next().toLowerCase();

                String search = "\"word\":\"" + guess.toLowerCase() + "\",\"score\":";
                
                if (data.contains(search)) {
                    int start = data.indexOf(search) + search.length();
                    int end = data.indexOf(",", start);
                    return Integer.parseInt(data.substring(start, end).trim());
                }
            }
        } catch (Exception e) {
            System.out.println("API Error: ");
        }

        return 0; 
    }
}