package main;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import main.tile.NavigationTile;
import main.tile.Tile;

import java.util.*;

public class GameBoard extends GridPane {

    private HashMap<String, Button> insertButtonMap = new HashMap<>();
    //arbitrary comment
    private BoardSquare[][] board;
    private INPUTMODE inputMode;

    public GameBoard(BoardSquare[][] board) {
        this.board = board;
        fillFromTileBag();
        setBoardSquares(board);
        setBoardSquareListeners();
        this.setVgap(3);
        this.setHgap(3);
        inputMode = INPUTMODE.NONE;

        generateInsertTileButtons();

        hideInserTileButtons();

    }

    public BoardSquare[][] getBoard() {
        return board;
    }

    /**
     * @param row  the row to insert into the left of
     * @param tile the main.tile to insert
     */
    public void pushFromLeft(int row, NavigationTile tile) {

        Player player = board[row][board.length - 1].getPlayer();

        Mataha.getGame().getTileBag().returnTile(board[row][board[0].length - 1].getTile());

        for (int i = board[0].length - 1; i > 0; i--) {
            board[row][i].setTile(board[row][i - 1].getTile());
            board[row][i].setPlayer(board[row][i - 1].getPlayer());
        }
        board[row][0].setTile(tile);
        board[row][0].setPlayer(player);
    }

    /**
     * @param row  the row to insert into the right of
     * @param tile the main.tile to insert
     */
    public void pushFromRight(int row, NavigationTile tile) {

        Player player = board[row][0].getPlayer();

        Mataha.getGame().getTileBag().returnTile(board[row][0].getTile());

        for (int i = 0; i < board[0].length - 1; i++) {
            board[row][i].setPlayer(board[row][i + 1].getPlayer());
            board[row][i].setTile(board[row][i + 1].getTile());
        }
        board[row][board[0].length - 1].setTile(tile);
        board[row][board[0].length - 1].setPlayer(player);
    }

    /**
     * @param column the column to insert into the top of
     * @param tile   the main.tile to insert
     */
    public void pushFromTop(int column, NavigationTile tile) {

        Player player = board[board.length - 1][column].getPlayer();

        Mataha.getGame().getTileBag().returnTile(board[board.length - 1][column].getTile());

        for (int i = board.length - 1; i > 0; i--) {
            board[i][column].setTile(board[i - 1][column].getTile());
            board[i][column].setPlayer(board[i - 1][column].getPlayer());
        }
        board[0][column].setTile(tile);
        board[0][column].setPlayer(player);
    }

    /**
     * @param column the column to insert into the bottom of
     * @param tile   the main.tile to insert
     */
    public void pushFromBottom(int column, NavigationTile tile) {

        Player player = board[0][column].getPlayer();

        Mataha.getGame().getTileBag().returnTile(board[0][column].getTile());

        for (int i = 0; i < board[0].length - 1; i++) {
            board[i][column].setTile(board[i + 1][column].getTile());
            board[i][column].setPlayer(board[i + 1][column].getPlayer());
        }
        board[board.length - 1][column].setTile(tile);
        board[board.length - 1][column].setPlayer(player);
    }

    /**
     * @param boardSquares sets the boardsquare 2d array that represents this boards contents
     */
    public void setBoardSquares(BoardSquare[][] boardSquares) {
        this.board = boardSquares;

        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board[0].length; column++) {
                this.add(this.board[row][column], column + 1, row + 1);
            }
        }
    }

    /**
     * creates the buttons that allow tiles to be inserted
     */
    public void generateInsertTileButtons() {

        //left tiles
        for (int i = 0; i < board.length; i++) {
            Button button = new Button("→");
            insertButtonMap.put("left;" + i, button);
            if (rowContainsFixed(i)) {
                button.setDisable(true);
            }
            int finalI = i;
            button.setOnMouseClicked(event -> {
                NavigationTile tile = Mataha.getGame().getCurrentPlayer().getHeldNavigationTile();
                pushFromLeft(finalI, tile);
                Mataha.getGame().getCurrentPlayer().setHeldNavigationTile(null);
                Mataha.getGame().getGameBoard().hideInserTileButtons();
                Mataha.getGame().getBottomPanel().displayPlayerTiles();
                Mataha.getGame().getGameInfoPanel().getTipLabel().setPlayEffectText();
                Mataha.getGame().getGameInfoPanel().showNavNextButton();
                Mataha.getGame().getDrawCardPanel().hideRotationButtons();
            });
            this.add(button, 0, i + 1);
        }

        //right tiles
        for (int i = 0; i < board.length; i++) {
            Button button = new Button("<-");
            insertButtonMap.put("right;" + i, button);
            if (rowContainsFixed(i)) {
                button.setDisable(true);
            }
            int finalI = i;
            button.setOnMouseClicked(event -> {
                NavigationTile tile = Mataha.getGame().getCurrentPlayer().getHeldNavigationTile();
                pushFromRight(finalI, tile);
                Mataha.getGame().getCurrentPlayer().setHeldNavigationTile(null);
                Mataha.getGame().getGameBoard().hideInserTileButtons();
                Mataha.getGame().getBottomPanel().displayPlayerTiles();
                Mataha.getGame().getGameInfoPanel().getTipLabel().setPlayEffectText();
                Mataha.getGame().getGameInfoPanel().showNavNextButton();
                Mataha.getGame().getDrawCardPanel().hideRotationButtons();
            });
            this.add(button, board[0].length + 1, i + 1);
        }

        // top tiles
        for (int i = 0; i < board[0].length; i++) {
            Button button = new Button("↓");
            insertButtonMap.put("top;" + i, button);
            if (columnContainsFixed(i)) {
                button.setDisable(true);
            }
            int finalI = i;
            button.setOnMouseClicked(event -> {
                NavigationTile tile = Mataha.getGame().getCurrentPlayer().getHeldNavigationTile();
                pushFromTop(finalI, tile);
                Mataha.getGame().getCurrentPlayer().setHeldNavigationTile(null);
                Mataha.getGame().getGameBoard().hideInserTileButtons();
                Mataha.getGame().getBottomPanel().displayPlayerTiles();
                Mataha.getGame().getGameInfoPanel().getTipLabel().setPlayEffectText();
                Mataha.getGame().getGameInfoPanel().showNavNextButton();
                Mataha.getGame().getDrawCardPanel().hideRotationButtons();
            });
            this.add(button, i + 1, 0);
        }

        //bottom tiles
        for (int i = 0; i < board[0].length; i++) {
            Button button = new Button("↑");
            insertButtonMap.put("bottom;" + i, button);
            if (columnContainsFixed(i)) {
                button.setDisable(true);
            }
            int finalI = i;
            button.setOnMouseClicked(event -> {
                NavigationTile tile = Mataha.getGame().getCurrentPlayer().getHeldNavigationTile();
                pushFromBottom(finalI, tile);
                Mataha.getGame().getCurrentPlayer().setHeldNavigationTile(null);
                Mataha.getGame().getGameBoard().hideInserTileButtons();
                Mataha.getGame().getBottomPanel().displayPlayerTiles();
                Mataha.getGame().getGameInfoPanel().getTipLabel().setPlayEffectText();
                Mataha.getGame().getGameInfoPanel().showNavNextButton();
                Mataha.getGame().getDrawCardPanel().hideRotationButtons();
            });
            this.add(button, i + 1, board.length + 1);
        }
    }

    /**
     * @param row the row to check
     * @return if the row contains a fixed main.tile
     */
    private boolean rowContainsFixed(int row) {
        for (int i = 0; i < board[0].length; i++) {
            if (board[row][i].isFixed()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param column the column to check
     * @return if the column contains a fixed main.tile
     */
    private boolean columnContainsFixed(int column) {
        for (int i = 0; i < board.length; i++) {
            if (board[i][column].isFixed()) {
                return true;
            }
        }
        return false;
    }

    public void showInsertTileButtons() {
        for (Node n : this.getChildren()) {
            if (n instanceof Button) {
                n.setVisible(true);
            }
        }
    }

    public void hideInserTileButtons() {
        for (Node n : this.getChildren()) {
            if (n instanceof Button) {
                n.setVisible(false);
            }
        }
    }

    /**
     * Called when a main.tile becomes fixed, updates the insert tiles buttons to disable for fixed rows and columns
     *
     * @param fixedRow    the row to be disabled
     * @param fixedColumn the column to be disables
     * @param fixed
     */
    public void updateInsertButtonsForFixed(int fixedRow, int fixedColumn, boolean fixed) {
        String key;
        Button button;

        key = "left;" + fixedRow;
        button = insertButtonMap.get(key);
        button.setDisable(fixed);

        key = "right;" + fixedRow;
        button = insertButtonMap.get(key);
        button.setDisable(fixed);

        key = "top;" + fixedColumn;
        button = insertButtonMap.get(key);
        button.setDisable(fixed);

        key = "bottom;" + fixedColumn;
        button = insertButtonMap.get(key);
        button.setDisable(fixed);
    }

    /**
     * @param row the row to search
     * @return all the players in a given row
     */
    private ArrayList<Player> getPlayersInRow(int row) {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            BoardSquare bs = board[row][i];
            if (bs.getPlayer() != null) {
                players.add(bs.getPlayer());
            }
        }
        return players;
    }

    /**
     * unused
     *
     * @param column the column to search
     * @return all the players in a given column
     */
    private ArrayList<Player> getPlayersInColumn(int column) {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            BoardSquare bs = board[i][column];
            if (bs.getPlayer() != null) {
                players.add(bs.getPlayer());
            }
        }
        return players;
    }

    /**
     *
     */
    public void updateAllButtonsForFire() {
        for (int row = 0; row < board.length; row++) {
            updateInsertButtonsForFire(row, 0);
        }
        for (int column = 0; column < board[0].length; column++) {
            updateInsertButtonsForFire(0, column);
        }
    }

    /**
     * Checks for a given row or column whether moving the row would move a player onto a fire main.tile.
     * If so, disables the insert main.tile button
     *
     * @param row
     * @param column
     */
    public void updateInsertButtonsForFire(int row, int column) {
        Button button;
        String key;

        key = "left;" + row;
        button = insertButtonMap.get(key);
        for (Player player : getPlayersInRow(row)) {
            int x = player.getPlayerX() + 1;
            int y = player.getPlayerY();
            if (outOfBounds(x, y)) continue;
            ArrayList<Effect> boardSquareEffects = board[y][x].getActiveEffects();
            if (boardSquareEffects.size() != 0 && boardSquareEffects.get(0).getEffectName().equals("fire")) {
                button.setDisable(true);
            } else {
                button.setDisable(false);
            }
        }

        key = "right;" + row;
        button = insertButtonMap.get(key);
        for (Player player : getPlayersInRow(row)) {
            int x = player.getPlayerX() - 1;
            int y = player.getPlayerY();
            if (outOfBounds(x, y)) continue;
            ArrayList<Effect> boardSquareEffects = board[y][x].getActiveEffects();
            if (boardSquareEffects.size() != 0 && boardSquareEffects.get(0).getEffectName().equals("fire")) {
                button.setDisable(true);
            } else {
                button.setDisable(false);
            }
        }

        key = "top;" + column;
        button = insertButtonMap.get(key);
        for (Player player : getPlayersInColumn(column)) {
            int x = player.getPlayerX();
            int y = player.getPlayerY() + 1;
            if (outOfBounds(x, y)) continue;
            ArrayList<Effect> boardSquareEffects = board[y][x].getActiveEffects();
            if (boardSquareEffects.size() != 0 && boardSquareEffects.get(0).getEffectName().equals("fire")) {
                button.setDisable(true);
            } else {
                button.setDisable(false);
            }
        }

        key = "bottom;" + column;
        button = insertButtonMap.get(key);
        for (Player player : getPlayersInColumn(column)) {
            int x = player.getPlayerX();
            int y = player.getPlayerY() - 1;
            if (outOfBounds(x, y)) continue;
            ArrayList<Effect> boardSquareEffects = board[y][x].getActiveEffects();
            if (boardSquareEffects.size() != 0 && boardSquareEffects.get(0).getEffectName().equals("fire")) {
                button.setDisable(true);
            } else {
                button.setDisable(false);
            }
        }
    }

    /**
     * Returns true if an x or y coordinate would fall outside the bounds of the array
     *
     * @param x
     * @param y
     * @return
     */
    private boolean outOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= board[0].length || y >= board[0].length;
    }

    /**
     * Fills up the blank square on the board from the main.tile bag
     */
    public void fillFromTileBag() {
        TileBag tileBag = new TileBag();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                if (board[row][column] != null) continue;
                Tile tile = tileBag.getTile();
                while (!(tile instanceof NavigationTile)) {
                    tileBag.returnTile(tile);
                    tile = tileBag.getTile();
                }
                NavigationTile navTile = (NavigationTile) tile;
                board[row][column] = new BoardSquare(navTile, column, row);
            }
        }
    }

    /**
     * Sets all the on click and mouse enter listeners for the boardsquare
     */
    private void setBoardSquareListeners() {
        for (Node n : this.getChildren()) {
            if (!(n instanceof BoardSquare)) return;
            BoardSquare bs = (BoardSquare) n;
            bs.setOnMouseEntered(event -> {
                bs.setMouseHover(true);
            });
            bs.setOnMouseExited(event -> {
                bs.setMouseHover(false);
            });
            bs.setOnMouseClicked(event -> {

                if (Mataha.getGame().getBoardState() == 0) return;

                if (Mataha.getGame().getBoardState() == 1) {
                    if (bs.surroundingSquaresContainPlayer()) return;
                    Effect effect = new Effect("ice");
                    effect.setIce(bs);
                }
                if (Mataha.getGame().getBoardState() == 2) {
                    if (bs.surroundingSquaresContainPlayer()) return;
                    Effect effect = new Effect("fire");
                    if (bs.getPlayer() == null) {
                        if (effect.isAreaFree(bs)) {
                            effect.setFire(bs, Mataha.getGame().getCurrentPlayer());
                        }
                    }

                }
                if (Mataha.getGame().getBoardState() == 3) {
                    Effect effect = new Effect("backtrack");
                    if (bs.getPlayer() != null) {
                        effect.setBacktrack(bs.getPlayer());
                    }
                }
                if (Mataha.getGame().getBoardState() == 4) {
                    Mataha.getGame().getCurrentPlayer().setNumMoves(2);
                }

                Mataha.getGame().getBottomPanel().getPlayerTilesPanel().removeEffectTile();
                Mataha.getGame().setBoardState(0);
                Mataha.getGame().initNavigationUI();
            });
        }
    }

    public enum INPUTMODE {
        NONE(0),
        FIRE(1),
        ICE(2),
        BACKTRACK(3),
        DOUBLEMOVE(4);
        private int num;

        private INPUTMODE(int num) {
            this.num = num;
        }
    }

    public void redrawTiles() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col].getTile().setTileImage();
            }
        }
    }

}
