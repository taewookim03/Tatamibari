package com.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Gayming on 8/31/2016.
 */
public class Tile extends Actor {//Actor vs Image?
    public enum Symbol{
        NONE, HORIZONTAL, VERTICAL, SQUARE
        /*
        NONE:       blank
        HORIZONTAL: -
        VERTICAL:   |
        SQUARE:     +
         */
    }

    private static final float SYMBOL_THICKNESS = 2.5f;

    private final int row;
    private final int col;

    private boolean selected;//current selection (input handling)
    //private boolean assigned;//replace with hasParent/getParent of the Actor class

    private Region region;//region of the board that the tile belongs to

    //private Color color; //this is part of Actor class
    private Symbol symbol;//symbol contained in the tile
    private ShapeRenderer sr;//for drawing the tile

    public Tile(int row, int col, float tileWidth, float tileHeight){
        super();//actor ctor

        this.row = row;
        this.col = col;

        //determine tile width and height based on board length

        setPosition(col*tileWidth, row*tileHeight);
        setWidth(tileWidth);
        setHeight(tileHeight);

        setColor(Color.WHITE);
        symbol = Symbol.NONE;

        sr = new ShapeRenderer();
        setBounds(getX(), getY(), getWidth(), getHeight());

        //move control to stage or board since multiple actors
        /*
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Tile","was clicked");
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Gdx.app.log("Tile","was dragged by");
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        */
    }

    public float getScreenX(){
        return getParent().getX() + getX();
    }
    public float getScreenY(){
        return getParent().getY() + getY();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        float screenX = getScreenX();
        float screenY = getScreenY();

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.setProjectionMatrix(getStage().getCamera().combined);
        //draw tiles (white squares)
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(getColor());
        sr.rect(screenX, screenY, getWidth(), getHeight());
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        //draw tile outline(thin gray lines)
        sr.setColor(Color.GRAY);
        sr.rect(screenX, screenY, getWidth(), getHeight());
        sr.end();

        //draw symbol
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        switch (symbol){
            case HORIZONTAL:
                drawSymbolHorizontal(sr, screenX, screenY, getWidth(), getHeight());
                break;
            case VERTICAL:
                drawSymbolVertical(sr, screenX, screenY, getWidth(), getHeight());
                break;
            case SQUARE:
                drawSymbolHorizontal(sr, screenX, screenY, getWidth(), getHeight());
                drawSymbolVertical(sr, screenX, screenY, getWidth(), getHeight());
                break;
        }
        sr.end();

        batch.begin();
    }

    private void drawSymbolHorizontal(ShapeRenderer sr, float x, float y, float width, float height){
        sr.rectLine(x + getWidth()/3, y + getHeight()/2,
                x + getWidth()*2/3, y + getHeight()/2,
                SYMBOL_THICKNESS);
    }
    private void drawSymbolVertical(ShapeRenderer sr, float x, float y, float width, float height){
        sr.rectLine(x + getWidth()/2, y + getHeight()/3,
                x + getWidth()/2, y + getHeight()*2/3,
                SYMBOL_THICKNESS);
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isSelected() {
        return selected;
    }

    public Region getRegion(){
        return region;
    }


    public boolean isAssignedRegion() {
        return region != null;
    }

    public void setRegion(Region region){
        this.region = region;
        if (region == null){
            this.setColor(Color.WHITE);
        }
        else{
            this.setColor(region.getColor());
        }
        //this.setColor(region.getColor());
    }

    public void setSelected(boolean b, Color color){
        selected = b;
        setColor(color);
        /*
        else{
            setColor(Color.WHITE);
        }
        */
    }

    public void setSelected(boolean b){
        selected = b;
    }



    public Symbol getSymbol(){
        return symbol;
    }

    public void setSymbol(Symbol symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "row: " + getRow() + ", col: " + getCol();
    }
}
