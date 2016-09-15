package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gameobjects.Board;
import com.gameworld.GameWorld;
import com.tatamibari.TatamibariGame;

/**
 * Created by Gayming on 9/8/2016.
 */
public class MainMenuScreen implements Screen {

    private TatamibariGame game;
    OrthographicCamera camera;

    public MainMenuScreen(TatamibariGame game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "HEELLLLOOOO", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

        game.batch.end();

        if (Gdx.input.isTouched()){
            System.out.println("main menu touch registered");
            game.setWorld(new GameWorld(5,5));
            game.setScreen(new GameScreen(game));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
