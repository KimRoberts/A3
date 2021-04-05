package main.stage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.create_texture_pack_components.DrawNavTileCanvas;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;


public class CreateTexturePackStage {

    public enum INPUT_TYPE{
        BRUSH,
        LINE,
        RECTANGLE
    }

    private INPUT_TYPE inputType = INPUT_TYPE.BRUSH;
    private int brushSize = 1;

    private ColorPicker colorPicker;
    private GridPane tileContainer;
    private HBox toolbar;
    private HBox saveContainer;

    private DrawNavTileCanvas lineTile;
    private DrawNavTileCanvas tShapeTile;
    private DrawNavTileCanvas lShapeTile;
    private DrawNavTileCanvas goalTile;


    public void show(){

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefSize(800, 600);

        initToolbar();

        initTiles();

        initSaveButton();


        root.getChildren().addAll(toolbar, tileContainer, saveContainer);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void initTiles(){
        tileContainer = new GridPane();
        tileContainer.setHgap(50);
        tileContainer.setVgap(50);

        lineTile = new DrawNavTileCanvas(150, DrawNavTileCanvas.TILE_TYPE.LINE, this);
        tShapeTile = new DrawNavTileCanvas(150, DrawNavTileCanvas.TILE_TYPE.TSHAPE, this);
        lShapeTile = new DrawNavTileCanvas(150, DrawNavTileCanvas.TILE_TYPE.LSHAPE, this);
        goalTile = new DrawNavTileCanvas(150, DrawNavTileCanvas.TILE_TYPE.GOAL, this);

        tileContainer.add(lineTile, 1, 1);
        tileContainer.add(tShapeTile, 1, 2);
        tileContainer.add(lShapeTile, 2, 1);
        tileContainer.add(goalTile, 2, 2);
    }

    private void initToolbar(){

        toolbar = new HBox();
        toolbar.setSpacing(20);
        toolbar.setPrefSize(800, 50);

        colorPicker = new ColorPicker();

        Slider brushSlider = new Slider(1, 20, 3);
        brushSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                brushSize = newValue.intValue();
            }
        });
        toolbar.getChildren().addAll(colorPicker, brushSlider);
    }

    private void initSaveButton(){

        Button saveTiles = new Button("Save");
        saveTiles.setOnMouseClicked(e -> {
            TextInputDialog td = new TextInputDialog("Map Name");
            td.showAndWait();
            String mapName = td.getEditor().getText();
            if (saveImages(mapName)){
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Saved!");
                a.show();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Something went wrong");
                a.show();
            }
        });

        saveContainer = new HBox();
        saveContainer.setPadding(new Insets(100, 0, 0, 0));
        saveContainer.setAlignment(Pos.CENTER);
        saveContainer.getChildren().addAll(saveTiles);

    }

    public double getBrushSize(){
        return brushSize;
    }

    public Color getColour(){
        return colorPicker.getValue();
    }

    public boolean saveImages(String filename){

        String path = "src\\resources\\Images\\game_images\\texture_packs\\";
        String newFilePath = path + filename;

        File newFile = new File(newFilePath);

        if (newFile.exists()){
            Alert a = new Alert(Alert.AlertType.ERROR, "File name already exists");
            return false;
        }

        newFile.mkdir();


        RenderedImage lineImage = lineTile.getRenderedImage();
        RenderedImage tShapeImage = tShapeTile.getRenderedImage();
        RenderedImage lShapeImage = lShapeTile.getRenderedImage();
        RenderedImage goalImage = goalTile.getRenderedImage();

        try {
            File lineFile = new File(newFilePath + "\\line.gif");
            ImageIO.write(lineImage, "gif", lineFile);

            File tShapeFile = new File(newFilePath + "\\tshape.gif");
            ImageIO.write(tShapeImage, "gif", tShapeFile);

            File lShapeFile = new File(newFilePath + "\\corner.gif");
            ImageIO.write(lShapeImage, "gif", lShapeFile);

            File goalFile = new File(newFilePath + "\\goal.gif");
            ImageIO.write(goalImage, "gif", goalFile);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public INPUT_TYPE getInputType() {
        return inputType;
    }

}
