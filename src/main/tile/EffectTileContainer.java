package main.tile;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.tile.EffectTile;

/**
 * This class is for a container for the effects which allows the effect  tiles to be visualised to the game.
 *
 * @author G38.dev
 * @version 2
 */
public class EffectTileContainer extends ImageView {

    public static final int SIZE = 70;

    private EffectTile tile;
    private boolean isMouseHover;
    private boolean isClicked;

    /**
     * This sets the effect Tile container, then sets the image of the effect.
     *
     * @param tile The effect main.tile which is going into the container.
     */
    public EffectTileContainer(EffectTile tile) {
        this.tile = tile;
        this.setFitHeight(SIZE);
        this.setFitWidth(SIZE);
        isClicked = false;
        isMouseHover = false;
        setImage();
    }

    /**
     * This gets the image url for the effect main.tile in question.
     *
     * @return The Image URL for the effect
     */
    private String getImageUrl() {
        String imageUrl = "resources/Images/game_images/Effects/";
        imageUrl += this.tile.getTileEffect().getEffectName();
        if (isClicked) {
            imageUrl += "_clicked";
        } else if (isMouseHover) {
            imageUrl += "_hover";
        }
        imageUrl += ".gif";
        return imageUrl;
    }

    /**
     * This sets the image for the effect.
     */
    private void setImage() {
        Image image = new Image(getImageUrl());
        this.setImage(image);
    }


    /**
     * This sets if the mouse is hovering over the effect main.tile.
     *
     * @param mouseHover if the mouse is hovering over the effect main.tile
     */
    public void setMouseHover(boolean mouseHover) {
        if (isClicked) return;
        isMouseHover = mouseHover;
        setImage();
    }


    /**
     * This sets the image if the image is clicked upon.
     *
     * @param clicked if the user has clicked returns true.
     */
    public void setClicked(boolean clicked) {
        isClicked = clicked;
        isMouseHover = false;
        setImage();
    }

    /**
     * This gets the effect main.tile.
     *
     * @return The effect Tile.
     */
    public EffectTile getTile() {
        return tile;
    }

    /**
     * This sets the effect main.tile.
     *
     * @param tile An new effect main.tile for the game.
     */
    public void setTile(EffectTile tile) {
        this.tile = tile;
    }
}