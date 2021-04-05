package main;

import main.tile.NavigationTile;

/**
 * This class sets and creates the effects which are needed for the game.
 *
 * @author G38.dev
 * @version 4
 */
public class Effect {
    private String effectName;
    private Player effectedPlayer;
    private final int[] xList = new int[9];
    private final int[] yList = new int[9];
    Game currentGame;
    GameBoard currentGBoard;
    BoardSquare[][] boardSqArray;
    BoardSquare targetBSquare;
    private boolean areaFree = true;
    private int timeRemaining;
    private boolean isTimed;

    /**
     * This constructor allows the construction of the effect which is named and passed into the constructor.
     *
     * @param e The name of the effect which is being added to the board.
     */
    public Effect(String e /*String e*/) {
        effectName = e;
        currentGame = Mataha.getGame();
    }

    /**
     * This constructor gets the name of the effect and how long the effect should last .
     *
     * @param e        the effect type.
     * @param timeLeft The amount of time the effect should last.
     */
    public Effect(String e, int timeLeft) {
        effectName = e;
        timeRemaining = timeLeft;
        currentGame = Mataha.getGame();
    }

    /**
     * This method gets the effect name.
     *
     * @return The effect name
     */
    public String getEffectName() {
        return effectName;
    }


    /**
     * This method returns true or false if the effect is timed.
     *
     * @return If the effect is timed as a true or false.
     */
    public boolean isTimed() {
        return isTimed;
    }

    /**
     * This method is used to set the effct of ice, this sets the effect to a 3x3 grid on the game board.
     *
     * @param targetTile This is the target main.tile in the center of the 3x3 grid to set the effect in play.
     */
    public void setIce(BoardSquare targetTile) {
        effectName = "ice";
        targetBSquare = targetTile;
        setArea();
        setTime();
        currentGBoard = currentGame.getGameBoard();
        boardSqArray = currentGBoard.getBoard();
        for (int i = 0; i < 9; i++) {
            if (outofBounds(xList[i], yList[i])) continue;
            BoardSquare bs = boardSqArray[yList[i]][xList[i]];
            bs.setFixed(true);
            bs.addActiveEffects(this);
        }
    }

    /**
     * This method reverses the effects of the ice function, and removes the negative effect applied to the 3x3 grid.
     */
    private void reverseIce() {
        if (effectName.equals("ice")) {
            for (int i = 0; i < 9; i++) {
                if (outofBounds(xList[i], yList[i])) continue;
                boardSqArray[yList[i]][xList[i]].setFixed(false);
                boardSqArray[yList[i]][xList[i]].removeActiveEffect(this);
                boardSqArray[yList[i]][xList[i]].removeIce();
            }
        }

    }

    /**
     * This sets the functionality of the fire effect,this uses a target main.tile to add negative effect to the 3x3 grid.
     *
     * @param targetTile The main.tile where the user wants the negative effect to occur , this is the center of the 3x3 grid.
     * @param p          The player.
     */
    public void setFire(BoardSquare targetTile, Player p) {
        if (p != null) {
            effectName = "fire";
            targetBSquare = targetTile;
            setArea();
            setTime();
            currentGBoard = currentGame.getGameBoard();
            boardSqArray = currentGBoard.getBoard();
            for (int i = 0; i < 9; i++) {
                if (outofBounds(xList[i], yList[i])) continue;
                boardSqArray[yList[i]][xList[i]].addActiveEffects(this);
            }
        }
    }

    /***
     *
     */
    private void reverseFire() {
        if (effectName.equals("fire")) {
            for (int i = 0; i < 9; i++) {
                if (outofBounds(xList[i], yList[i])) continue;
                boardSqArray[yList[i]][xList[i]].setFixed(false);
                boardSqArray[yList[i]][xList[i]].removeActiveEffect(this);
                boardSqArray[yList[i]][xList[i]].removeFire();
            }
        }
    }

    /**
     * This method checks if it is out of bounds.
     *
     * @param x Position x on the board
     * @param y Position y on the board
     * @return a boolean if the x and y are out of bounds.
     */
    private boolean outofBounds(int x, int y) {
        return x < 0 || x >= boardSqArray[0].length || y < 0 || y >= boardSqArray.length;
    }

    /**
     * This sets the effect of a double move, this allows the player to move twice.
     *
     * @param p The player, whose effect this is
     */
    public void setDoubleMove(Player p) {
        effectedPlayer = p;
        effectedPlayer.setNumMoves(2);
    }

    /**
     * This sets the effect to a backtrack, this means that the player can move back a space.
     *
     * @param player The player, whose playing this effect.
     */
    public void setBacktrack(Player player) {
        for (int i = 0; i < 2; i++) {
            if (player.getMovementStack().size() == 0) return;
            NavigationTile.DIRECTION direction = getOppositeDirection(player.getMovementStack().pop());
            if (!player.canMove(direction)) return;
            player.moveCardinally(direction);
            player.getMovementStack().pop();
        }

    }

    private NavigationTile.DIRECTION getOppositeDirection(NavigationTile.DIRECTION direction) {
        switch (direction) {
            case UP:
                return NavigationTile.DIRECTION.DOWN;
            case DOWN:
                return NavigationTile.DIRECTION.UP;
            case LEFT:
                return NavigationTile.DIRECTION.RIGHT;
            case RIGHT:
                return NavigationTile.DIRECTION.LEFT;
        }
        return null;
    }

    /**
     * This method is setting the 3x3 grid for the ice and fire effect, this grid is used to set the tiles which the effect is being played on.
     */
    private void setArea() {
        //center
        xList[0] = targetBSquare.getxPos();
        yList[0] = targetBSquare.getyPos();
        //North
        xList[1] = xList[0];
        yList[1] = yList[0] + 1;
        //North West
        xList[2] = xList[0] + 1;
        yList[2] = yList[0] + 1;
        //West
        xList[3] = xList[0] + 1;
        yList[3] = yList[0];
        //South West
        xList[4] = xList[0] + 1;
        yList[4] = yList[0] - 1;
        //South
        xList[5] = xList[0];
        yList[5] = yList[0] - 1;
        //South East
        xList[6] = xList[0] - 1;
        yList[6] = yList[0] - 1;
        //East
        xList[7] = xList[0] - 1;
        yList[7] = yList[0];
        //North East
        xList[8] = xList[0] - 1;
        yList[8] = yList[0] + 1;

    }

    /**
     * This method checks if the area where the effect is about to be played on is free and an effect can be played there.
     *
     * @param foo This is the boardsquare where the effect will be played.
     * @return if the area is free on the board return true.
     */
    public boolean isAreaFree(BoardSquare foo) {
        targetBSquare = foo;
        setArea();
        currentGBoard = currentGame.getGameBoard();
        boardSqArray = currentGBoard.getBoard();
        for (int i = 0; i < 9; i++) {
            if (outofBounds(xList[i], yList[i])) continue;
            if (boardSqArray[yList[i]][xList[i]].getPlayer() != null) {
                areaFree = false;
                break;
            }
        }
        return areaFree;
    }

    /**
     * This method sets the amount of time that the effect will be in play.
     */
    public void setTime() {
        isTimed = true;
        int playerNumber;
        playerNumber = currentGame.getQueueSize();
        currentGame.setTimedEffects(this);
        switch (effectName) {
            case "ice":
                timeRemaining = playerNumber;
                break;
            case "fire":
                timeRemaining = playerNumber * 2;
                break;
            default:
                break;
        }
        //get current turn number, add the amount of players to it, double it if fire, then set time remaining to that
    }

    /**
     * This method checks if the effect is timed this method allows the time to decrease.
     */
    public void decreaseTime() {
        if (isTimed()) {
            timeRemaining = timeRemaining - 1;
        }
    }

    /**
     * This gets the amount of time remaining for the effect.
     *
     * @return The amount of time remaining
     */
    public int getTime() {
        return timeRemaining;
    }

    /**
     * This removes the effect once the time has expired.
     */
    public void removeTime() {
        timeRemaining = 0;
        isTimed = false;
        if (effectName.equals("ice")) {
            reverseIce();
        } else if (effectName.equals("fire")) {
            reverseFire();
        }
    }

}