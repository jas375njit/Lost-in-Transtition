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
                    int commaEnd = data.indexOf(",", start);
                    int braceEnd = data.indexOf("}", start);
                    int end = (commaEnd == -1) ? braceEnd : (braceEnd == -1) ? commaEnd : Math.min(commaEnd, braceEnd);
                    return Integer.parseInt(data.substring(start, end).trim());
                }
            }
        } catch (Exception e) {
            System.out.println("API Error: " + e.getMessage());
        }

        return 0; 
    }
}