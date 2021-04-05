package main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Utils class will do something.
 *
 * @author Khalid Evans
 * @version 4
 */
public class Utils {

    /* Bool for whether the file has been checked before. */
    private static boolean isValidFile = false;

    /**
     * Method translates phrase to chosen language.
     *
     * @param language language to translate to of type String.
     * @param phrase   phrase to be translated of type String.
     * @return translated phrase of type String.
     */
    public static String translate(String phrase, String language) {
        Scanner in = checkFile("src/resources/Languages/langs.txt");
        String translated = "N/A";
        if (!isValidFile) {
            in = checkFile("src/resources/Languages/langs.txt");
            isValidFile = true;
        }

        int langIndex = 0;
        boolean foundLang = false;
        while (in.hasNextLine()) {
            String currentLine = in.nextLine();
            Scanner line = new Scanner(currentLine);

            if (!foundLang) {
                String[] languages = line.nextLine().split(",");
                for (int i = 0; i < languages.length; i++) {
                    if (languages[i].equals(language)) {
                        langIndex = i;
                    }
                }
                foundLang = true;
            } else {
                String[] words = line.nextLine().split(",");
                for (String word : words) {
                    if (word.toLowerCase().equals(phrase.toLowerCase())) {
                        translated = words[langIndex];
                        break;
                    }
                }
            }
            line.close();
        }

        in.close();
        if (translated != "N/A") {
            return translated;
        } else {
            return "[" + phrase + "]";
        }
    }

    /**
     * Method checks if the file can be found to be read from
     *
     * @param fileName the file name of type String
     * @return Scanner with the file inputted of type Scanner
     */
    private static Scanner checkFile(String fileName) {
        //Language support file.
        File langFile = new File(fileName);
        Scanner in = null;
        try {
            in = new Scanner(langFile, "utf-8");
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find " + fileName);
            System.exit(0);
        }
        return in;
    }

    /**
     * Method returns all the supported languages.
     *
     * @return languages in an ArrayList of type String
     */
    public static ArrayList<String> getLanguages() {
        Scanner in = checkFile("src/resources/Languages/langs.txt");
        if (!isValidFile) {
            in = checkFile("src/resources/Languages/langs.txt");
            isValidFile = true;
        }
        return new ArrayList<>(Arrays.asList(in.nextLine().split(",")));
    }

    /**
     * Method retrieves the message of the day for CS-230's Group Project.
     *
     * @return message of the day of type String.
     */
    public static String messageOfDay() {
        String decoded = httpRequest("http://cswebcat.swansea.ac.uk/puzzle");
        StringBuilder encoded = new StringBuilder("CS-230");
        boolean doBackwards = true;
        for (int i = 0; i < decoded.toCharArray().length; i++) {
            int iBack = decoded.toCharArray()[i] - (i + 1);
            int iForward = decoded.toCharArray()[i] + (i + 1);
            if (doBackwards) {
                doBackwards = false;
                if (iBack < 65) {
                    encoded.append((char) (iBack + 26));
                } else {
                    encoded.append((char) iBack);
                }
            } else {
                doBackwards = true;
                if (iForward > 90) {
                    encoded.append((char) (iForward - 26));
                } else {
                    encoded.append((char) iForward);
                }
            }
        }
        encoded.append(encoded.length());

        return httpRequest("http://cswebcat.swansea.ac.uk/message?solution=" + encoded);
    }

    /**
     * Method sends an HTTP GET request to a URL
     *
     * @param urlString URL of the site of type String
     * @return response from the URL of type String
     */
    private static String httpRequest(String urlString) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            System.out.println("request failed from " + urlString);
        }
        return response.toString();
    }

    public static boolean outOfBounds(int x, int y, BoardSquare[][] board) {
        return x < 0 || y < 0 || x >= board[0].length || y >= board.length;
    }
}