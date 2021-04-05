package main.tile;

public class TShapeTile extends NavigationTile {
    public TShapeTile(int rotation, boolean isFixed) {
        super(isFixed);
        pathTo = new boolean[]{false, true, true, true};
        rotateClockwise(rotation);
    }

    @Override
    public String toString() {
        return "tshape";
    }

    @Override
    public String preciseToString() {
        return "tshape;" + getRotation() + ";" + isFixed();
    }
}

