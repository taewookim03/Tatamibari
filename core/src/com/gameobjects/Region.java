package com.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * A region is a user-selected group of tiles that form a rectangle. A region must contain exactly one symbol
 * and its dimensions must match the symbol.
 */
public class Region {
    //Region contains tiles and stores information about the region such as:
    //symbol, symbol location, all tiles selected (part of group), color, etc.
    private Color color;
    private List<Tile> tiles;

    private Board board;

    private boolean draw;
    private ShapeRenderer sr;

    private static float BORDER_THICKNESS;

    public Region(Board board){
        this.board = board;
        //color = new Color(Color.rgba8888(255/255f, 255/255f, 0/255f, 0.3f));//light yellow
        color = new Color(Color.rgba8888(1f, 0.627451f, 0.08235294f, 0.5f));
        tiles = new ArrayList<Tile>();
        draw = true;
        sr = new ShapeRenderer();

        BORDER_THICKNESS = board.getOutlineThickness() * 3 / 2;
    }

    public Region(Board board, Color color){//idea: maybe color-code -, |, + ?
        this(board);
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public void addSelectedTiles(){
        for (Actor actor : board.getChildren()) {
            Tile tile = (Tile) actor;
            if (tile.isSelected()){
                addTile(tile);//sets color here as well and gets the position data for drawing the region
            }
        }
    }

    public void addTile(Tile tile){
        tile.setRegion(this);
        tile.setColor(color);
        tiles.add(tile);

    }
    //need some kind of a container of colors to assign to each region and free up as a region is deleted, etc.

    public void clearRegionFromTiles(){
        for (Tile tile : tiles){
            if (this.equals(tile.getRegion())){
                tile.setRegion(null);
            }
        }
        tiles.clear();
    }

    public List<Tile> getTiles(){
        return tiles;
    }

    public Tile getFirstTile(){
        try{
            return tiles.get(0);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            Gdx.app.log("getFirstTile", "index out of bounds");
            return null;
        }
    }
    public Tile getLastTile(){
        try{
            return tiles.get(tiles.size() - 1);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            Gdx.app.log("getLastTile", "index out of bounds. index=" + (tiles.size() - 1));
            return null;
        }
    }

    public int getRows(){//calculates the number of rows
        try{
            return getLastTile().getRow() - getFirstTile().getRow() + 1;
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            Gdx.app.log("getRows", "something wrong with getFirstTile and/or getLastTile: "
                    + (getLastTile().getRow() - getFirstTile().getRow() + 1));
            return -1;
        }
    }

    public int getCols(){//calculates the number of columns
        try{
            return getLastTile().getCol() - getFirstTile().getCol() + 1;
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            Gdx.app.log("getCols", "something wrong with getFirstTile and/or getLastTile: "
            + (getLastTile().getCol() - getFirstTile().getCol() + 1));
            return -1;
        }
    }

    public void refresh(){
        for (Tile tile : tiles){
            tile.setRegion(this);
        }
        setDraw(true);
    }

    public int getBottomRow(){
        return tiles.get(0).getRow();
    }
    public int getTopRow(){
        return tiles.get(tiles.size() - 1).getRow();
    }
    public int getLeftCol(){
        return tiles.get(0).getCol();
    }
    public int getRightCol(){
        return tiles.get(tiles.size() - 1).getCol();
    }

    public Vector2 getUpperLeftCorner(){
        return new Vector2(tiles.get(0).getScreenX(),
                tiles.get(tiles.size() - 1).getScreenY() + tiles.get(tiles.size() - 1).getHeight());
    }
    public Vector2 getUpperRightCorner(){
        return new Vector2(tiles.get(tiles.size() - 1).getScreenX() + tiles.get(tiles.size() - 1).getWidth(),
                tiles.get(tiles.size() - 1).getScreenY() + tiles.get(tiles.size() - 1).getHeight());
    }
    public Vector2 getLowerLeftCorner(){
        return new Vector2(tiles.get(0).getScreenX(), tiles.get(0).getScreenY());
    }
    public Vector2 getLowerRightCorner(){
        return new Vector2(tiles.get(tiles.size() - 1).getScreenX() + tiles.get(tiles.size() - 1).getWidth(),
                tiles.get(0).getScreenY());
    }

    public void setDraw(boolean d){
        draw = d;
        if (draw){
            for (Tile tile : tiles){
                tile.setColor(color);
            }
        }
        else{
            for (Tile tile : tiles){
                tile.setColor(Color.WHITE);
            }
        }
    }

    public void draw(Batch batch, float parentAlpha){
        //Gdx.app.log("region", "draw function called");
        if (draw){
            if (tiles.size() == 0){
                return;
            }

            Vector2 minXY = getLowerLeftCorner();
            Vector2 maxXY = getUpperRightCorner();

            //drawing border for the region
            batch.end();//end other batch before beginning shaperenderer

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.BLACK);

            //draw region borders
            sr.rectLine(minXY.x, maxXY.y, maxXY.x, maxXY.y, BORDER_THICKNESS);//top
            sr.rectLine(minXY.x, minXY.y, maxXY.x, minXY.y, BORDER_THICKNESS);//bottom
            sr.rectLine(minXY.x, minXY.y, minXY.x, maxXY.y, BORDER_THICKNESS);//left
            sr.rectLine(maxXY.x, minXY.y, maxXY.x, maxXY.y, BORDER_THICKNESS);//right

            sr.end();

            batch.begin();
        }
    }

    public boolean hasOneSymbol(){
        //count symbol tiles
        int symbolCount = 0;
        for (Tile tile : tiles){
            if(tile.getSymbol() != Tile.Symbol.NONE){
                symbolCount++;
            }
        }
        return symbolCount == 1;
    }

    public Tile getSymbolTile(){
        for (Tile tile : tiles){
            if (tile.getSymbol() != Tile.Symbol.NONE){
                return tile;
            }
        }
        return null;
    }

    public boolean matchesSymbol(){
        Tile symbolTile = getSymbolTile();

        if (symbolTile == null){
            Gdx.app.debug("matchesSymbol", "symbolTile is null");
            return false;
        }

        switch(symbolTile.getSymbol()){
            case HORIZONTAL:
                return isHorizontal();
            case VERTICAL:
                return isVertical();
            case SQUARE:
                return isSquare();
        }
        return false;
    }

    public boolean isVertical(){
        return getRows() > getCols();
    }
    public boolean isHorizontal(){
        return getRows() < getCols();
    }
    public boolean isSquare(){
        return getRows() == getCols();
    }

    @Override
    public String toString() {
        return "region " + getFirstTile() + " to " + getLastTile();
    }
}
