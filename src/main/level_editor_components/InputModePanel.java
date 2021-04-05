package main.level_editor_components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import main.Utils;
import main.stage.LevelEditorStage;

/**
 * InputModePanel is the panel on the top right of the level editor screen that allows the user to select whether
 * they want to edit tiles or starting positions.
 *
 * @author G38.dev
 */
public class InputModePanel extends VBox {

    private ToggleButton tileInput;
    private ToggleButton startPositionInput;
    private LevelEditorStage context;

    /**
     * Constructor method for InputModePanel
     *
     * @param context - The current LevelEditorStage
     */
    public InputModePanel(LevelEditorStage context) {
        this.context = context;
        setSpacing(10);
        setAlignment(Pos.CENTER);
        initUI();
        setListeners();
    }

    /**
     * Method that sets the listeners for the 2 buttons
     */
    private void setListeners() {
        tileInput.setOnMouseClicked(event -> {
            context.setInputMode(LevelEditorStage.INPUT_MODE.TILE);
            tileInput.setSelected(true);
            startPositionInput.setSelected(false);
        });

        startPositionInput.setOnMouseClicked(event -> {
            context.setInputMode(LevelEditorStage.INPUT_MODE.START_POSITION);
            startPositionInput.setSelected(true);
            tileInput.setSelected(false);
        });
    }

    /**
     * Method that initialises the user interface
     */
    private void initUI() {
        Label label = new Label("Input mode");
        label.setText(Utils.translate("Input Mode", main.MainMenu.getLang()));
        tileInput = new ToggleButton(main.Utils.translate("Tile", main.MainMenu.getLang()));
        tileInput.setSelected(true);
        startPositionInput = new ToggleButton(main.Utils.translate("Starting Position", main.MainMenu.getLang()));

        this.getChildren().addAll(label, tileInput, startPositionInput);
    }
}
