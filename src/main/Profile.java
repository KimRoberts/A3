package main;

import java.io.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * main.Profile.java
 * This is a class to store the profile information, including number of wins and losses.
 *
 * @author Asinsa Guniyangodage
 */

public class Profile implements Comparable<Profile> {

    private String name;
    private int playerID;
    private int wins;
    private int losses;
    private String originalValues;

    /**
     * Constructor that creates an instance of a main.Profile
     *
     * @param name     The person's name
     * @param playerID The unique profile number
     */
    public Profile(String name, int playerID, int wins, int losses) {
        setName(name);
        this.playerID = playerID;
        this.wins = wins;
        this.losses = losses;
        originalValues = (name + "," + playerID + "," + wins + "," + losses);
    }

    /**
     * Method gets the profile using the param profileName.
     *
     * @param profileName the name to find
     * @return the profile
     */
    public static Profile getProfile(String profileName) {
        File file = new File("playerProfiles.txt");
        Profile profile = null;

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                Scanner tokenScanner = new Scanner(scanner.nextLine());
                tokenScanner.useDelimiter(",");
                String currentName = tokenScanner.next();
                int id = tokenScanner.nextInt();
                int win = tokenScanner.nextInt();
                int loss = tokenScanner.nextInt();

                if (currentName.equals(profileName)) {
                    profile = new Profile(currentName, id, win, loss);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return profile;
    }

    public void updateProfile() {
        File fileToBeModified = new File("playerProfiles.txt");
        String updatedValues = name + "," + playerID + "," + wins + "," + losses;
        String oldContent = "";
        FileWriter writer = null;

        try {
            Scanner scanner = new Scanner(fileToBeModified);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                oldContent = oldContent + line + System.lineSeparator();
            }

            //Replacing oldString with newString in the oldContent
            String newContent = oldContent.replaceAll(originalValues, updatedValues);
            writer = new FileWriter(fileToBeModified);

            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGame(Boolean win) {
        if (win) {
            wins++;
        } else {
            losses++;
        }
    }

    public int getNumGamesPlayed() {
        return wins + losses;
    }

    public int getNumWins() {
        return wins;
    }

    @Override
    public int compareTo(Profile o) {
        int score = (3 * this.wins) + ((-1) * this.losses);
        int compareScore = (3 * o.wins) + ((-1) * o.losses);
        return compareScore - score;
    }
}