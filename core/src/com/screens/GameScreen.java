package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gameworld.GameWorld;
import com.tatamibari.TatamibariGame;

/**
 * might not use this
 */
public class GameScreen implements Screen {

    private TatamibariGame game;
    private GameWorld world;

    public GameScreen(TatamibariGame game){
        this.game = game;
        world = game.getWorld();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        world.act();
        world.draw();
        world.getCamera().update();

        //world.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        if (world.getState() == GameWorld.GameState.SOLVED){
            /*
            game.batch.begin();
            game.font.setColor(Color.PINK);
            game.font.draw(game.batch, "Click to go back to main menu", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
            game.batch.end();
            */
            world.endDialog.show(world).setPosition(100,100);
            //world.toMenuButton.setVisible(true);
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
        world.dispose();
    }
}
