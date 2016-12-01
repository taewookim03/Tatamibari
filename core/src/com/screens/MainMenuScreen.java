package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gameobjects.Board;
import com.gameworld.GameWorld;
import com.tatamibari.TatamibariGame;

/**
 * Created by Gayming on 9/8/2016.
 */
public class MainMenuScreen implements Screen {
    private TatamibariGame game;//need this to set screen based on which menu item is chosen
    private OrthographicCamera camera;
    private GameWorld world;//stage containing game elements, to be initiated from here
    private Stage menuStage;
    private Skin skin;
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

        //later have multiple options based on which button is touched, instantiate different sizes etc.
        if (Gdx.input.isTouched()){
            System.out.println("main menu touch registered");
            world = new GameWorld(5, 5);//instantiate world based on which size board is touched
            game.setScreen(new GameScreen(world));
            dispose();
        }

        //Still need to handle when touch dragged, which causes a crash

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
