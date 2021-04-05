package main.tile;

public class GoalTile extends NavigationTile {
    public GoalTile(int rotation) {
        super(true);
        pathTo = new boolean[]{true, true, true, true};
        rotateClockwise(rotation);
    }

    @Override
    public String toString() {
        return "goal";
    }

    @Override
    public String preciseToString() {
        return "goal;" + getRotation() + ";fixed";
    }
}
