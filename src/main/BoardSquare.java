package main;

import main.UIOverlays.FireOverlay;
import main.UIOverlays.FixedOverlay;
import main.UIOverlays.HighlightOverlay;
import main.UIOverlays.IceOverlay;
import javafx.scene.layout.StackPane;
import main.tile.NavigationTile;
import main.tile.TileFactory;

import java.util.ArrayList;

/**
 * Class represents a board square.
 *
 * @author G38.dev
 */
public class BoardSquare extends StackPane {

    public static final int SIZE = 40;
    private NavigationTile tile;
    private Player player;
    private boolean isFixed;
    private ArrayList<Effect> activeEffectList;
    private String imgUrl;
    private boolean isMouseHover;
    private FireOverlay fireOverlay = new FireOverlay();
    private IceOverlay iceOverlay = new IceOverlay();
    private HighlightOverlay highlightOverlay = new HighlightOverlay();
    private FixedOverlay fixedOverlay;
    private int xPos;
    private int yPos;

    public BoardSquare(NavigationTile tile, int xPos, int yPos) {
        setTile(tile);
        this.isFixed = tile.isFixed();
        if (this.isFixed) {
            fixedOverlay = new FixedOverlay();
            this.getChildren().add(fixedOverlay);
        }
        activeEffectList = new ArrayList<>();
        this.player = null;
        this.setPrefSize(SIZE, SIZE);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public boolean isMouseHover() {
        return isMouseHover;
    }

    /**
     * Method sets the overlay for the mouse hover.
     *
     * @param mouseHover set weather the mouse is considered hovering over this main tile or not.
     */
    public void setMouseHover(boolean mouseHover) {
        isMouseHover = mouseHover;
        if (Mataha.getGame().getBoardState() == 0) return;
        boolean canSelect = mouseHover && highlightOverlay == null && activeEffectList.size() == 0;
        boolean canDeselect = !mouseHover && !(highlightOverlay == null);
        if (canSelect) {
            highlightOverlay = new HighlightOverlay();
            this.getChildren().add(highlightOverlay);
        }
        if (canDeselect) {
            this.getChildren().remove(highlightOverlay);
            highlightOverlay = null;
        }
    }

    public NavigationTile getTile() {
        return tile;
    }

    /**
     * Method sets main.tile.
     *
     * @param tile the actual main.tile that should occupy this boardsquare
     */
    public void setTile(NavigationTile tile) {
        this.tile = tile;
        if (!this.getChildren().contains(this.tile)) {
            this.getChildren().add(0, this.tile);
        }
    }

    public boolean isFixed() {
        return isFixed;
    }

    /**
     * Method sets a main.tile to update to fixed or not.
     *
     * @param fixed whether the main.tile should be movable or not
     */
    public void setFixed(boolean fixed) {
        isFixed = fixed;
        Mataha.getGame().getGameBoard().updateInsertButtonsForFixed(yPos, xPos, fixed);
        if (this.isFixed) {
            fixedOverlay = new FixedOverlay();
            this.getChildren().add(fixedOverlay);
        } else {
            this.getChildren().remove(fixedOverlay);
        }
    }

    public ArrayList<Effect> getActiveEffects() {
        return activeEffectList;
    }

    /**
     * Method adds active effects.
     *
     * @param effect the effect to add to this square
     */
    public void addActiveEffects(Effect effect) {
        activeEffectList.add(effect);
        if (effect.getEffectName().equals("fire")) {
            addFire();
        } else if (effect.getEffectName().equals("ice")) {
            addIce();
        }
    }

    /**
     * Adds the ice effect to the square.
     */
    private void addIce() {
        if (this.highlightOverlay != null) {
            this.getChildren().remove(highlightOverlay);
        }
        this.getChildren().add(iceOverlay);
    }

    /**
     * Adds the fire effect to the square.
     */
    private void addFire() {
        if (this.highlightOverlay != null) {
            this.getChildren().remove(highlightOverlay);
        }

        this.getChildren().add(fireOverlay);
        Mataha.getGame().getGameBoard().updateInsertButtonsForFire(this.yPos, this.xPos);
    }

    public void removeIce() {
        if (iceOverlay == null) return;
        if (this.getChildren().contains(fixedOverlay)) {
            this.getChildren().remove(fixedOverlay);
        }
        this.getChildren().remove(iceOverlay);
    }

    public void removeFire() {
        if (fireOverlay == null) return;
        this.getChildren().remove(fireOverlay);
    }

    /**
     * Method removes the active effetct.
     *
     * @param effect the effect to remove from this main.tile
     */
    public void removeActiveEffect(Effect effect) {
        int removeIndex;
        removeIndex = activeEffectList.indexOf(effect);
        activeEffectList.remove(removeIndex);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Method sets players accordingly.
     *
     * @param player the player this tile now has on it.
     */
    public void setPlayer(Player player) {
        this.getChildren().remove(this.player);
        this.player = player;
        if (player == null) return;
        this.getChildren().add(player);
        player.draw();
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    @Override
    public String toString() {
        return xPos + "," + yPos;
    }

    /**
     * @return a serialised version of this BoardSquare
     */
    public String serialise() {
        String effects = "";
        for (Effect effect : activeEffectList) {
            effects = effects + effect.getEffectName() + ":" + effect.getTime() + ",";
        }
        String pname = "";
        String purl = "";
        if (getPlayer() != null) {
            pname = getPlayer().getName();
            purl = getPlayer().getCharacterURL();
        }


        //x,y,player name. player url,fixed,rotation,tiletype,effects..
        return xPos + "," + yPos + "," + pname + "," + purl + "," + isFixed + "," + getTile().getRotation() + ","
                + this.getTile().toString() + "," + effects;
    }

    /**
     * @param input takes a string boardsquare
     * @return a boardsquare object constructed from the serialised version
     */
    public static BoardSquare deserialise(String input) {
        String[] csv = input.split(",");
        System.out.println(input);
        NavigationTile tile = TileFactory.getTile(csv[6], Integer.parseInt(csv[5]), false);
        if (csv[4].equals("true")) {
            tile = TileFactory.getTile(csv[6], Integer.parseInt(csv[5]), true);
        }

        BoardSquare boardSquare = new BoardSquare(tile, Integer.parseInt(csv[0]), Integer.parseInt(csv[1]));

        if (csv[4].equals("true")) {
            boardSquare.setFixed(true);
        } else {
            boardSquare.setFixed(false);
        }
        for (int i = 7; i < csv.length; i++) {
            String[] eff = csv[i].split(":");
            boardSquare.addActiveEffects(new Effect(eff[0], Integer.parseInt(eff[1])));
        }
        if (!csv[2].equals("")) {
            boardSquare.setPlayer(new Player(Profile.getProfile(csv[2]), csv[3]));
        }
        boardSquare.setOnMouseEntered(event -> {
            boardSquare.setMouseHover(true);
        });
        boardSquare.setOnMouseExited(event -> {
            boardSquare.setMouseHover(false);
        });
        boardSquare.setOnMouseClicked(event -> {

            if (Mataha.getGame().getBoardState() == 0) return;

            if (Mataha.getGame().getBoardState() == 1) {
                Effect effect = new Effect("ice");
                effect.setIce(boardSquare);
            }
            if (Mataha.getGame().getBoardState() == 2) {
                Effect effect = new Effect("fire");
                if (boardSquare.getPlayer() == null) {
                    if (effect.isAreaFree(boardSquare)) {
                        effect.setFire(boardSquare, Mataha.getGame().getCurrentPlayer());
                    }
                }

            }
            if (Mataha.getGame().getBoardState() == 3) {
                Effect effect = new Effect("backtrack");
                if (boardSquare.getPlayer() != null) {
                    effect.setBacktrack(boardSquare.getPlayer());
                }
            }
            if (Mataha.getGame().getBoardState() == 4) {
                Effect effect = new Effect("doublemove");
                if (boardSquare.getPlayer() != null) {
                    effect.setDoubleMove(boardSquare.getPlayer());
                }
            }

            Mataha.getGame().getBottomPanel().getPlayerTilesPanel().removeEffectTile();
            Mataha.getGame().setBoardState(0);
            Mataha.getGame().initNavigationUI();
        });


        return boardSquare;
    }

    /**
     * Method checks for surrounding players.
     *
     * @return true or false if there is a player surrounding.
     */
    public boolean surroundingSquaresContainPlayer() {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (Utils.outOfBounds(x + xPos, y + yPos, Mataha.getGame().getGameBoard().getBoard())) continue;
                BoardSquare boardSquare = Mataha.getGame().getGameBoard().getBoard()[yPos + y][xPos + x];
                if (boardSquare.getPlayer() != null) {
                    return true;
                }
            }
        }
        return false;
    }


}
