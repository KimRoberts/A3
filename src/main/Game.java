package main;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.panels.BottomPanel;
import main.panels.DrawCardPanel;
import main.panels.GameInfoPanel;
import main.stage.LoadGameStage;
import main.stage.NewGameStage;
import main.tile.EffectTile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * main.Game.java
 * <p>
 * This class is used to build a game board with specified params and start or end a game.
 *
 * @author G38.dev
 * @version 4
 */

public class Game {

    private TileBag tileBag;
    private GameBoard gameBoard;
    private static ArrayList<Profile> allProfiles = new ArrayList<Profile>();
    private static ArrayList<Player> allPlayers = new ArrayList<Player>();
    private ArrayList<Effect> timedEffects = new ArrayList<Effect>();
    private BorderPane mainLayout;
    private BottomPanel bottomPanel;
    private DrawCardPanel drawCardPanel;
    private GameInfoPanel gameInfoPanel;
    private int boardState;

    /**
     * This constructor gets a new main.Game, and sets it to the main layout, and creates a new tileBag.
     *
     * @param mainLayout The layout used within the game.
     */
    public Game(BorderPane mainLayout) {
        this.mainLayout = mainLayout;
        tileBag = new TileBag();
        initScreen();
        players.peek().setNumMoves(1);

    }

    /**
     * This method initiates the screen so the user can interact and see the game.
     */
    private void initScreen() {
        GameBoard board = new GameBoard(FileReader.getGameBoard());
        this.gameBoard = board;
        for (int row = 0; row < gameBoard.getBoard().length; row++) {
            for (int col = 0; col < gameBoard.getBoard()[0].length; col++) {
                BoardSquare bs = board.getBoard()[row][col];
                bs.setTile(bs.getTile());
            }
        }
        board.redrawTiles();
        mainLayout.setCenter(board);
        initCharacters();
        BottomPanel bottomPanel = new BottomPanel(this);
        this.bottomPanel = bottomPanel;
        bottomPanel.displayPlayerTiles();
        mainLayout.setBottom(bottomPanel);
        this.gameInfoPanel = new GameInfoPanel(this);
        mainLayout.setRight(gameInfoPanel);
        drawCardPanel = new DrawCardPanel();
        mainLayout.setLeft(drawCardPanel);
        TitlePanel titlePanel = new TitlePanel("src\\resources\\Images\\game_images\\title.gif");
        mainLayout.setTop(titlePanel);
    }


    /**
     * This initiates the players onto the game board, this loads each character into the 4 corners of the gameBoard.
     */
    private void initCharacters() {
        for (int i = 0; i < NewGameStage.numOfPlayers; i++) {
            Profile profile = Profile.getProfile(NewGameStage.players.get(i));
            Player player = new Player(profile, NewGameStage.chosenCharacters.get(i));
            allProfiles.add(profile);
            allPlayers.add(player);
        }
        Player player1 = allPlayers.get(0);
        Player player2 = allPlayers.get(1);

        ArrayList<Integer[]> playerPositions = FileReader.getPlayerPositions();

        gameBoard.getBoard()[playerPositions.get(0)[0]][playerPositions.get(0)[1]].setPlayer(player1);
        gameBoard.getBoard()[playerPositions.get(1)[0]][playerPositions.get(1)[1]].setPlayer(player2);

        players.add(player1);
        players.add(player2);

        if (NewGameStage.numOfPlayers >= 3) {
            Player player3 = allPlayers.get(2);
            gameBoard.getBoard()[playerPositions.get(2)[0]][playerPositions.get(2)[1]].setPlayer(player3);
            players.add(player3);
        }
        if (NewGameStage.numOfPlayers == 4) {
            Player player4 = allPlayers.get(3);
            gameBoard.getBoard()[playerPositions.get(3)[0]][playerPositions.get(3)[1]].setPlayer(player4);
            players.add(player4);
        }
        tileBag.setTiles(FileReader.getTileBag());
    }


    /***
     * This method ends the game, this happens when the user's player lands on the goal square.
     * It adds to the wins and losses of the players. It also shows a notification  on who won and allows the players to play again or exit.
     * @param profile The profile of the player who won.
     */
    public void endGame(Profile profile) {
        for (int i = 0; i < NewGameStage.numOfPlayers; i++) {
            if (allProfiles.get(i).equals(profile)) {
                profile.addGame(true);
                profile.updateProfile();
            } else {
                allProfiles.get(i).addGame(false);
                allProfiles.get(i).updateProfile();
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Congratulations");
        alert.setHeaderText("Congratulations");
        alert.setContentText("Congratulations, " + profile.getName() + " you won the game!");

        ButtonType buttonTypeMenu = new ButtonType("Menu");
        ButtonType buttonTypeExit = new ButtonType("Exit");

        alert.getButtonTypes().setAll(buttonTypeMenu, buttonTypeExit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeMenu) {
            returnToMenu();
        } else {
            Stage stage = (Stage) Mataha.getScene().getWindow();
            stage.close();
        }
    }

    /***
     *This method allows the user to return to the main menu.
     */
    public void returnToMenu() {
        MainMenu menu = new MainMenu();
        try {
            menu.start(new Stage());
            Stage stage = (Stage) Mataha.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Queue<Player> players = new LinkedList<>();

    /***
     * This method sets all the players in the game.
     * @param allPlayers
     */
    public static void setAllPlayers(ArrayList<Player> allPlayers) {
        Game.allPlayers = allPlayers;
    }

    /**
     * This method assigns players to the queue and the list
     *
     * @param players a list/queue of players
     */
    public void setPlayers(Queue<Player> players) {
        this.players = players;
        this.allPlayers = new ArrayList<>();
        this.allPlayers.addAll(players);
    }

    /**
     * This method gets the next player in the queue.
     *
     * @return the player that is now at the front of the queue after the poll
     */
    public Player nextPlayer() {
        players.add(players.poll());
        return players.peek();
    }

    /***
     * This method gets the queue of players.
     * @return The queue of players.
     */
    public Queue<Player> getPlayers() {
        return players;
    }

    /***
     * This gets the queue size/ amount of players in the game.
     * @return The size of the queue
     */
    public int getQueueSize() {
        return players.size();
    }

    /**
     * This gets the current player who is playing.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.peek();
    }

    /**
     * This sets the effects to add to the timed effects.
     *
     * @param effect effect to add to timed effects
     */
    public void setTimedEffects(Effect effect) {
        timedEffects.add(effect);
    }

    /***
     * This method starts a countdown on the effect and decreases the time.
     */
    public void effectCountdown() {
        if (timedEffects != null) {
            for (int i = 0; i < timedEffects.size(); i++) {
                if (timedEffects.get(i).getTime() > 0) {
                    timedEffects.get(i).decreaseTime();
                } else {
                    timedEffects.get(i).removeTime();
                    timedEffects.remove(i);
                }
            }
        }
    }

    /***
     * This is a game loop which allows the game to save, and decrease any effect counters, this loop happens every turn.
     * @param player The player whose turn it is.
     */
    public void gameLoop(Player player) {
        save();
        bottomPanel.getPlayerTilesPanel().setPlayerTiles();
        player.setNumMoves(1);
        player.updateEffectTiles();
        effectCountdown();
        initDrawTileUI();
        gameInfoPanel.getPtp().update();

    }

    public void initNavigationUI() {
        gameBoard.hideInserTileButtons();
        bottomPanel.displayNavigationButtons();
        if (getCurrentPlayer().canMove()) {
            gameInfoPanel.getTipLabel().setMoveText();
        } else {
            gameInfoPanel.getTipLabel().setCannotMoveText();
            gameInfoPanel.showNextButton();
        }
    }

    /***
     *This method initialised the UI and displays the tiles on the board.
     */
    public void initDrawTileUI() {
        bottomPanel.displayPlayerTiles();
        drawCardPanel.showDrawTileUI();
        gameInfoPanel.getTipLabel().setDrawTileText();
        save();
    }

    /***
     * This method saves the game file and writes all the information to a file.
     */
    public void save() {

        BoardSquare[][] board = Mataha.getGame().getGameBoard().getBoard();
        FileReader.writeToFilesNoAppend("save", "" + board.length);
        FileReader.writeToFiles("save", "" + board[0].length);
        FileReader.writeToFiles("save", ";");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                FileReader.writeToFiles("save", board[i][j].serialise());
            }
        }
        FileReader.writeToFiles("save", ";");

        for (Player player : Mataha.getGame().getPlayers()) {
            String tile = player.getProfile().getName() + ",";
            for (EffectTile etile : player.getHeldTiles()) {
                tile += etile.getTileEffect().getEffectName() + ",";
            }
            FileReader.writeToFiles("save", tile);
        }
        FileReader.writeToFiles("save", ";");
    }

    /***
     *This method loads a file which contains the contents of a game.
     */
    public void load() {
        FileReader.readSave(LoadGameStage.saveFileUrl);
    }

    /***
     *This method gets the main.tile bag for the game, this contains the effect tiles and navigation files.
     * @return The Tile bag for the game
     */
    public TileBag getTileBag() {
        return tileBag;
    }

    /***
     * This method gets the gameboard of the game.
     * @return The game board containing the tiles in play.
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /***
     *This gets the draw card Panel for the UI.
     * @return This returns the panel for the cards being drawn out of the main.tile bag.
     */
    public DrawCardPanel getDrawCardPanel() {
        return drawCardPanel;
    }

    /***
     *This gets the bottom panel for the UI.
     * @return The bottom panel for effect tiles or navigation tiles (part of the UI)
     */
    public BottomPanel getBottomPanel() {
        return bottomPanel;
    }

    /***
     *This gets the information panel on how to play the game.
     * @return The game information panel for the UI.
     */
    public GameInfoPanel getGameInfoPanel() {
        return gameInfoPanel;
    }

    /***
     *This allows you to set the board State.
     * @param boardState The state of the board.
     */
    public void setBoardState(int boardState) {
        this.boardState = boardState;
    }

    /***
     * This gets the state of the board for the UI.
     * @return a int for the board state.
     */
    public int getBoardState() {
        return boardState;
    }

}