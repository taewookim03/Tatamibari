package com.gameobjects;

import com.badlogic.gdx.graphics.Color;
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

    private int row;
    private int col;

    private boolean selected;//current selection (input handling)
    private boolean assigned;//boolean for whether the tile has been assigned a color or not

    //private Color color; //this is part of Actor class
    private Symbol symbol;

    private ShapeRenderer sr;

    public Tile(int row, int col){
        super();
        this.row = row;
        this.col = col;
        setPosition(row*50, col*50);
        setWidth(50);
        setHeight(50);

        setColor(Color.WHITE);
        symbol = Symbol.NONE;
        assigned = false;

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

    @Override
    public void draw(Batch batch, float parentAlpha) {

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(getColor());
        sr.rect(getParent().getX()+getX(), getParent().getY()+getY(), getWidth(), getHeight());
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        sr.rect(getParent().getX()+getX(), getParent().getY()+getY(), getWidth(), getHeight());
        sr.end();
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

    public boolean isAssigned() {
        return assigned;
    }


    public void setAssigned(boolean b){
        assigned = b;
    }
    public void setSelected(boolean b){
        selected = b;
        if (selected){
            setColor(Color.RED);
        }
        else{
            setColor(Color.WHITE);
        }
    }

    public Symbol getSymbol(){
        return symbol;
    }

    public void setSymbol(Symbol symbol){
        this.symbol = symbol;
    }

}
