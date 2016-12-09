package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gameworld.GameWorld;
import com.tatamibari.TatamibariGame;

/**
 * Created by Gayming on 9/8/2016.
 */
public class MainMenuScreen implements Screen {
    private TatamibariGame game;//need this to set screen based on which menu item is chosen
    private OrthographicCamera camera;
    private GameWorld world;//stage containing game elements, to be initiated when user chooses board size and difficulty
    private Stage menuStage;
    private Viewport viewport;


    public MainMenuScreen(TatamibariGame game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.apply();//what does this do?
        camera.setToOrtho(false);
        camera.update();
        //instantiate world based on which size board is touched
        menuStage = new Stage(viewport);
        Gdx.input.setInputProcessor(menuStage);
    }
    @Override
    public void show() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);//set table to fill the whole stage
        mainTable.center();//alignment of contents

        //create menu buttons
        Label sizePrompt = new Label("Choose a size:", game.skin);
        TextButton play5x5Button = new TextButton("5x5", game.skin);
        TextButton play10x10Button = new TextButton("10x10", game.skin);
        TextButton quitButton = new TextButton("Quit", game.skin);

        //add listeners to buttons
        play5x5Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                world = new GameWorld(5, 5);
                game.setScreen(new GameScreen(game, world));
            }
        });
        play10x10Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                world = new GameWorld(10, 10);
                game.setScreen(new GameScreen(game, world));
            }
        });
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        //add buttons to table
        mainTable.add(sizePrompt);
        mainTable.row();
        mainTable.add(play5x5Button);
        mainTable.row();
        mainTable.add(play10x10Button);
        mainTable.row();
        mainTable.add(quitButton);

        //add table to stage
        menuStage.addActor(mainTable);

        //implement difficulty as well (parameter to GameWorld which calls random problem generator)

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuStage.act();
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

    }

}
