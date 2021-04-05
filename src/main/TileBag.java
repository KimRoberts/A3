package main;

import main.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TileBag {

    private ArrayList<Tile> tiles;

    public TileBag() {
        tiles = new ArrayList<>();
        initTestTileBag();
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public Tile getTile() {

        if (tiles.size() == 0) return null;
        shakeBag();
        Tile tile = tiles.get(0);
        tiles.remove(tile);
        return tile;
    }

    public void returnTile(Tile tile) {
        tiles.add(tile);
    }

    private void shakeBag() {
        Collections.shuffle(tiles, new Random());

    }

    private void initTestTileBag() {
        tiles = FileReader.getTileBag();
    }
}