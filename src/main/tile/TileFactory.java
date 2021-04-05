package main.tile;

public class TileFactory {

    public enum TILE_TYPE {
        CORNER("corner"),
        TSHAPE("tshape"),
        LINE("line"),
        GOAL("goal");
        private String val;

        TILE_TYPE(String type) {
            this.val = type;
        }
    }


    public static NavigationTile getTile(String type, int rotation, boolean isFixed) {
        NavigationTile navigationTile = null;
        switch (type) {
            case "corner":
                navigationTile = new CornerTile(rotation, isFixed);
                break;
            case "tshape":
                navigationTile = new TShapeTile(rotation, isFixed);
                break;
            case "line":
                navigationTile = new LineTile(rotation, isFixed);
                break;
            case "goal":
                navigationTile = new GoalTile(rotation);
                break;
            default:
                break;
        }
        return navigationTile;
    }
}
