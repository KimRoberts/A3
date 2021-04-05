package main.level_editor_components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import main.Effect;
import main.Utils;
import main.stage.LevelEditorStage;
import main.tile.*;

import java.util.ArrayList;

/**
 * TileSelectionPanel is a visual component that is rendered in the LevelEditor Scene. It allows users to select
 * a certain NavigationTile that can be added to the map. This component houses a number of TileStackPanes and manages
 * their state
 *
 * @author G38.dev
 */
public class TileSelectionPanel extends VBox {

    private LevelEditorStage context;

    private TileStackPane goalTile;
    private TileStackPane lineTile;
    private TileStackPane cornerTile;
    private TileStackPane tShapeTile;
    private Button rotate;


    /**
     * Constructs an instance of the TileSelectionPanel with a reference to its parent container
     * @param context a reference to the LevelEditorStage that this is rendered in
     */
    public TileSelectionPanel(LevelEditorStage context){

        this.context = context;

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.setPrefWidth(200);
        initUI();
        setListeners();
    }


    /**
     * Initialises the UI components that render within this class and adds them to the children ArrayList
     */
    private void initUI() {

        lineTile = new TileStackPane(new LineTile(0, false), context);
        cornerTile = new TileStackPane(new CornerTile(0, false), context);
        tShapeTile = new TileStackPane(new TShapeTile(0, false), context);
        goalTile = new TileStackPane(new GoalTile(0), context);
        rotate = new Button("Rotate");

        rotate.setText(Utils.translate("Rotate", main.MainMenu.getLang()));

        this.getChildren().addAll(lineTile, goalTile, cornerTile, tShapeTile, rotate);

    }

    /**
     * Sets the click listener for the rotate button
     */
    private void setListeners() {

        rotate.setOnMouseClicked(event -> {
            lineTile.getNavigationTile().rotateClockwise();
            cornerTile.getNavigationTile().rotateClockwise();
            tShapeTile.getNavigationTile().rotateClockwise();
        });
    }

    /**
     * Removes the highlight overlay component of all the TileStackPanes contained within this component
     */
    public void removeAllHighlightOverlays(){
        for (Node child : this.getChildren()){
            if (child instanceof TileStackPane){
                TileStackPane panel = (TileStackPane) child;
                panel.removeHighlightOverlay();
            }
        }
    }


    /**
     * Calls all the Tile attributes of the TileStackPane components of to redraw their image. This may be called
     * in response to global UI changes that need to be reflected in smaller components such as texture packs
     */
    public void updateUI(){
        lineTile.getNavigationTile().setTileImage();
        cornerTile.getNavigationTile().setTileImage();
        tShapeTile.getNavigationTile().setTileImage();
        goalTile.getNavigationTile().setTileImage();
    }
}
