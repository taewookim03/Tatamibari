package com.gameworld;

import com.badlogic.gdx.scenes.scene2d.Stage;
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
