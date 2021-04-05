package main.level_editor_components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.Mataha;


/**
 * TileBagComponent is a visual component that renders in SetTileBagPanel. It contains a type of tile,
 * an input box that represents the number of that type of tile in the TileBag and buttons to increment or decrement
 * the value
 *
 * @author G38.dev
 */
public class TileBagComponent extends HBox {

    TILE_IMAGE tileImage;
    TextField input;
    ImageView image;

    /**
     * Constructs an instance of this class using the type of tile to be represented using an enum
     *
     * @param tileImage
     */
    public TileBagComponent(TILE_IMAGE tileImage) {

        this.tileImage = tileImage;

        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        initUI();
    }

    /**
     * Initialises the UI components that render within this class and adds them to the children ArrayList
     */
    private void initUI() {

        image = new ImageView();
        drawTile();
        image.setFitWidth(40);
        image.setFitHeight(40);

        input = new TextField("0");
        input.setMaxWidth(60);

        // force the field to be numeric only
        input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    input.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.equals("")) {
                    input.setText("0");
                }
            }
        });

        Label minus = new Label("-");
        minus.setPrefWidth(15);
        minus.setPrefHeight(15);
        minus.setAlignment(Pos.CENTER);
        minus.setOnMouseClicked(event -> {
            decreaseVal();
        });

        Label plus = new Label("+");
        plus.setPrefWidth(15);
        plus.setPrefHeight(15);
        plus.setAlignment(Pos.CENTER);
        plus.setOnMouseClicked(event -> {
            increaseVal();
        });

        this.getChildren().addAll(image, minus, input, plus);
    }

    /**
     * decreases the value of the text box rendered within this component
     */
    private void decreaseVal() {

        int newVal = Integer.parseInt(input.getText().toString()) - 1;
        newVal = Math.max(newVal, 0);
        input.setText(Integer.toString(newVal));
    }

    /**
     * sets the value of the text box rendered within this component
     *
     * @param num - the new value displayed in the textbox
     */
    public void setVal(int num) {
        input.setText(String.valueOf(num));
    }

    /**
     * increases the value of the text box rendered within this component
     */
    public void increaseVal() {

        int newVal = Integer.parseInt(input.getText().toString()) + 1;
        input.setText(Integer.toString(newVal));
    }

    /**
     * returns the value of the text box rendered within this component
     *
     * @return the value of the text box
     */
    public int getValue() {
        return Integer.parseInt(input.getText().toString());
    }

    /**
     * @return returns the URL of the image that visually represents the tile inside the instance of this class
     */
    public String getImageUrl() {
        String imgUrl = "";
        switch (tileImage) {
            case TSHAPE:
                imgUrl = "src\\resources\\Images\\game_images\\texture_packs\\" + Mataha.texturePack + "\\" + "tshape.gif";
                break;
            case CORNER:
                imgUrl = "src\\resources\\Images\\game_images\\texture_packs\\" + Mataha.texturePack + "\\" + "corner.gif";
                break;
            case LINE:
                imgUrl = "src\\resources\\Images\\game_images\\texture_packs\\" + Mataha.texturePack + "\\" + "line.gif";
                break;
            case FIRE:
                imgUrl = "src\\resources\\Images\\game_images\\Effects\\fire.gif";
                break;
            case ICE:
                imgUrl = "src\\resources\\Images\\game_images\\Effects\\ice.gif";
                break;
            case BACKTRACK:
                imgUrl = "src\\resources\\Images\\game_images\\Effects\\backtrack.gif";
                break;
            case DOUBLEMOVE:
                imgUrl = "src\\resources\\Images\\game_images\\Effects\\doublemove.gif";
                break;
        }
        return imgUrl;

    }

    /**
     * Calls the Tile attribute of this class to redraw its image. This may be called
     * in response to global UI changes that need to be reflected in smaller components such as texture packs
     */
    public void drawTile() {
        String imgUrl = getImageUrl();
        image.setImage(new Image(imgUrl));
    }

    /**
     * An enumerated type that represents the 7 possible types of tile to add to the TileBag, and contains
     * a URL to an image that visually represents that tile on the screen
     */
    public enum TILE_IMAGE {
        CORNER("src\\resources\\Images\\game_images\\texture_packs\\" + Mataha.texturePack + "\\" + "corner.gif"),
        LINE("src\\resources\\Images\\game_images\\texture_packs\\" + Mataha.texturePack + "\\" + "line.gif"),
        TSHAPE("src\\resources\\Images\\game_images\\texture_packs\\" + Mataha.texturePack + "\\" + "tshape.gif"),
        BACKTRACK("src\\resources\\Images\\game_images\\Effects\\backtrack.gif"),
        DOUBLEMOVE("src\\resources\\Images\\game_images\\Effects\\doublemove.gif"),
        FIRE("src\\resources\\Images\\game_images\\Effects\\fire.gif"),
        ICE("src\\resources\\Images\\game_images\\Effects\\ice.gif");

        private String imgUrl;

        TILE_IMAGE(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
