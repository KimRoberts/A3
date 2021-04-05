package main.UIOverlays;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Overlay extends ImageView {

    public static final int TILE_SIZE = 40;
    public static final double OPACITY = 0.8;

    String imgUrl;

    public Overlay() {
        this.setFitHeight(TILE_SIZE);
        this.setFitWidth(TILE_SIZE);
        this.setOpacity(0.2);
    }

    protected void setImage() {
        Image image = new Image(imgUrl);
        this.setImage(image);
        this.setOpacity(OPACITY);
    }
}
