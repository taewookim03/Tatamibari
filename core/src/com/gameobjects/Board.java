package com.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.*;

/**
 * Created by Gayming on 8/31/2016.
 */
public class Board extends Group {
    private int rows;
    private int cols;
    private Tile[][] tiles;

    private List<Region> regions;//maybe initialize regions with different colors

    private ShapeRenderer sr;

    private static final float OUTLINE_THICKNESS = 2.0f;

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

        setWidth(tiles[0][0].getWidth() * cols);//error handling?
        setHeight(tiles[0][0].getHeight() * rows);

        setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight() / 2);
        //System.out.println(getX() + ", " + getY());

        regions = new ArrayList<Region>();

        sr = new ShapeRenderer();

        //for testing symbol drawing
        setSymbol(2, 2, Tile.Symbol.SQUARE);
        setSymbol(2, 3, Tile.Symbol.VERTICAL);
        setSymbol(0, 0, Tile.Symbol.HORIZONTAL);
        setSymbol(0, 4, Tile.Symbol.SQUARE);
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

    public void clearSelection(){
        for (Actor actor : getChildren()) {
            Tile tile = (Tile) actor;
            tile.setSelected(false);//if multicolor scheme, should track previous color to revert to
            if (tile.getRegion() == null){
                tile.setColor(Color.WHITE);
            }
        }
    }

    public void select(Tile firstTile, Tile lastTile, Color color){
        Set<Tile> tilesSelected = getRectangularSelection(firstTile, lastTile);
        for (Tile t : tilesSelected){
            t.setSelected(true, color);
        }
    }

    public void addTempRegion(Region tempRegion){

    }
    public void addRegion(Region newRegion){
        newRegion.addSelectedTiles();

        regions.add(newRegion);
    }

    public void removeRegion(Region region){
        //System.out.println("deleting region: " + region);
        region.clearRegionFromTiles();
        regions.remove(region);
    }

    public void clearOverlappingRegions(){
        Set<Region> toBeRemoved = new HashSet<Region>();

        for (Region region : regions){
            //for each region, check if it contains tiles whose assigned region is not itself (because it was assigned newRegion)
            //delete such regions
            for (Tile tile : region.getTiles()){
                if (!region.equals(tile.getRegion())){
                    toBeRemoved.add(region);
                    //System.out.println("tile " + tile + " is part of:\n" + region + "\nbut getRegion() returns: " + tile.getRegion());
                }
            }
        }

        for (Region region : toBeRemoved){
            removeRegion(region);
        }
    }

    public void refreshRegions(){
        for (Region region : regions){
            region.refresh();
        }
    }

    public List<Region> getRegions(){
        return regions;
    }

    private Set<Tile> getRectangularSelection(Tile firstTile, Tile lastTile){
        Set<Tile> selection = new HashSet<Tile>();
        if (firstTile == null || lastTile == null){
            return selection;
        }

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

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        //draw the four edges of the board
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.rectLine(getX(), getY(), getX() + getWidth(), getY(), OUTLINE_THICKNESS);//bottom
        sr.rectLine(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight(), OUTLINE_THICKNESS);//top
        sr.rectLine(getX(), getY(), getX(), getY() + getHeight(), OUTLINE_THICKNESS);//left
        sr.rectLine(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight(), OUTLINE_THICKNESS);//right
        sr.end();

        for (Region region : regions){
            region.draw(batch, parentAlpha);
        }
    }

    public boolean hasFourRegionCorner() {
        //map of corner positions (Vector2 of xy) : count, return true if any count is 4 (can't be more than 4)
        Map<Vector2, Integer> cornerCount = new HashMap<Vector2, Integer>();
        Vector2[] corners = new Vector2[4];

        for (Region region : regions){

            corners[0] = region.getLowerLeftCorner();
            corners[1] = region.getLowerRightCorner();
            corners[2] = region.getUpperLeftCorner();
            corners[3] = region.getUpperRightCorner();

            for (Vector2 corner : corners){
                if (cornerCount.containsKey(corner)){
                    cornerCount.put(corner, cornerCount.get(corner) + 1);
                }
                else{
                    cornerCount.put(corner, 1);
                }
            }
        }

        for (Vector2 corner : cornerCount.keySet()){
            if (cornerCount.get(corner) == 4){
                return true;
            }
        }

        return false;
    }
}
