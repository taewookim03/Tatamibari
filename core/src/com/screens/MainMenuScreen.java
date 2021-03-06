package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tatamibari.TatamibariGame;

/**
 * Main menu screen
 */

public class MainMenuScreen implements Screen {
    private TatamibariGame game;//need this to set screen based on which menu item is chosen
    private OrthographicCamera camera;
    private Stage menuStage;
    private Viewport viewport;

    public MainMenuScreen(TatamibariGame game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.apply();
        camera.setToOrtho(false);
        camera.update();
        //instantiate world based on which size board is touched
        menuStage = new Stage(viewport);
        Gdx.input.setInputProcessor(menuStage);
    }
    @Override
    public void show() {
        Table menuTable = new Table();
        menuTable.setFillParent(true);//set table to fill the whole stage
        menuTable.center();//alignment of contents

        //create menu buttons
        //Label menuPrompt = new Label("", game.skin);
        Label title = new Label("Tatamibari", game.skin, "title");
        TextButton rulesButton = new TextButton("How to Play", game.skin);
        TextButton playButton = new TextButton("Play", game.skin);
        //TextButton quitButton = new TextButton("Quit", game.skin);
        TextButton aboutButton = new TextButton("About", game.skin);

        //add listeners to buttons
        rulesButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new RulesScreen(game));
            }
        });
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new SizeSelectionScreen(game));
            }
        });
        aboutButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new AboutScreen(game));
            }
        });
        /*
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
        */
        //calculate button sizes - use rules button, which is the largest
        float buttonWidth = rulesButton.getWidth();
        float buttonHeight = rulesButton.getHeight();

        //add buttons to table
        int padAmount = (int)(Gdx.graphics.getHeight() * 0.02);
        //menuTable.add(menuPrompt).pad(padAmount)row();
        menuTable.add(title).pad((int)(Gdx.graphics.getHeight() * 0.1)).row();
        menuTable.add(rulesButton).pad(padAmount).row();
        menuTable.add(playButton).size(buttonWidth, buttonHeight).pad(padAmount).row();
        menuTable.add(aboutButton).size(buttonWidth, buttonHeight).pad(padAmount).row();
        //menuTable.add(quitButton).size(buttonWidth, buttonHeight).pad(padAmount);
        menuTable.setY(menuTable.getY() + (int)(Gdx.graphics.getHeight() * 0.06));//move table up a little

        //add table to stage
        menuStage.addActor(menuTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        menuStage.draw();
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
        menuStage.dispose();
    }
}
