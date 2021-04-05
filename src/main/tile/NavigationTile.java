package main.tile;

import javafx.scene.image.Image;
import main.Mataha;

public abstract class NavigationTile extends Tile implements Cloneable {

    public static final int SIZE = 40;

    protected boolean[] pathTo;
    private boolean isFixed;
    private int rotation;
    private String imgUrl;

    public NavigationTile(boolean isFixed) {
        this.rotation = 0;
        this.setFitHeight(SIZE);
        this.setFitWidth(SIZE);
        this.isFixed = isFixed;
    }

    private String getImgUrl() {
        String texture = Mataha.texturePack;
        String imgUrl = "src\\resources\\Images\\game_images\\texture_packs\\" + texture + "\\" + this.toString();
        return imgUrl + ".gif";
    }

    public void setTileImage() {
        this.imgUrl = getImgUrl();
        Image image = new Image(this.imgUrl);
        double toRotate = (90 * this.getRotation()) % 360;
        this.setRotate(toRotate);
        this.setImage(image);
    }

    public enum DIRECTION {
        UP(0),
        RIGHT(1),
        DOWN(2),
        LEFT(3);
        private final int num;

        private DIRECTION(int num) {
            this.num = num;
        }
    }

    public boolean hasPath(DIRECTION direction) {
        boolean hasPath = pathTo[direction.num];
        return hasPath;
    }

    public boolean[] getPathTo() {
        return pathTo;
    }

    public void rotateClockwise() {
        rotateClockwise(1);
    }

    public void rotateClockwise(int n) {
        rotation = (rotation + n) % 4;
        boolean[] newPathTo = new boolean[pathTo.length];
        for (int i = 0; i < pathTo.length; i++) {
            int newIndex = (i + n) % 4;
            newPathTo[newIndex] = pathTo[i];
        }
        pathTo = newPathTo;
        this.setTileImage();
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public abstract String preciseToString();
}
