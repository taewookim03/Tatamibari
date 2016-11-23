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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gamelogic.GameLogic;
import com.gameobjects.Board;
import com.gameobjects.Region;
import com.gameobjects.Tile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * manages game objects (tile and board (group of tiles)
 *
 * due to simplicity don't use this class and just use the default Stage claess with custom input inside the TatamibariGame class?
 */
public class GameWorld extends Stage {

    public enum GameState{//from menu, choose tile size etc. and run game.
        RUNNING, SOLVED//MENU is handled by a separate screen
    }

    private GameState currentState;
    private Board board;

    //UI
    private Skin skin;
    //public TextButton toMenuButton;
    public Dialog endDialog;

    public GameWorld(int rows, int cols){
        super(new ScreenViewport());

        currentState = GameState.RUNNING;
        board = new Board(rows, cols);

        //UI
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        /*
        toMenuButton = new TextButton("Return", skin, "default");
        //toMenuButton.setWidth(100);
        //toMenuButton.setHeight(20);

        addActor(toMenuButton);

        toMenuButton.setColor(Color.RED);

        //toMenuButton.setVisible(false);
        toMenuButton.toFront();
        toMenuButton.setPosition(Gdx.graphics.getWidth()/2 - toMenuButton.getWidth()/2,
                Gdx.graphics.getHeight()/2 - toMenuButton.getHeight()/2 - 50);

        toMenuButton.setPosition(0,0);

        toMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("button clicked");
            }
        });
        */
        addActor(board);

        /*
        endDialog = new Dialog("Click Message", skin){
            public void result(Object obj){
                System.out.println("result " + obj);
            }
        };

        //endDialog.setModal(true);
        //endDialog.setMovable(false);
        //endDialog.setResizable(false);
        //endDialog.show(this).setPosition(100,100);
        endDialog.setName("endDialog");

        Button b = new TextButton("A", skin, "default");

        b.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Clicked", "you did");
            }
        });
        endDialog.button(b);
        endDialog.button("Yes", true);
        //endDialog.show(this);
        //addActor(endDialog);
        //addActor(board);
        //endDialog.show(this);

        //addActor(endDialog);
        //endDialog.hide();
        //endDialog.setTouchable(Touchable.disabled);

        //endDialog.toFront();
        */
    }

    @Override
    public Tile hit(float stageX, float stageY, boolean touchable) {
        if (super.hit(stageX, stageY, touchable) instanceof Tile){
            return (Tile)super.hit(stageX, stageY, touchable);
        }
        return null;
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

    public void showDialog() {
        Dialog dialog = new Dialog("Quit?", skin) {

            @Override
            protected void result(Object object) {
                boolean exit = (Boolean) object;
                if (exit) {
                    Gdx.app.exit();
                } else {
                    remove();
                }
            }

            @Override
            public Dialog show(Stage stage) {
                return super.show(stage);
            }

            @Override
            public void cancel() {
                super.cancel();
            }

            @Override
            public float getPrefHeight() {
                return 50f;
            }
        };
        dialog.button("Yes", true);
        dialog.button("No", false);
        dialog.key(Input.Keys.ENTER, true);
        dialog.key(Input.Keys.ESCAPE, false);
        //addActor(dialog);
        dialog.show(this);

    }
/*
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("GameWorld touchDown returning false");
        return false;//default false
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    */
}
