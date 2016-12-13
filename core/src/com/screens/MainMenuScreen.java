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
    //private GameWorld world;//stage containing game elements, to be initiated when user chooses board size and difficulty
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
        Table menuTable = new Table();
        menuTable.setFillParent(true);//set table to fill the whole stage
        menuTable.center();//alignment of contents

        //create menu buttons
        //Label menuPrompt = new Label("", game.skin);
        TextButton play4x4Button = new TextButton("4x4", game.skin);
        TextButton play5x5Button = new TextButton("5x5", game.skin);
        TextButton play6x6Button = new TextButton("6x6", game.skin);
        TextButton play8x8Button = new TextButton("8x8", game.skin);
        TextButton play10x10Button = new TextButton("10x10", game.skin);
        TextButton rulesButton = new TextButton("How to Play", game.skin);
        TextButton quitButton = new TextButton("Quit", game.skin);

        //add listeners to buttons
        rulesButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new RulesScreen(game));
            }
        });
        play4x4Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game, new GameWorld(4, 4)));
            }
        });
        play5x5Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game, new GameWorld(5, 5)));
            }
        });
        play6x6Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game, new GameWorld(6, 6)));
            }
        });
        play8x8Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game, new GameWorld(8, 8)));
            }
        });
        play10x10Button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game, new GameWorld(10, 10)));
            }
        });
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        //add buttons to table
        //menuTable.add(menuPrompt);
        //menuTable.row();
        menuTable.add(rulesButton);
        menuTable.row();
        menuTable.add(play4x4Button);
        menuTable.row();
        menuTable.add(play5x5Button);
        menuTable.row();
        menuTable.add(play6x6Button);
        menuTable.row();
        menuTable.add(play8x8Button);
        menuTable.row();
        menuTable.add(play10x10Button);
        menuTable.row();
        menuTable.add(quitButton);

        //add table to stage
        menuStage.addActor(menuTable);

        //implement difficulty as well (parameter to GameWorld which calls random problem generator)

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //menuStage.act();
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
