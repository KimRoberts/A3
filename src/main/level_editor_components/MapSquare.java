package main.level_editor_components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import main.BoardSquare;
import main.UIOverlays.StartingPositionOverlay;
import main.stage.LevelEditorStage;
import main.tile.*;

/**
 * MapSquare is each of the individual tiles of the map where the user can assign navigation tiles of their choosing
 *
 * @author G38.dev
 */
public class MapSquare extends StackPane {

    private static String NO_TILE_IMAGE = "src\\resources\\Images\\game_images\\notile.jpg";

    private LevelEditorStage context;
    private NavigationTile tile;
    private boolean startingPosition;
    private int x;
    private int y;

    private StartingPositionOverlay overlay;

    /**
     * Constructor method for MapSquare
     *
     * @param context - The current LevelEditorStage
     * @param x       - x position of map square
     * @param y       - y position of map square
     */
    public MapSquare(LevelEditorStage context, int x, int y) {
        this.x = x;
        this.y = y;
        this.context = context;
        startingPosition = false;
        setTile(null);

        // When clicked, conditionally place a tile, remove a tile or set a starting position dependant on the
        // InputMode attribute of the LevelEditorStage
        this.setOnMouseClicked(event -> {
            if (context.getInputMode() == LevelEditorStage.INPUT_MODE.TILE) {
                NavigationTile tileInHand = cloneTile(context.getTileInHand());
                if (tileInHand instanceof GoalTile) context.getMap().removeGoalSquare();
                this.getChildren().clear();
                setTile(tileInHand);
            } else {
                this.setStartingPosition(!startingPosition);
            }
        });
    }

    /**
     * Method that gets the navigation tile on this map square currently
     *
     * @return the navigation tile on this map square currently
     */
    public NavigationTile getTile() {
        return tile;
    }

    /**
     * Method that sets relevant navigation tile
     *
     * @param tile - The navigation tile to be placed in current board square
     */
    public void setTile(NavigationTile tile) {

        // remove tile and set the image to be a blank space with no tile
        if (tile == null) {
            this.tile = null;
            ImageView imageView = new ImageView();
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            imageView.setImage(new Image(NO_TILE_IMAGE));
            this.getChildren().add(imageView);
            if (startingPosition) {
                this.setStartingPosition(false);
                this.setStartingPosition(true);
            }
        } else {
            // remove current tile
            if (this.tile != null) {
                this.getChildren().remove(tile);
            }
            // add new tile
            this.tile = tile;
            this.getChildren().add(tile);
            if (this.isStartingPosition()) {
                this.setStartingPosition(false);
                this.setStartingPosition(true);
            }
        }
    }

    /**
     * Method that creates a new instance of a given type of tile
     *
     * @param tile - the tile to be cloned
     * @return navigation tile
     */
    public NavigationTile cloneTile(NavigationTile tile) {
        NavigationTile toReturn = null;

        if (tile instanceof GoalTile) {
            toReturn = new GoalTile(tile.getRotation());
        } else if (tile instanceof LineTile) {
            toReturn = new LineTile(tile.getRotation(), true);
        } else if (tile instanceof CornerTile) {
            toReturn = new CornerTile(tile.getRotation(), true);
        } else if (tile instanceof TShapeTile) {
            toReturn = new TShapeTile(tile.getRotation(), true);
        }
        return toReturn;
    }

    /**
     * Method that checks if the navigation tile has paths in a given direction
     *
     * @param direction - The directions that needs to be checked
     * @return true if path in given direction is possible, false if not
     */
    public boolean hasPath(NavigationTile.DIRECTION direction) {
        switch (direction) {
            case UP:
                return tile == null || tile.hasPath(NavigationTile.DIRECTION.UP);
            case RIGHT:
                return tile == null || tile.hasPath(NavigationTile.DIRECTION.RIGHT);
            case DOWN:
                return tile == null || tile.hasPath(NavigationTile.DIRECTION.DOWN);
            case LEFT:
                return tile == null || tile.hasPath(NavigationTile.DIRECTION.LEFT);
            default:
                return false;
        }
    }

    /**
     * Method that gets the x position of map square
     *
     * @return the x position of the map square
     */
    public int getX() {
        return x;
    }

    /**
     * Method that gets the y position of map square
     *
     * @return the y position of the map square
     */
    public int getY() {
        return y;
    }

    /**
     * Method that checks if the map square is a starting position
     *
     * @return true if map square is a starting position, false if not
     */
    public boolean isStartingPosition() {
        return startingPosition;
    }

    /**
     * Method that sets the starting position
     *
     * @param startingPosition - Whether the map square is a starting position or not
     */
    public void setStartingPosition(boolean startingPosition) {

        // goal tiles cannot be starting positions
        if (tile instanceof GoalTile) {
            return;
        }

        // if the map already has 4 starting positions, try and call removeNearestStartingPosition
        // to reduce the number of starting positions on the map
        if (startingPosition && context.getMap().getNumStartingPosition() == 4
                && !context.getMap().removeNearestStartingPosition(this.x, this.y)) {
            return;
        }

        this.startingPosition = startingPosition;
        if (startingPosition) {
            this.getChildren().add(getOverlay());
        } else {
            this.getChildren().remove(getOverlay());
        }
    }

    /**
     * Method that gets the starting position overlay and creates a new one if one doesn't already exist
     *
     * @return the starting position overlay
     */
    private StartingPositionOverlay getOverlay() {
        if (overlay == null) {
            overlay = new StartingPositionOverlay();
        }
        return overlay;
    }

    /**
     * Method that makes a new board square
     *
     * @return the board square created
     */
    public BoardSquare toBoardSquare() {
        return new BoardSquare(this.tile, this.x, this.y);
    }

    /**
     * Method that serialises the map square contents so they can be appended to the save file.
     *
     * @return the sting containing details about the map square such as position and whether it is a starting position
     */
    public String serialise() {
        if (tile == null) {
            return "";
        }
        return x + "," + y + "," + isStartingPosition() + "," + tile.preciseToString() + "\n";
    }
}
