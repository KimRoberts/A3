package main.tile;

public class LineTile extends NavigationTile {
    public LineTile(int rotation, boolean isFixed) {
        super(isFixed);
        pathTo = new boolean[]{true, false, true, false};
        rotateClockwise(rotation);
    }

    @Override
    public String toString() {
        return "line";
    }

    @Override
    public String preciseToString() {
        return "line;" + getRotation() + ";" + isFixed();
    }
}
