package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.level_editor_components.Map;
import main.level_editor_components.MapSquare;
import main.stage.LevelEditorStage;
import main.tile.*;

import java.io.*;
import java.util.*;

/***
 * This file is to read files for the file, this loads in  the height and width, the fixed tiles, players and
 * the contents of the main.tile bag, This file also allows the writing of files.
 * @author G38.dev
 * @version 1.9
 */

public class FileReader {
    public static int width;
    public static int height;
    private static Player player;
    public static int playeriD;
    private static int fixedTileAmount;
    private static Scanner lineScanner = null;
    private static String currentLine;
    private static int sum;
    private static BoardSquare[][] gameBoard;
    private static int fixX;
    private static int fixY;
    private static BoardSquare boardSquare;
    private static ArrayList<Tile> tiles = new ArrayList<Tile>();
    private static ArrayList<Integer[]> playerPositions = new ArrayList<>();
    private static String texturePack;
    private static String test;


    /***
     * This is a method that will iterate throughout the file that is specified through the parameter passed in,
     * it then runs a  series of checks which is relavent to the correct piece of data, through the if statements below.
     * Then feeds the information into the classes, if file does not exist a file Not found error is thrown
     * @param fileName This is the name/location of the file to read from
     */
    public static void readFile(String fileName) {
        File inputFile = new File(fileName);
        try {

            lineScanner = new Scanner(inputFile, "utf-8");
            while (lineScanner.hasNextLine()) {

                lineScanner.useDelimiter("\r\n");
                currentLine = lineScanner.nextLine();
                if (sum < 1) {
                    getHeightandWidth(currentLine);
                    gameBoard = new BoardSquare[height][width];
                }
                if (lineScanner.hasNextInt()) {
                    fixedTileAmount = lineScanner.nextInt();
                }

                if (currentLine.contains("true")) {
                    setfixedTile(currentLine, gameBoard);
                }
                if (currentLine.contains("player")) {
                    readPlayers(currentLine);
                }
                if (currentLine.contains("false")) {
                    tileBagContents(currentLine);
                }
                if (currentLine.contains("Goal")) {
                    goalSqr(currentLine);
                }
                if (currentLine.contains("effect")) {
                    setEffectTiles(currentLine);
                }
                if (currentLine.contains("goal")) {
                    goalSqr(currentLine);
                }
                if (currentLine.contains(".texture")) {
                    setTexturePack(currentLine);
                }


            }

        } catch (FileNotFoundException e) {
            System.out.println("Error 404:no such element found " + e);
            System.exit(0);
        } finally {
            if (currentLine.equals("#")) {
                lineScanner.close();
            }
        }

    }

    private static void setTexturePack(String currentLine) {
        texturePack = currentLine;
    }

    public static String getTexturePack() {
        return texturePack;
    }

    /**
     * This sets all the effect tiles which are within the main.tile bag. It will put X amount of a specific effect main.tile into the bag.
     *
     * @param currentLine The current line of the line scanner to bed fed into the token scanner
     */
    private static void setEffectTiles(String currentLine) {
        Scanner tokenScanner = new Scanner(currentLine);
        tokenScanner.useDelimiter(",");
        String effect = tokenScanner.next();
        String effectType = tokenScanner.next();
        int amount = tokenScanner.nextInt();
        while (amount > 0) {
            tiles.add(new EffectTile(new Effect(effectType)));
            amount--;

        }

    }

    /***
     * This gets the height and width of the UI and will set it to all the scenes throughout the UI to ensure that it is
     * consistent.
     * @param currentLine The current line of the line scanner to be fed into the token scanner
     */
    private static void getHeightandWidth(String currentLine) {
        Scanner tokenScanner = new Scanner(currentLine);
        tokenScanner.useDelimiter(",");
        height = tokenScanner.nextInt();
        width = tokenScanner.nextInt();
        sum++;
    }

    /***
     * This method gets the fixed tiles which are in the text file.
     * @param line The current line of the line scanner to be fed into the token scanner
     * @param gameboard This is the game board which the tiles will be placed
     */
    private static void setfixedTile(String line, BoardSquare[][] gameboard) {
        Scanner tokenScanner = new Scanner(line);
        tokenScanner.useDelimiter(",");
        fixX = tokenScanner.nextInt();
        fixY = tokenScanner.nextInt();
        String tileType = tokenScanner.next();
        int rotation = tokenScanner.nextInt();
        Boolean isFixed = tokenScanner.nextBoolean();
        NavigationTile newTile = TileFactory.getTile(tileType, rotation, isFixed);
        System.out.println(newTile); //testing
        System.out.println(fixX + "" + fixY);
        boardSquare = new BoardSquare(newTile, fixX, fixY);
        gameBoard[fixY][fixX] = boardSquare;

    }

    /***
     * This method returns the constructed BoardSquare from the file Reader.
     * @return The board square for the board
     */
    public static BoardSquare getBoardSquare() {
        return boardSquare;
    }

    /***
     * This gets the fixed X position of the main.tile.
     * @return This gets the fixed tiles X position
     */
    public static int getFixedPosX() {
        return fixX;
    }

    /***
     * This gets the fixed Y position of the main.tile.
     * @return This gets the fixed tiles Y position
     */
    public static int getFixedPosY() {
        return fixY;
    }

    /***
     * This gets the content of the main.tile bag.
     * @return An array list containing all the tiles in  the main.tile bag
     */
    public static ArrayList getTileBag() {
        return tiles;
    }

    /***
     * This  reads all the players from the file.
     * @param currentLine The current line of the line scanner to be fed into the token scanner
     */
    public static void readPlayers(String currentLine) {
        Scanner tokenScanner = new Scanner(currentLine);
        tokenScanner.useDelimiter(",");
        String playerName = tokenScanner.next();
        playeriD = tokenScanner.nextInt();
        int playerX = tokenScanner.nextInt();
        int playerY = tokenScanner.nextInt();
        playerPositions.add(new Integer[]{
                playerY,
                playerX
        });
    }


    /**
     * Returns each player in the playerProfiles.txt file.
     *
     * @return ObservableList of all the profiles
     */
    public static ObservableList<String> readProfileNames(String fileName) {
        ObservableList<String> profiles = FXCollections.observableArrayList();
        Scanner sc = null;
        try {
            sc = new Scanner(new File(fileName + ".txt"), "utf-8");
            sc.useDelimiter(",");
            while (sc.hasNextLine()) {
                Scanner line = new Scanner(sc.nextLine());
                line.useDelimiter(",");
                String name = line.next();
                int userID = line.nextInt();
                int wins = line.nextInt();
                int losses = line.nextInt();
                Profile profile = new Profile(name, userID, wins, losses);
                String playerName = profile.getName();
                profiles.add(playerName);
                player = new Player(profile, "");

            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed to find txt");
            e.printStackTrace();
        }
        return profiles;

    }


    /**
     * Returns all the profiles in an ArrayList
     *
     * @param fileName Text file that is going to be read
     * @return ArrayList of profiles
     */
    public static ObservableList<Profile> readAllProfiles(String fileName) {
        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        Scanner sc = null;
        try {
            sc = new Scanner(new File(fileName + ".txt"), "utf-8");
            sc.useDelimiter(",");
            while (sc.hasNextLine()) {
                Scanner line = new Scanner(sc.nextLine());
                line.useDelimiter(",");
                String name = line.next();
                int userID = line.nextInt();
                int wins = line.nextInt();
                int losses = line.nextInt();
                Profile profile = new Profile(name, userID, wins, losses);
                profiles.add(profile);
                player = new Player(profile, "");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed to find txt");
            e.printStackTrace();
        }
        return profiles;

    }

    public static Player getPlayer() {
        return player;
    }

    /***
     * This gets the tiles for the main.tile bag this does not get the effect tiles.
     * @param currentLine The current line of the line scanner to be read by the token scanner
     */
    public static void tileBagContents(String currentLine) {
        Scanner tokenScanner = new Scanner(currentLine);
        TileBag tileBag = new TileBag();
        tokenScanner.useDelimiter(",");
        String tileType = tokenScanner.next();
        int rotation = 0;
        Boolean isFixed = tokenScanner.nextBoolean();
        int amount = tokenScanner.nextInt();
        while (amount > 0) {
            tiles.add(TileFactory.getTile(tileType, rotation, isFixed));

            amount -= 1;
        }

    }

    /***
     * This gets the goal main.tile location.
     * @param currentLine The current line from the line scanner for the token scanner.
     */
    public static void goalSqr(String currentLine) {
        Scanner tokenScanner = new Scanner(currentLine);

        GoalTile goal = new GoalTile(0);

    }

    /***
     * This allows the writing and creation of the files, and will write the content to the file line by line
     * @param fileName The name of the file you would like to write to or create
     * @param fileContents The line you wouuld like to add to the file
     */
    public static void writeToFiles(String fileName, String fileContents) {
        File f = new File(fileName);

        if (f.exists() && !f.isDirectory()) {

            try {
                File myObj = new File(fileName + ".txt");
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter myWriter = new FileWriter(fileName + ".txt", true);
                myWriter.write(fileContents + "\n");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    /***
     * This writes to the save file, this method over writes the original data within the file.
     * @param fileName The name of the file you would like to write to
     * @param fileContents The contents of the file.
     */
    public static void writeToFilesNoAppend(String fileName, String fileContents) {
        File f = new File(fileName);

        if (f.exists() && !f.isDirectory()) {

            try {
                File myObj = new File(fileName + ".txt");
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter myWriter = new FileWriter(fileName + ".txt", false);
                myWriter.write(fileContents + "\n");

                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that returns the number of lines in the file.
     *
     * @param fileName The file where you want to get the amount of lines in the file
     */
    public static int linesInFile(String fileName) {
        int lines = 1;
        Scanner sc = null;
        try {
            sc = new Scanner(new File(fileName + ".txt"));
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
                lines++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed to find txt");
            e.printStackTrace();
        }
        return lines;
    }

    /***
     * This method reads the save file, and creates the game board which was in the save files, this includes the
     * players and the players progress and what contents they have in their hand.
     * @param fileUrl This is the location of the file you would like to read
     */
    public static void readSave(String fileUrl) {
        LinkedList<Player> playerss = new LinkedList<Player>();
        HashMap<String, Player> pp = new HashMap<>();
        try {
            File file = new File(fileUrl);
            Scanner scanner = new Scanner(file);

            int height = Integer.parseInt(scanner.nextLine());
            int width = Integer.parseInt(scanner.nextLine());
            scanner.nextLine();
            List<String> csvs = new ArrayList<>();
            for (int i = 0; i < height * width; i++) {
                csvs.add(scanner.nextLine());
            }
            BoardSquare[][] boardSquares = new BoardSquare[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (csvs.size() != 0) {
                        boardSquares[i][j] = BoardSquare.deserialise(csvs.remove(0));
                    }
                    if (boardSquares[i][j].getPlayer() != null) {
                        pp.put(boardSquares[i][j].getPlayer().getName(), boardSquares[i][j].getPlayer());
                    }
                }
            }

            System.out.println(scanner.nextLine());

            boolean stop = true;
            while (stop) {
                String line = scanner.nextLine();
                if (line.equals(";")) {
                    stop = false;

                } else {
                    String[] player = line.split(",");
                    Player aa = pp.get(player[0]);
                    playerss.add(aa);
                    for (int i = 1; i < player.length; i++) {
                        aa.addEffectTile(new EffectTile(new Effect(player[i])));
                    }
                }

            }
            Mataha.getGame().setPlayers(playerss);
            Mataha.getGame().getGameBoard().setBoardSquares(boardSquares);
            Mataha.getGame().getGameInfoPanel().getPtp().reloadImages();
            Mataha.getGame().getGameInfoPanel().getTipLabel().setDrawTileText();
            Mataha.getGame().getGameInfoPanel().getPtp().update();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveMapSquares(String name, LevelEditorStage context) {

        name = "src\\resources\\LevelEditorFiles\\" + name + ".txt";

        try {
            File file = new File(name);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(context.getMap().getMapHeight() + "," + context.getMap().getMapWidth() + "\n");
            fileWriter.append(context.getMap().serialise());
            fileWriter.append("--\n");
            fileWriter.append(context.getSelectedTexture() + "\n");
            fileWriter.append(context.serialiseTileBag());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMapSquares(String load, LevelEditorStage context) {

        String filename = "src\\resources\\LevelEditorFiles\\" + load;

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            String[] hw = scanner.nextLine().split(",");
            int h = Integer.parseInt(hw[0]);
            int w = Integer.parseInt(hw[1]);
            MapSquare[][] map = new MapSquare[h][w];
            String sqaure = "";
            while (!sqaure.equals("--")) {
                sqaure = scanner.nextLine();
                if (sqaure.equals("--")) {
                    break;
                }
                String[] qq = sqaure.split(",");
                MapSquare square = new MapSquare(context, Integer.parseInt(qq[0]), Integer.parseInt(qq[1]));
                square.setStartingPosition(isTrue(qq[2]));
                String[] tile = qq[3].split(";");
                square.setTile(TileFactory.getTile(tile[0], Integer.parseInt(tile[1]), isTrue(tile[2])));
                map[square.getY()][square.getX()] = square;
            }
            context.setNewTexture(scanner.nextLine());
            String inBag = "";
            ArrayList<Tile> bag = new ArrayList<>();

            while (scanner.hasNextLine()) {
                inBag = scanner.nextLine();
                String[] tile = inBag.split(",");
                if (tile[0].equals("effect")) {
                    EffectTile effectTile = new EffectTile(new Effect(tile[1]));
                    effectTile.setFirstTurn(isTrue(tile[2]));
                    bag.add(effectTile);
                } else {
                    NavigationTile navigationTile = TileFactory.getTile(tile[0], Integer.parseInt(tile[1]), isTrue(tile[2]));
                    bag.add(navigationTile);
                }
            }

            for (int row = 0; row < map.length; row++) {
                for (int col = 0; col < map[0].length; col++) {
                    if (map[row][col] == null) {
                        map[row][col] = new MapSquare(context, col, row);
                    }
                }
            }
            setFileReaderVairables(w, h, map, bag);
            context.loadTileBag(bag);
            context.getResizePanel().setHeightWidth(h, w);
            context.getMap().setMapSquares(map);
            context.getMap().redrawTiles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setFileReaderVairables(int w, int h, MapSquare[][] mapSquares, ArrayList<Tile> tileBag) {
        BoardSquare[][] boardSquares = new BoardSquare[h][w];
        ArrayList<Integer[]> playerPositions = new ArrayList<>();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                MapSquare mapSquare = mapSquares[row][col];
                if (mapSquare.getTile() != null) {
                    if (mapSquare.isStartingPosition()) {
                        playerPositions.add(new Integer[]{mapSquare.getY(), mapSquare.getX()});
                    }
                    boardSquares[row][col] = mapSquares[row][col].toBoardSquare();
                }
            }
        }

        FileReader.playerPositions = playerPositions;
        System.out.println(FileReader.playerPositions);
        gameBoard = boardSquares;
        FileReader.tiles = tileBag;
    }

    public static boolean isTrue(String in) {
        if (in.equals("true")) {
            return true;
        }
        return false;
    }

    public static BoardSquare[][] getGameBoard() {
        return gameBoard;
    }

    public static ArrayList<Integer[]> getPlayerPositions() {
        return playerPositions;
    }
}