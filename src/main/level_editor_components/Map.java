package main.level_editor_components;

import javafx.scene.layout.GridPane;
import main.stage.LevelEditorStage;
import main.tile.GoalTile;

import java.util.*;

import static main.tile.NavigationTile.DIRECTION.*;

/**
 * Map is the map where the user can place navigation tiles of their choosing
 *
 * @author G38.dev
 */
public class Map extends GridPane {

    private static MapSquare[][] map;
    private static LevelEditorStage context;
    private static String serialised;

    /**
     * Constructor method for Map
     *
     * @param width   - The width of the map
     * @param height  - The height of the map
     * @param context - The current LevelEditorStage
     */
    public Map(int width, int height, LevelEditorStage context) {
        this.context = context;
        initNewMap(width, height);
        initGoalTile();
        System.out.println("Has possible path: " + hasPossiblePath());
        this.setVgap(3);
        this.setHgap(3);
    }

    /**
     * Method that gets the serialised map contents
     *
     * @return the sting containing details about the map
     */
    public static String getSerialised() {
        return serialised;
    }

    /**
     * Method that initialises the goal tile in roughly the middle of the map
     */
    private void initGoalTile() {
        int goalX = map[0].length / 2;
        int goalY = map.length / 2;
        map[goalY][goalX].setTile(new GoalTile(0));
    }

    /**
     * Method that initialises a new map using the width and height specified
     *
     * @param width  - The width of the map
     * @param height - The height of the map
     */
    private void initNewMap(int width, int height) {

        map = new MapSquare[height][width];

        // Create a new map square, add it to every position in the map[][]
        // then add this MapSquare to this node at position (row + 1, col + 1) as gridpanes index at 1
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                MapSquare mapSquare = new MapSquare(context, col, row);
                map[row][col] = mapSquare;
                this.add(mapSquare, col + 1, row + 1);
            }
        }
    }

    /**
     * Method that removes the goal tile if the user tries to place another one elsewhere
     */
    public void removeGoalSquare() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col].getTile() instanceof GoalTile) {
                    map[row][col].setTile(null);
                }
            }
        }
    }


    /**
     * Method that checks if the placement of the navigation tiles allows all the players to get to the goal tile.
     *
     * @returns true if all players have possible path from starting point, false if not
     */
    public boolean hasPossiblePath() {

        int X = 0;
        int Y = 1;

        int[] goal = getGoalLocation();

        Queue<MapSquare> queue = new LinkedList<>();
        HashSet<MapSquare> visited = new HashSet<>();
        int startingPositionsFound = 0;

        MapSquare current = map[goal[Y]][goal[X]];
        queue.add(current);
        visited.add(current);

        // bfs
        while (!queue.isEmpty()) {
            // pop queue
            current = queue.poll();
            visited.add(current);

            // check if current is one of the starting positions, if so, increment num found positions
            if (current.isStartingPosition()) {
                startingPositionsFound++;
            }

            // add neighbouring squares to list if there is a path there
            ArrayList<MapSquare> neighbours = new ArrayList<>();

            if (squareHasPathUp(current)) {
                neighbours.add(map[current.getY() - 1][current.getX()]);
            }
            if (squareHasPathRight(current)) {
                neighbours.add(map[current.getY()][current.getX() + 1]);
            }
            if (squareHasPathDown(current)) {
                neighbours.add(map[current.getY() + 1][current.getX()]);
            }
            if (squareHasPathLeft(current)) {
                neighbours.add(map[current.getY()][current.getX() - 1]);
            }

            // if unvisited, add neighbouring nodes to queue
            for (MapSquare neighbour : neighbours) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        return startingPositionsFound == 4;
    }

    /**
     * Method that increases the width of the map
     */
    public void increaseWidth() {

        // instantiates a new map with width 1 size bigger and adds all the current map squares to it
        MapSquare[][] newMap = new MapSquare[getMapHeight()][getMapWidth() + 1];
        for (int row = 0; row < getMapHeight(); row++) {
            for (int col = 0; col < getMapWidth(); col++) {
                newMap[row][col] = map[row][col];
            }
        }

        // Adds a new column to the gridpane consisting of new mapsquares, then add these mapsquares to the map[][]
        int newIndex = map[0].length + 1;
        for (int i = 0; i < map.length; i++) {
            MapSquare newSquare = new MapSquare(context, newIndex - 1, i);
            this.add(newSquare, newIndex, i + 1);
            newMap[i][newIndex - 1] = newSquare;
        }

        map = newMap;
    }

    /**
     * Method that increases the height of the map
     */
    public void increaseHeight() {

        // instantiates a new map with height 1 size bigger and adds all the current map squares to it
        MapSquare[][] newMap = new MapSquare[getMapHeight() + 1][getMapWidth()];
        for (int row = 0; row < getMapHeight(); row++) {
            for (int col = 0; col < getMapWidth(); col++) {
                newMap[row][col] = map[row][col];
            }
        }

        // Adds a new row to the gridpane consisting of new mapsquares, then add these mapsquares to the map[][]
        int newIndex = map.length + 1;
        for (int i = 0; i < map[0].length; i++) {
            MapSquare newSquare = new MapSquare(context, i, newIndex - 1);
            this.add(newSquare, i + 1, newIndex);
            newMap[newIndex - 1][i] = newSquare;
        }

        map = newMap;
    }

    /**
     * Method that decreases the width of the map
     */
    public void decreaseWidth() {

        //removes one column from the gridpane. If the mapsquare getting removed is a starting position
        // or a goal tile, it moves it to the left
        int removeIndex = getMapWidth() - 1;
        for (int i = 0; i < getMapHeight(); i++) {
            MapSquare toRemove = map[i][removeIndex];
            if (toRemove.getTile() instanceof GoalTile) {
                map[i][removeIndex - 1].setTile(new GoalTile(0));
            }
            if (toRemove.isStartingPosition()) {
                map[i][removeIndex - 1].setStartingPosition(true);
            }
            this.getChildren().remove(toRemove);
        }

        // instantiates a new map with width 1 smaller and copies over the elements of the current map
        MapSquare[][] newMap = new MapSquare[getMapHeight()][getMapWidth() - 1];
        for (int row = 0; row < getMapHeight(); row++) {
            for (int col = 0; col < getMapWidth() - 1; col++) {
                newMap[row][col] = map[row][col];
            }
        }

        map = newMap;
    }

    /**
     * Method that decreases the height of the map
     */
    public void decreaseHeight() {

        //removes one row from the gridpane. If the mapsquare getting removed is a starting position
        // or a goal tile, it moves it to the left
        int removeIndex = getMapHeight() - 1;
        for (int i = 0; i < getMapWidth(); i++) {
            MapSquare toRemove = map[removeIndex][i];
            if (toRemove.getTile() instanceof GoalTile) {
                map[removeIndex - 1][i].setTile(new GoalTile(0));
            }
            if (toRemove.isStartingPosition()) {
                map[removeIndex - 1][i].setStartingPosition(true);
            }
            this.getChildren().remove(toRemove);
        }

        // instantiates a new map with width 1 smaller and copies over the elements of the current map
        MapSquare[][] newMap = new MapSquare[getMapHeight() - 1][getMapWidth()];
        for (int row = 0; row < getMapHeight() - 1; row++) {
            for (int col = 0; col < getMapWidth(); col++) {
                newMap[row][col] = map[row][col];
            }
        }

        map = newMap;

    }

    /**
     * Method that checks if the map square specified has a path upwards
     *
     * @param current - The current map square
     * @return true if there is a path up, false otherwise
     */
    private boolean squareHasPathUp(MapSquare current) {

        int x = current.getX();
        int y = current.getY();

        if (outOfBounds(x, y - 1)) {
            return false;
        }

        MapSquare aboveSquare = map[y - 1][x];

        return current.hasPath(UP) && aboveSquare.hasPath(DOWN);
    }

    /**
     * Method that checks if the map square specified has a path right
     *
     * @param current - The current map square
     * @return true if there is a path right, false otherwise
     */
    private boolean squareHasPathRight(MapSquare current) {
        int x = current.getX();
        int y = current.getY();

        if (outOfBounds(x + 1, y)) {
            return false;
        }

        MapSquare toRightSquare = map[y][x + 1];

        return current.hasPath(RIGHT) && toRightSquare.hasPath(LEFT);
    }

    /**
     * Method that checks if the map square specified has a path left
     *
     * @param current - The current map square
     * @return true if there is a path left, false otherwise
     */
    private boolean squareHasPathLeft(MapSquare current) {
        int x = current.getX();
        int y = current.getY();

        if (outOfBounds(x - 1, y)) {
            return false;
        }

        MapSquare toLeftSquare = map[y][x - 1];

        return current.hasPath(LEFT) && toLeftSquare.hasPath(RIGHT);
    }

    /**
     * Method that checks if the map square specified has a path downwards
     *
     * @param current - The current map square
     * @return true if there is a path downwards, false otherwise
     */
    private boolean squareHasPathDown(MapSquare current) {
        int x = current.getX();
        int y = current.getY();

        if (outOfBounds(x, y + 1)) {
            return false;
        }

        MapSquare belowSquare = map[y + 1][x];

        return current.hasPath(DOWN) && belowSquare.hasPath(UP);
    }

    /**
     * Method that checks if a 2d index is out of the bounds of the array
     *
     * @param x - the x component of the index
     * @param y - the y component of the index
     * @return true if out of bounds, false otherwise
     */
    private boolean outOfBounds(int x, int y) {
        return x < 0 || x >= map[0].length || y < 0 || y >= map.length;
    }

    /**
     * Method that gets the location of the goal tile
     *
     * @return integer array of the location of the goal tile
     */
    private int[] getGoalLocation() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col].getTile() instanceof GoalTile) {
                    return new int[]{col, row};
                }
            }
        }

        return new int[]{-1, -1};
    }

    /**
     * Method that checks if the map has a goal square
     *
     * @return true if map has goal square, false otherwise
     */
    public boolean hasGoalSquare() {
        int[] goal = getGoalLocation();
        return !(goal[0] == -1 && goal[1] == -1);
    }

    /**
     * Method that gets the width of the map
     *
     * @return integer width of the map
     */
    public int getMapWidth() {
        return Map.map[0].length;
    }

    /**
     * Method that sets the width of the map
     *
     * @param width - The new width of the map
     */
    public void setMapWidth(int width) {

        int diff = width - getMapWidth();
        if (diff == 0) return;

        // calls the increaseWidth method n times where n is the difference between the new width and the old width
        for (int i = 0; i < Math.abs(diff); i++) {
            if (diff < 0) decreaseWidth();
            if (diff > 0) increaseWidth();
        }
    }

    /**
     * Method that gets the height of the map
     *
     * @return integer height of the map
     */
    public int getMapHeight() {
        return Map.map.length;
    }

    /**
     * Method that sets the height of the map
     *
     * @param height - The new height of the map
     */
    public void setMapHeight(int height) {

        int diff = height - getMapHeight();
        if (diff == 0) return;

        // calls the increaseHeight method n times where n is the difference between the new width and the old width
        for (int i = 0; i < Math.abs(diff); i++) {
            if (diff < 0) decreaseHeight();
            if (diff > 0) increaseHeight();
        }
    }

    /**
     * Method that gets the number of non-fixed tiles
     *
     * @return the total number of non-fixed tiles
     */
    public int getNumNonFixedTiles() {
        int sum = 0;
        for (int row = 0; row < getMapHeight(); row++) {
            for (int col = 0; col < getMapWidth(); col++) {
                if (map[row][col].getTile() == null) {
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * Method that serialises the map contents so they can be appended to the save file.
     *
     * @return the sting containing details about the map
     */
    public String serialise() {
        String build = "";
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                build += map[i][j].serialise();
            }
        }
        serialised = build;
        return build;
    }

    /**
     * Method that gets the number of starting positions placed on the map
     *
     * @return the total number of starting positions
     */
    public int getNumStartingPosition() {
        int sum = 0;
        for (int row = 0; row < getMapHeight(); row++) {
            for (int col = 0; col < getMapWidth(); col++) {
                if (map[row][col].isStartingPosition()) {
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * Method that removes the nearest starting position from a given x y position on the map
     *
     * @param x - x coordinate of the selected position
     * @param y - y coordinate of the selected position
     * @return true if it finds a starting position to remove, false otherwise
     */
    public boolean removeNearestStartingPosition(int x, int y) {
        MapSquare current = map[y][x];
        Queue<MapSquare> q = new LinkedList<>();
        HashSet<MapSquare> visited = new HashSet<>();
        q.add(current);

        //bfs
        while (!q.isEmpty()) {

            //pop queue
            current = q.poll();
            visited.add(current);
            if (current.isStartingPosition()) {
                current.setStartingPosition(false);
                return true;
            }

            // add neighbouring map squares
            ArrayList<MapSquare> neighbours = new ArrayList<>();
            for (int xInc = -1; xInc <= 1; xInc++) {
                for (int yInc = -1; yInc <= 1; yInc++) {
                    int newX = current.getX() + xInc;
                    int newY = current.getY() + yInc;
                    if (outOfBounds(newX, newY)) continue;
                    neighbours.add(map[newY][newX]);
                }
            }

            // if unvisited, add neighbouring nodes to queue
            for (MapSquare neighbour : neighbours) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    q.add(neighbour);
                }
            }
        }
        return false;
    }

    /**
     * Method that sets the map squares
     *
     * @param mapSquares - A 2D array containing all the map squares
     */
    public void setMapSquares(MapSquare[][] mapSquares) {
        Map.map = mapSquares;
        this.getChildren().clear();

        // Adds all map squares to the grid pane
        for (int row = 0; row < mapSquares.length; row++) {
            for (int col = 0; col < mapSquares[0].length; col++) {
                if (mapSquares[row][col] == null) {
                    // if mapSquare at (row, col) is null, create a new one
                    this.add(new MapSquare(context, col, row), col + 1, row + 1);
                } else {
                    this.add(mapSquares[row][col], col + 1, row + 1);
                }

            }
        }
    }

    /**
     * Method that redraws the tiles
     */
    public void redrawTiles() {
        for (int row = 0; row < getMapHeight(); row++) {
            for (int col = 0; col < getMapWidth(); col++) {
                if (map[row][col] != null && map[row][col].getTile() != null) {
                    map[row][col].setTile(map[row][col].getTile());
                    map[row][col].getTile().setTileImage();
                }
            }
        }
    }

    /**
     * Method that checks if the starting positions have a tile under them
     *
     * @return true if all the starting positions have a tile under them, false otherwise
     */
    public boolean startingPositionsAreFixed() {
        for (int row = 0; row < getMapHeight(); row++) {
            for (int col = 0; col < getMapWidth(); col++) {
                MapSquare square = map[row][col];
                if (square.isStartingPosition() && square.getTile() == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
