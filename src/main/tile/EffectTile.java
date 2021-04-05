package main.tile;

import main.Effect;
import main.tile.Tile;

/**
 * This class is setting an effect as a main.tile.
 *
 * @author G38.dev
 */
public class EffectTile extends Tile {

    private Effect effect;
    private boolean firstTurn;

    /**
     * This allows a effect main.tile to be constructed, and set to the correct effect for the main.tile.
     *
     * @param effect The effect which the effect main.tile will be set to.
     */
    public EffectTile(Effect effect) {
        this.effect = effect;
        this.firstTurn = true;
    }


    /**
     * This gets the main.Effect Tile's type.
     *
     * @return The type of effect
     */
    public Effect getTileEffect() {
        return effect;
    }


    /**
     * This sets the effect for the main.tile.
     *
     * @param effect The effect name.
     */
    public void setEffect(Effect effect) {
        this.effect = effect;
    }


    public boolean isFirstTurn() {
        return firstTurn;
    }

    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }
}