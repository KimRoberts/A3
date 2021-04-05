package main.tile;

public class CornerTile extends NavigationTile {
    public CornerTile(int rotation, boolean isFixed) {
        super(isFixed);
        pathTo = new boolean[]{true, false, false, true};
        rotateClockwise(rotation);
    }

    @Override
    public String toString() {
        return "corner";
    }

    @Override
    public String preciseToString() {
        return "corner;" + getRotation() + ";" + isFixed();
    }
}
