package main;

import javafx.scene.control.Label;

/***
 * This class adds Tips to the game, this is to help the player play the game.
 * @author G38.dev
 * @version 4
 */
public class TipLabel extends Label {

    public static final String DRAW_TILE = "get a tile from the tile bag.";
    public static final String PLAY_NAVIGATION_TILE = "insert the tile into a row  or column " +
            "where there are no fixed tiles.";
    public static final String PLAY_EFFECT_TILE = "you may now play an effect tile if you have any.";
    public static final String CHOOSE_PLAYER = "choose a player to cast this effect on.";
    public static final String CHOOSE_BOARD_SQUARE = "choose a board square to cast this effect on.";
    public static final String MOVE = "move your player to a free space on the board.";
    public static final String CANNOT_MOVE = "your player cannot move and therefore you skip this turn.";
    public static final String WIN_1 = "congratulations, you have won the game!";
    public static final String WIN_2 = "you have won the game!";

    private Game game;

    public TipLabel(Game game) {
        this.game = game;
        this.setPrefWidth(180);
        this.setWrapText(true);

    }

    public void setDrawTileText() {
        String playerName = game.getCurrentPlayer().getName();
        this.setText(playerName + ", " + Utils.translate(DRAW_TILE, MainMenu.getLang()));
    }

    public void setPlayNavigationText() {
        String playerName = game.getCurrentPlayer().getName();
        this.setText(playerName + ", " + Utils.translate(PLAY_NAVIGATION_TILE, MainMenu.getLang()));
    }

    public void setPlayEffectText() {
        String playerName = game.getCurrentPlayer().getName();
        this.setText(playerName + ", " + Utils.translate(PLAY_EFFECT_TILE, MainMenu.getLang()));
    }

    public void setMoveText() {
        String playerName = game.getCurrentPlayer().getName();
        this.setText(playerName + ", " + Utils.translate(MOVE, MainMenu.getLang()));
    }

    public void setCannotMoveText() {
        String playerName = game.getCurrentPlayer().getName();
        this.setText(playerName + ", " + Utils.translate(CANNOT_MOVE, MainMenu.getLang()));
        Mataha.getGame().getGameInfoPanel().showNextButton();
    }

    public void setWinText() {
        String playerName = game.getCurrentPlayer().getName();
        this.setText(playerName + ", " + Utils.translate(WIN_1, MainMenu.getLang()) + ", " +
                Utils.translate(WIN_2, MainMenu.getLang()));
    }

    public void clearText() {
        this.setText("");
    }
}