package com.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.gamelogic.GameLogic;

import java.util.*;

/**
 * A group of tiles as well as regions (tracks both)
 */
public class Board extends Group {
    private final float OUTLINE_THICKNESS;
    private final int rows;
    private final int cols;
    private Tile[][] tiles;

    private List<Region> regions;//maybe initialize regions with different colors

    private ShapeRenderer sr;

    private GameLogic logic;

    public Board(int rows, int cols){
        this(rows, cols, getDefaultLength(), getDefaultLength(), getOutlineThickness());//default board size
    }

    public Board(int rows, int cols, float width, float height, float outlineThickness) {
        super();
        OUTLINE_THICKNESS = outlineThickness;
        this.rows = rows;
        this.cols = cols;
        final float tileWidth = width / cols;
        final float tileHeight = height / rows;

        tiles = new Tile[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tiles[i][j] = new Tile(i, j, tileWidth, tileHeight);
                addActor(tiles[i][j]);//adding tiles to Board as actors
            }
        }

        setWidth(tiles[0][0].getWidth() * cols);//error handling?
        setHeight(tiles[0][0].getHeight() * rows);

        setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight() / 2);
        //System.out.println(getX() + ", " + getY());

        regions = new ArrayList<Region>();

        sr = new ShapeRenderer();

        logic = new GameLogic(this);

        randomize();
    }

    private static float getDefaultLength(){
        //calculate appropriate board size based on screen size
        //get smaller of the screen height and width
        float screenLength = Gdx.graphics.getHeight() < Gdx.graphics.getWidth() ?
                Gdx.graphics.getHeight() : Gdx.graphics.getWidth();
        return screenLength * 0.8f;
    }
    private static float getOutlineThickness(){
        return getDefaultLength() / 300.0f;
    }

    private void randomize(){
        //randomly generate a problem
        //pass recursive division depth for each board size option
        //NOTE the depth parameter's impact on the board also depends on the depth reduction parameter, which is randomly chosen
        /*
        appropriate recursive depths determined by trial and error
        4x4: 5
        5x5: 6
        6x6: 7
        8x8: 8
        10x10: 9
         */
        int divisionDepth = 0;
        int divReductionCoeff = 3;
        switch (rows){//assuming square board
            case 4:
                divisionDepth = 5; break;
            case 5:
                divisionDepth = 6; break;
            case 6:
                divisionDepth = 7; break;
            case 8:
                divisionDepth = 8; break;
            case 10:
                divisionDepth = 9; break;
        }
        logic.generateRandomProblem(divisionDepth, divReductionCoeff);//default depth reduction coefficient to 3 (1 - 3)
    }

    public Tile getTile(int row, int col) {
        try{
            return tiles[row][col];
        }
        catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            Gdx.app.log("getTile", "index out of bounds");
            return null;
        }
    }

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

    //left color parameter in case of future implementation of differently colored regions
    public void select(Tile firstTile, Tile lastTile, Color color){
        Set<Tile> tilesSelected = getRectangularSelection(firstTile, lastTile);
        for (Tile t : tilesSelected){
            t.setSelected(true, color);
        }
    }

    public void select(Tile firstTile, Tile lastTile){
        select(firstTile, lastTile, Color.WHITE);
    }

    public void addRegion(Region newRegion){
        newRegion.addSelectedTiles();
        regions.add(newRegion);
    }
    public void addRegion(){
        addRegion(new Region(this));
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

    public Set<Tile> getRectangularSelection(Tile firstTile, Tile lastTile){
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
        super.draw(batch, parentAlpha);//draw tiles

        //http://stackoverflow.com/questions/16381106/libgdx-shaperenderer-in-group-draw-renders-in-wrong-colour
        //also note performance hit of batch being/end

        //draw regions
        for (Region region : regions){
            region.draw(batch, parentAlpha);
        }

        //draw the four edges of the board
        batch.end();//see above link

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.setProjectionMatrix(getStage().getCamera().combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.rectLine(getX(), getY(), getX() + getWidth(), getY(), OUTLINE_THICKNESS);//bottom
        sr.rectLine(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight(), OUTLINE_THICKNESS);//top
        sr.rectLine(getX(), getY(), getX(), getY() + getHeight(), OUTLINE_THICKNESS);//left
        sr.rectLine(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight(), OUTLINE_THICKNESS);//right
        sr.end();

        batch.begin();
    }

    public boolean isFilled(){
        for (Actor actor : getChildren()){
            Tile tile = (Tile)actor;
            if (tile.getRegion() == null){
                return false;
            }
        }
        return true;
    }

    public void removeAllRegions(){
        while (!regions.isEmpty()){
            removeRegion(regions.get(0));
        }
    }

    public void removeAllSymbols(){
        for (Tile[] tileRow : tiles){
            for (Tile tile : tileRow){
                tile.setSymbol(Tile.Symbol.NONE);
            }
        }
    }

    public void setDrawTileOutlines(boolean b){
        for (Tile[] tileRow : tiles){
            for (Tile tile : tileRow){
                tile.setDrawOutline(b);
            }
        }
    }
    /*
    public Tile[][] getTiles(){
        return tiles;
    }
    */

}
