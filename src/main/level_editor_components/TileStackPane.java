package main.level_editor_components;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import main.UIOverlays.HighlightOverlay;
import main.stage.LevelEditorStage;
import main.tile.NavigationTile;

/**
 * TileStackPane is a visual component that renders within the TileSelectionPanel parent container. It houses a
 * NavigationTile and a HighLightOverlay to display when the tile is selected.
 *
 * @author G38.dev
 */
public class TileStackPane extends StackPane {

    private LevelEditorStage context;
    private NavigationTile navigationTile;
    private HighlightOverlay highlightOverlay;

    /**
     * Constructs an instance of the TileStackPane with a given tile and a reference to the LevelEditor scene
     *
     * @param tile    - the tile to be displayed in the stack pane
     * @param context - a reference to the LevelEditorStage that this component exists within
     */
    public TileStackPane(NavigationTile tile, LevelEditorStage context) {
        this.context = context;
        this.setWidth(40);
        this.setHeight(40);
        this.navigationTile = tile;
        this.getChildren().add(tile);

        /*
            If the input mode is in starting position mode, it does not get selected.
            Otherwise it either selects or deselects this component, changing the held tile in the LevelEditorFile
            class and adding a highlight overlay
         */
        this.setOnMouseClicked(event -> {
            if (context.getInputMode() == LevelEditorStage.INPUT_MODE.START_POSITION) {
                return;
            }
            if (context.getTileInHand() == null || !(context.getTileInHand().equals(tile))) {
                context.getTileSelectionPanel().removeAllHighlightOverlays();
                addHighlightOverlay();
                context.setTileInHand(tile);
            } else {
                removeHighlightOverlay();
                context.setTileInHand(null);
            }
        });
    }

    /**
     * This method ads an instance of the HighlighOverlay class that indicates that this tileStackPane has
     * been selected
     */
    private void addHighlightOverlay() {
        if (highlightOverlay == null) {
            highlightOverlay = new HighlightOverlay();
        }

        this.getChildren().add(highlightOverlay);
    }

    /**
     * Removes the instance of the HighlightOverlay to indicate that this is no longer selected
     */
    public void removeHighlightOverlay() {
        if (highlightOverlay != null) {
            this.getChildren().remove(highlightOverlay);
            highlightOverlay = null;
        }
    }


    /**
     * @return the navigation tile stored in this stack pane
     */
    public NavigationTile getNavigationTile() {
        return navigationTile;
    }
}
