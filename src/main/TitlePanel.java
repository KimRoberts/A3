package main;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/***
 * This class is for the title panel in the game.
 * @author G3.dev
 * @version 1
 */
public class TitlePanel extends HBox {

    private ImageView imageView;

    public TitlePanel(String imgUrl) {
        this.setAlignment(Pos.CENTER);
        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(80);
        Image image = new Image(imgUrl);
        imageView.setImage(image);
        this.getChildren().add(imageView);
    }
}