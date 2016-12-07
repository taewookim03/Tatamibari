package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gameworld.GameWorld;
import com.helpers.InputHandler;
import com.tatamibari.TatamibariGame;

/**
 * might not use this
 */
public class GameScreen implements Screen {
    private TatamibariGame game;
    private GameWorld world;

    public GameScreen(TatamibariGame game, GameWorld world){
        this.game = game;
        this.world = world;//world is instantiated in main menu and passed in

    }

    @Override
    public void show() {
        world.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        InputMultiplexer im = new InputMultiplexer();

        im.addProcessor(world);//input for the dialog
        im.addProcessor(new InputHandler(game, world));
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        //world.act();seems unnecessary
        world.getCamera().update();
        world.draw();
    }



    @Override
    public void resize(int width, int height) {
        world.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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
        world.dispose();
    }
}
