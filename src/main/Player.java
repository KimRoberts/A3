package main;

import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.tile.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class represents all the information about the player during the game play.
 *
 * @author Hooi Xuan Low
 */
public class Player extends ImageView {

    public static final int SIZE = 30;

    private Profile profile;
    private String characterURL; //it's a file reference (string that points where the image is)
    private String name;
    private Stack<NavigationTile.DIRECTION> movementStack;
    private ArrayList<Effect> currentlyAppliedEffects;
    private List<EffectTile> heldTiles;
    private EffectTile newTile;
    private int numMoves = 0;
    private NavigationTile heldNavigationTile;
    private EffectTileContainer currentlySelectedEffect;

    /**
     * Constructor for the main.Player class initialises profile.
     *
     * @param profile      a profile to construct the player with.
     * @param characterURL the image of the character.
     */
    public Player(Profile profile, String characterURL) {
        this.profile = profile;
        this.name = profile.getName();
        this.characterURL = characterURL;
        currentlyAppliedEffects = new ArrayList<Effect>();
        movementStack = new Stack<>();
        heldTiles = new ArrayList<>();
        this.setFitHeight(SIZE);
        this.setFitWidth(SIZE);
        this.currentlySelectedEffect = null;
    }

    public String getName() {
        return name;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getCharacterURL() {
        return characterURL;
    }

    /**
     * draws the player to the screen
     */
    public void draw() {
        System.out.println(this.characterURL);
        Image image = new Image(this.characterURL);
        this.setImage(image);
    }


    public ArrayList<Effect> getCurrentlyAppliedEffects() {
        return currentlyAppliedEffects;
    }


    public void addEffect(Effect effect) {
        currentlyAppliedEffects.add(effect);
    }

    public void updateEffectTiles() {
        for (EffectTile effectTile : this.heldTiles) {
            if (effectTile.isFirstTurn()) {
                effectTile.setFirstTurn(false);
            }
        }
    }

    public static Boolean isShown() {
        return true;
    }

    /**
     * put a card into the players hand, interpret it as an action or NavigationTile as needed
     */
    public void drawCard() {
        Tile tile = Mataha.getGame().getTileBag().getTile();
        if (tile instanceof EffectTile) {
            if (newTile != null) {
                heldTiles.add(newTile);
            }
            newTile = (EffectTile) tile;
            heldTiles.add((EffectTile) tile);
            Mataha.getGame().getBottomPanel().displayNewTile(newTile);
            Mataha.getGame().getBottomPanel().displayPlayerTiles();


            Mataha.getGame().getGameInfoPanel().getTipLabel().setPlayEffectText();
            Mataha.getGame().getGameInfoPanel().showNavNextButton();
        }
        if (tile instanceof NavigationTile) {
            this.heldNavigationTile = (NavigationTile) tile;
            Mataha.getGame().getDrawCardPanel().setImageAsTile();
            Mataha.getGame().getGameInfoPanel().getTipLabel().setPlayNavigationText();
            Mataha.getGame().getGameBoard().showInsertTileButtons();
        }

    }


    public NavigationTile.DIRECTION getLastMovement() {
        return movementStack.peek();
    }


    /**
     * moves the player, if it can, also call the win state if needed
     *
     * @param direction the direction for the player to travel
     */
    public void moveCardinally(NavigationTile.DIRECTION direction) {
        //bar
        BoardSquare[][] boardSquares = Mataha.getGame().getGameBoard().getBoard();
        switch (direction) {
            case DOWN:
                if (!canMoveDown()) throw new IllegalArgumentException();
                BoardSquare belowSquare = boardSquares[getPlayerY() + 1][getPlayerX()];
                getSquare().setPlayer(null);
                belowSquare.setPlayer(this);
                movementStack.push(direction);
                if (getSquare().getTile() instanceof GoalTile) {
                    Mataha.getGame().endGame(profile);
                }
                break;
            case UP:
                if (!canMoveUp()) throw new IllegalArgumentException();
                BoardSquare aboveSquare = boardSquares[getPlayerY() - 1][getPlayerX()];
                getSquare().setPlayer(null);
                aboveSquare.setPlayer(this);
                movementStack.push(direction);
                if (getSquare().getTile() instanceof GoalTile) {
                    Mataha.getGame().endGame(profile);
                }
                break;
            case RIGHT:
                if (!canMoveRight()) throw new IllegalArgumentException();
                BoardSquare toRightSquare = boardSquares[getPlayerY()][getPlayerX() + 1];
                getSquare().setPlayer(null);
                toRightSquare.setPlayer(this);
                movementStack.push(direction);
                if (getSquare().getTile() instanceof GoalTile) {
                    Mataha.getGame().endGame(profile);
                }
                break;
            case LEFT:
                if (!canMoveLeft()) throw new IllegalArgumentException();
                BoardSquare toLeftSquare = boardSquares[getPlayerY()][getPlayerX() - 1];
                getSquare().setPlayer(null);
                toLeftSquare.setPlayer(this);
                movementStack.push(direction);
                if (getSquare().getTile() instanceof GoalTile) {
                    Mataha.getGame().endGame(profile);
                }
        }
    }

    /**
     * @return the square this player is standing on
     */
    public BoardSquare getSquare() {
        BoardSquare[][] boardSquares = Mataha.getGame().getGameBoard().getBoard();
        for (BoardSquare[] row : boardSquares) {
            for (BoardSquare square : row) {
                if (square.getPlayer() != null && square.getPlayer().getName().equals(this.getName())) {
                    return square;
                }
            }
        }
        return null;
    }

    public int getPlayerX() {
        return getSquare().getxPos();
    }

    public int getPlayerY() {
        return getSquare().getyPos();
    }


    public boolean canMoveUp() {
        if (getPlayerY() == 0) {
            return false;
        }

        BoardSquare[][] boardSquares = Mataha.getGame().getGameBoard().getBoard();

        BoardSquare playerSquare = boardSquares[getPlayerY()][getPlayerX()];
        BoardSquare aboveSquare = boardSquares[getPlayerY() - 1][getPlayerX()];

        if (targetSquareUnavailable(aboveSquare)) {
            return false;
        }

        boolean hasPathUp = playerSquare.getTile().hasPath(NavigationTile.DIRECTION.UP) &&
                aboveSquare.getTile().hasPath(NavigationTile.DIRECTION.DOWN);

        return hasPathUp;
    }

    public boolean canMoveDown() {

        BoardSquare[][] boardSquares = Mataha.getGame().getGameBoard().getBoard();

        if (getPlayerY() >= boardSquares.length - 1) {
            return false;
        }

        BoardSquare playerSquare = boardSquares[getPlayerY()][getPlayerX()];
        BoardSquare belowSquare = boardSquares[getPlayerY() + 1][getPlayerX()];

        if (targetSquareUnavailable(belowSquare)) {
            return false;
        }

        boolean hasPathDown = playerSquare.getTile().hasPath(NavigationTile.DIRECTION.DOWN) &&
                belowSquare.getTile().hasPath(NavigationTile.DIRECTION.UP);

        return hasPathDown;

    }

    public boolean canMoveLeft() {

        if (getPlayerX() == 0) {
            return false;
        }


        BoardSquare[][] boardSquares = Mataha.getGame().getGameBoard().getBoard();

        if (boardSquares[getPlayerY()][getPlayerX() - 1].getPlayer() != null) {
            return false;
        }

        BoardSquare playerSquare = boardSquares[getPlayerY()][getPlayerX()];
        BoardSquare leftSquare = boardSquares[getPlayerY()][getPlayerX() - 1];

        if (targetSquareUnavailable(leftSquare)) {
            return false;
        }

        boolean hasPathLeft = playerSquare.getTile().hasPath(NavigationTile.DIRECTION.LEFT) &&
                leftSquare.getTile().hasPath(NavigationTile.DIRECTION.RIGHT);

        return hasPathLeft;

    }

    public boolean canMoveRight() {

        BoardSquare[][] boardSquares = Mataha.getGame().getGameBoard().getBoard();

        if (getPlayerX() >= boardSquares[0].length - 1) {
            return false;
        }

        if (boardSquares[getPlayerY()][getPlayerX() + 1].getPlayer() != null) {
            return false;
        }

        BoardSquare playerSquare = boardSquares[getPlayerY()][getPlayerX()];
        BoardSquare rightSquare = boardSquares[getPlayerY()][getPlayerX() + 1];

        if (targetSquareUnavailable(rightSquare)) {
            return false;
        }

        boolean hasPathRight = playerSquare.getTile().hasPath(NavigationTile.DIRECTION.RIGHT) &&
                rightSquare.getTile().hasPath(NavigationTile.DIRECTION.LEFT);

        return hasPathRight;

    }

    public boolean canMove() {
        return canMoveLeft() || canMoveDown() ||
                canMoveRight() || canMoveUp();
    }

    public boolean canMove(NavigationTile.DIRECTION direction) {
        switch (direction) {
            case UP:
                return canMoveUp();
            case RIGHT:
                return canMoveRight();
            case DOWN:
                return canMoveDown();
            case LEFT:
                return canMoveLeft();
        }
        return false;
    }

    /**
     * @param targetSquare the square to check
     * @return if the square is usable
     */
    private boolean targetSquareUnavailable(BoardSquare targetSquare) {

        boolean hasPlayer = targetSquare.getPlayer() != null;
        boolean isOnFire = targetSquare.getActiveEffects().size() != 0 &&
                targetSquare.getActiveEffects().get(0).getEffectName().equals("fire");

        return hasPlayer || isOnFire;

    }

    @Override
    public String toString() {
        return name;
    }

    public List<EffectTile> getHeldTiles() {
        return heldTiles;
    }

    public void addEffectTile(EffectTile tile) {
        heldTiles.add(tile);
    }

    public NavigationTile getHeldNavigationTile() {
        return heldNavigationTile;
    }

    public void setHeldNavigationTile(NavigationTile heldNavigationTile) {
        this.heldNavigationTile = heldNavigationTile;
    }

    public EffectTileContainer getCurrentlySelectedEffect() {
        return currentlySelectedEffect;
    }

    public void setCurrentlySelectedEffect(EffectTileContainer currentlySelectedEffect) {
        this.currentlySelectedEffect = currentlySelectedEffect;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public void setNumMoves(int numMoves) {
        this.numMoves = numMoves;
    }

    public Stack<NavigationTile.DIRECTION> getMovementStack() {
        return movementStack;
    }
}