package com.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
        //camera.setToOrtho(false, 800, 480);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
