package com.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Gayming on 8/31/2016.
 */
public class Board extends Group {
    private int rows;
    private int cols;
    private Tile[][] tiles;

    public Board(int rows, int cols) {
        super();

        this.rows = rows;
        this.cols = cols;

        tiles = new Tile[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tiles[i][j] = new Tile(i, j);
                addActor(tiles[i][j]);//adding tiles to group
            }
        }

        setWidth(tiles[0][0].getWidth() * cols);
        setHeight(tiles[0][0].getHeight() * rows);

        setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight() / 2);
        //System.out.println(getX() + ", " + getY());

    }

    public Tile getTile(int row, int col) {
        //do i need try/catch here
        try{
            return tiles[row][col];
        }
        catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            Gdx.app.log("getTile", "index out of bounds");
            return null;
        }
    }
    /*
    public Tile getTile(int index) {
        int row = index/rows;
        int col = index%rows;
        return tiles[row][col];
    }
    */
    /*
    public void setSymbol(int row, int col, Tile.Symbol symbol){
        Tile tile;
        try{
            tile = getTile(row, col);
        }
        catch(Exception e){
            System.out.println(e.getMessage());//index out of bounds
            return;
        }
        tile.setSymbol(symbol);
    }
    */

    public void clearSelection(){
        for (Actor actor : getChildren()) {
            Tile tile = (Tile) actor;
            tile.setSelected(false);
        }
    }

    public void select(Tile firstTile, Tile lastTile){
        Set<Tile> tilesSelected = getRectangularSelection(firstTile, lastTile);
        for (Tile t : tilesSelected){
            t.setSelected(true);
        }
    }

    public void assignColorToSelection(Color color){

    }

    private Set<Tile> getRectangularSelection(Tile firstTile, Tile lastTile){
        Set<Tile> selection = new HashSet<Tile>();
        int row1 = firstTile.getRow();
        int col1 = firstTile.getCol();
        int row2 = lastTile.getRow();
        int col2 = lastTile.getCol();

        if (row1 > row2){
            int temp = row1;
            row1 = row2;
            row2 = temp;
        }
        if (col1 > col2){
            int temp = col1;
            col1 = col2;
            col2 = temp;
        }

        for (int i = row1; i <= row2; i++){
            for (int j = col1; j <= col2; j++){
                selection.add(getTile(i, j));
            }
        }
        return selection;
    }
}
