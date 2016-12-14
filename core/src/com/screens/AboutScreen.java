package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tatamibari.TatamibariGame;

/**
 * Created by Gayming on 12/14/2016.
 */
public class AboutScreen implements Screen {
    private TatamibariGame game;
    private Stage stage;
    private OrthographicCamera camera;

    public AboutScreen(TatamibariGame game){
        this.game = game;
        camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        viewport.apply();//what does this do?
        camera.setToOrtho(false);
        camera.update();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        table.left();

        Label gameDescription = new Label("Tatamibari is a logic puzzle game by Nikoli based on Japanse tatami mats.\n" +
                "2:1 rectangular mats (and optional square mats) are laid out in different directions\n" +
                "(horizontal or vertical) to compose the flooring of a traditional Japanese room.", game.skin);
        Label comment = new Label("I enjoyed solving problems that were available online, but I quickly ran out\n" +
                "and just had to implement a random problem generator for the game.\n" +
                "I do not own any copyrights to the game.", game.skin);
        Label credit = new Label("Programmed by Taewoo Kim\ntaewookim03@gmail.com", game.skin);
        table.add(gameDescription).align(Align.left).row();
        table.add(comment).align(Align.left).row();
        table.add(credit).align(Align.left).row();

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
