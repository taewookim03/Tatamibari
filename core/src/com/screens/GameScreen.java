package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gameworld.GameWorld;
import com.helpers.InputHandler;
import com.tatamibari.TatamibariGame;

/**
 * Manages the screen for the game being played
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

        //add a give up button to go back to main menu
        TextButton giveUpButton = new TextButton("Give Up", game.skin);
        giveUpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new SizeSelectionScreen(game));
            }
        });
        giveUpButton.setX(Gdx.graphics.getWidth() - giveUpButton.getWidth());
        world.addActor(giveUpButton);


        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(world);//input processor for the dialog
        im.addProcessor(new InputHandler(game, world));
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

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
