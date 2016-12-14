package com.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gameobjects.Board;

/**
 * manages game objects (tile, region and board)
 */
public class GameWorld extends Stage {

    public enum GameState{
        RUNNING, SOLVED
    }

    private GameState currentState;
    private Board board;


    public GameWorld(int rows, int cols){
        super(new ScreenViewport());
        currentState = GameState.RUNNING;
        board = new Board(rows, cols);

        addActor(board);
    }

/*
    OVERRIDING HIT WAS CAUSING THE ISSUE WHERE DIALOG COULD NOT BE CLICKED. FINALLY FOUND THIS BUG!!!
    @Override
    public Tile hit(float stageX, float stageY, boolean touchable) {
        if (super.hit(stageX, stageY, touchable) instanceof Tile){
            return (Tile)super.hit(stageX, stageY, touchable);
        }
        return null;
    }
*/

    public Board getBoard(){
        return board;
    }

    public boolean isRunning(){
        return currentState == GameState.RUNNING;
    }

    public boolean isSolved(){
        return currentState == GameWorld.GameState.SOLVED;
    }

    public void setSolved(){
        currentState = GameState.SOLVED;
    }

    @Override
    public void draw() {
        super.draw();
    }
}
