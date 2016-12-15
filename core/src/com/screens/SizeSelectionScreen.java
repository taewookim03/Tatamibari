package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gameworld.GameWorld;
import com.tatamibari.TatamibariGame;


public class SizeSelectionScreen implements Screen {
    private TatamibariGame game;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;

    public SizeSelectionScreen(TatamibariGame game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new ScreenViewport();
        viewport.apply();//what does this do?
        camera.setToOrtho(false);
        camera.update();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);//set table to fill the whole stage
        table.center();//alignment of contents

        TextButton play4x4Button = new TextButton("4x4", game.skin);
        TextButton play5x5Button = new TextButton("5x5", game.skin);
        TextButton play6x6Button = new TextButton("6x6", game.skin);
        TextButton play8x8Button = new TextButton("8x8", game.skin);
        TextButton play10x10Button = new TextButton("10x10", game.skin);
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

        //get largest button size and make all buttons same size
        float buttonWidth = play10x10Button.getWidth();
        float buttonHeight = play10x10Button.getHeight();

        table.add(play4x4Button).size(buttonWidth, buttonHeight).pad(10).row();
        table.add(play5x5Button).size(buttonWidth, buttonHeight).pad(10).row();
        table.add(play6x6Button).size(buttonWidth, buttonHeight).pad(10).row();
        table.add(play8x8Button).size(buttonWidth, buttonHeight).pad(10).row();
        table.add(play10x10Button).size(buttonWidth, buttonHeight).pad(10).row();

        stage.addActor(table);

        //add a back button
        TextButton backButton = new TextButton("Back to Main Menu", game.skin);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenuScreen(game));
            }
        });
        backButton.setX(Gdx.graphics.getWidth() - backButton.getWidth());
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        stage.draw();
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
        stage.dispose();
    }
}
