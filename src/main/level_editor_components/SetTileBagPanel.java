package main.level_editor_components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import main.Effect;
import main.TileBag;
import main.stage.LevelEditorStage;
import main.tile.*;

import javax.sound.sampled.Line;
import java.util.ArrayList;

/**
 * SetTileBagPanel is the panel in the middle right of the level editor screen that allows the user to input the
 * number of each type of tile in the tilebag
 *
 * @author G38.dev
 */
public class SetTileBagPanel extends VBox {

    TileBagComponent cornerComponent;
    TileBagComponent lineComponent;
    TileBagComponent tshapeComponent;
    TileBagComponent backtrackComponent;
    TileBagComponent doublemoveComponent;
    TileBagComponent fireComponent;
    TileBagComponent iceComponent;

    private LevelEditorStage context;

    /**
     * Constructor method for SetTileBagPanel
     *
     * @param context - The current LevelEditorStage
     */
    public SetTileBagPanel(LevelEditorStage context) {
        this.context = context;
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(30));
        this.setSpacing(10);
        initUI();
    }

    /**
     * Method that initialises the user interface of the tile bag panel
     */
    private void initUI() {
        Label titleLabel = new Label("Tile Bag Contents");
        titleLabel.setText(main.Utils.translate("Tile Bag Contents", main.MainMenu.getLang()));
        titleLabel.setPadding(new Insets(20));

        cornerComponent = new TileBagComponent(TileBagComponent.TILE_IMAGE.CORNER);
        lineComponent = new TileBagComponent(TileBagComponent.TILE_IMAGE.LINE);
        tshapeComponent = new TileBagComponent(TileBagComponent.TILE_IMAGE.TSHAPE);
        backtrackComponent = new TileBagComponent(TileBagComponent.TILE_IMAGE.BACKTRACK);
        doublemoveComponent = new TileBagComponent(TileBagComponent.TILE_IMAGE.DOUBLEMOVE);
        fireComponent = new TileBagComponent(TileBagComponent.TILE_IMAGE.FIRE);
        iceComponent = new TileBagComponent(TileBagComponent.TILE_IMAGE.ICE);

        this.getChildren().addAll(titleLabel, cornerComponent, lineComponent, tshapeComponent, backtrackComponent,
                doublemoveComponent, fireComponent, iceComponent);
    }

    /**
     * Method that gets the total number of navigation tiles in the tile bag
     *
     * @return the total number of navigation tiles in the tile bag
     */
    public int getNumNavTiles() {
        return cornerComponent.getValue() + lineComponent.getValue() + tshapeComponent.getValue();
    }

    /**
     * Method that gets all the tiles in the tile bag
     *
     * @return an arraylist of all the tiles in the tilebag, including both navigation and effect tiles
     */
    public ArrayList<Tile> getTiles() {
        ArrayList<Tile> tiles = new ArrayList<>();

        // Create n amount of each type of tile and add them to the array list, where n is the value in the input
        // box for each component
        for (int i = 0; i < cornerComponent.getValue(); i++) {
            int rotation = (int) (Math.random() * 3 + 1);
            Tile cornerTile = new CornerTile(rotation, false);
            tiles.add(cornerTile);
        }

        for (int i = 0; i < lineComponent.getValue(); i++) {
            int rotation = (int) (Math.random() * 3 + 1);
            Tile lineTile = new LineTile(rotation, false);
            tiles.add(lineTile);
        }

        for (int i = 0; i < tshapeComponent.getValue(); i++) {
            int rotation = (int) (Math.random() * 3 + 1);
            Tile tShapeTile = new TShapeTile(rotation, false);
            tiles.add(tShapeTile);
        }

        for (int i = 0; i < backtrackComponent.getValue(); i++) {
            Tile backtrackTile = new EffectTile(new Effect("backtrack"));
            tiles.add(backtrackTile);
        }

        for (int i = 0; i < doublemoveComponent.getValue(); i++) {
            Tile doublemoveTile = new EffectTile(new Effect("doublemove"));
            tiles.add(doublemoveTile);
        }

        for (int i = 0; i < fireComponent.getValue(); i++) {
            Tile fireTile = new EffectTile(new Effect("fire"));
            tiles.add(fireTile);
        }

        for (int i = 0; i < iceComponent.getValue(); i++) {
            Tile iceTile = new EffectTile(new Effect("ice"));
            tiles.add(iceTile);
        }

        return tiles;
    }

    /**
     * Method that sets the number of each type of tile in the tile bag
     *
     * @param tileBag - An arraylist of all the tiles in the tile bag
     */
    public void setTileNumbers(ArrayList<Tile> tileBag) {
        lineComponent.setVal(0);
        cornerComponent.setVal(0);
        tshapeComponent.setVal(0);
        fireComponent.setVal(0);
        iceComponent.setVal(0);
        backtrackComponent.setVal(0);
        doublemoveComponent.setVal(0);

        // For each tile passed in, increase the value in the component that corresponds to that type of tile
        for (Tile t : tileBag) {
            if (t instanceof LineTile) {
                lineComponent.increaseVal();
            } else if (t instanceof CornerTile) {
                cornerComponent.increaseVal();
            } else if (t instanceof TShapeTile) {
                tshapeComponent.increaseVal();
            } else if (t instanceof EffectTile) {
                EffectTile e = (EffectTile) t;
                if (e.getTileEffect().getEffectName().equals("fire")) {
                    fireComponent.increaseVal();
                } else if (e.getTileEffect().getEffectName().equals("ice")) {
                    iceComponent.increaseVal();
                }
                if (e.getTileEffect().getEffectName().equals("doublemove")) {
                    doublemoveComponent.increaseVal();
                } else if (e.getTileEffect().getEffectName().equals("backtrack")) {
                    backtrackComponent.increaseVal();
                }
            }
        }
    }

    /**
     * Method that redraws all the tiles in the tile bag
     */
    public void redrawTiles() {
        cornerComponent.drawTile();
        lineComponent.drawTile();
        tshapeComponent.drawTile();
        backtrackComponent.drawTile();
        doublemoveComponent.drawTile();
        fireComponent.drawTile();
        iceComponent.drawTile();
    }
}
