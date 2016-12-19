package com.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;


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

    private final float SYMBOL_THICKNESS;

    private final int row;
    private final int col;

    private boolean selected;//current selection (input handling)
    private boolean drawOutline = true;//this is an option to allow drawing a board without tile outlines

    private Region region;//region of the board that the tile belongs to

    private Symbol symbol;//symbol contained in the tile
    private static ShapeRenderer sr;//for drawing the tile
    private static Texture pmTexture;//pixmap texture for drawing methods

    public Tile(int row, int col, float tileWidth, float tileHeight){
        super();//actor ctor

        this.row = row;
        this.col = col;

        //determine tile width and height based on board size
        setPosition(col*tileWidth, row*tileHeight);
        setWidth(tileWidth);
        setHeight(tileHeight);

        //scale symbol line thickness based on tile size
        //thickness:tile size
        SYMBOL_THICKNESS = Math.round(tileWidth / 35.0f * 10.0)/10.0f;//round to 1 decimal

        setColor(1, 1, 1, 0.5f);
        symbol = Symbol.NONE;

        sr = new ShapeRenderer();
        setBounds(getX(), getY(), getWidth(), getHeight());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pmTexture = new Texture(pixmap);
        //Pixmap.setBlending(Pixmap.Blending.None);
        pixmap.dispose();//pixmap is not automatically garbage collected

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
        //this function originally used shaperenderer to render the shapes and lines, but due to performance hit
        //a single batch with a dynamically generated texture from pixelmap was used to draw

        //draw the tile (square)
        batch.setColor(getColor());
        batch.draw(pmTexture, getX(), getY(), getWidth(), getHeight());

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //draw symbol
        if(symbol != Symbol.NONE){
            batch.setColor(Color.BLACK);
            switch (symbol){
                case HORIZONTAL:
                    drawSymbolHorizontal(batch);
                    break;
                case VERTICAL:
                    drawSymbolVertical(batch);
                    break;
                case SQUARE:
                    drawSymbolHorizontal(batch);
                    drawSymbolVertical(batch);
                    break;
            }

        }

        //draw tile outline(thin gray lines)
        if (drawOutline){
            batch.setColor(0.5f, 0.5f, 0.5f, 0.3f);//transparent gray with 0.3 alpha
            float screenLength = Gdx.graphics.getHeight() < Gdx.graphics.getWidth() ?
                    Gdx.graphics.getHeight() : Gdx.graphics.getWidth();
            float thickness = Math.round(screenLength / 800.0f * 10.0f) / 10.0f;//round to 1 decimal
            thickness = thickness > 0.5f ? thickness : 0.5f;//minimum 0.5

            batch.draw(pmTexture, getX(), getY(), getWidth(), thickness);
            batch.draw(pmTexture, getX(), getY(), thickness, getHeight());
            batch.draw(pmTexture, getX(), getY() + getHeight() - thickness, getWidth(), thickness);
            batch.draw(pmTexture, getX() + getWidth() - thickness, getY(), thickness, getHeight());
        }
    }

    private void drawSymbolHorizontal(Batch batch){
        batch.draw(pmTexture, getX() + getWidth() / 3, getY() + getHeight() / 2 - SYMBOL_THICKNESS / 2,
                getWidth() / 3, SYMBOL_THICKNESS);
    }
    private void drawSymbolVertical(Batch batch){
        batch.draw(pmTexture, getX() + getWidth() / 2 - SYMBOL_THICKNESS / 2, getY() + getHeight() / 3,
                SYMBOL_THICKNESS, getHeight() / 3);
    }

    public void setDrawOutline(boolean b){
        drawOutline = b;
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

    public void setRegion(Region region){
        this.region = region;
        if (region == null){
            this.setColor(Color.WHITE);
        }
        else{
            this.setColor(region.getColor());
        }
    }

    public void setSelected(boolean b, Color color){
        selected = b;
        setColor(color);
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
        return getRow() + "," + getCol();
    }
}
