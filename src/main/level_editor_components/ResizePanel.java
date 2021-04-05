package main.level_editor_components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Utils;
import main.stage.LevelEditorStage;

/**
 * ResizePanel is the panel on the top left that allows the user to resize the map
 *
 * @author G38.dev
 */
public class ResizePanel extends VBox {

    LevelEditorStage context;
    Slider widthSlider;
    Slider heightSlider;

    /**
     * Constructor method for ResizePanel
     *
     * @param context - The current LevelEditorStage
     */
    public ResizePanel(LevelEditorStage context) {
        this.context = context;
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        initUI();
    }

    /**
     * Method that sets the height and width of the map
     *
     * @param height - The required height of the map
     * @param width  - The required width of the map
     */
    public void setHeightWidth(int height, int width) {
        heightSlider.setValue(height);
        widthSlider.setValue(width);
    }

    /**
     * Method that initialises the user interface of the resize panel
     */
    private void initUI() {
        int mapWidth = context.getMap().getMapWidth();
        int mapHeight = context.getMap().getMapHeight();

        HBox widthBox = new HBox();
        Label widthLabel = new Label("Width:");
        widthLabel.setText(Utils.translate("Width", main.MainMenu.getLang()));
        widthSlider = new Slider(3, 15, mapWidth);
        widthBox.setSpacing(10);
        widthBox.getChildren().addAll(widthLabel, widthSlider);
        widthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            context.getMap().setMapWidth(newValue.intValue());
        });

        HBox heightBox = new HBox();
        Label heightLabel = new Label("Height:");
        heightLabel.setText(Utils.translate("Height", main.MainMenu.getLang()));
        heightSlider = new Slider(3, 15, mapHeight);
        heightBox.setSpacing(10);
        heightBox.getChildren().addAll(heightLabel, heightSlider);
        heightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            context.getMap().setMapHeight(newValue.intValue());
        });
        this.getChildren().addAll(heightBox, widthBox);
    }
}
