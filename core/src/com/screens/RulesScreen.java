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
import com.gameobjects.Board;
import com.tatamibari.TatamibariGame;


public class RulesScreen implements Screen {
    private TatamibariGame game;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;

    public RulesScreen(TatamibariGame game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.apply();//what does this do?
        camera.setToOrtho(false);
        camera.update();
        stage = new Stage(viewport);
        stage.addActor(new Board(4, 4, 125, 125, 0.5f));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        table.left();

        //create instruction labels
        //BitmapFont font = new BitmapFont(Gdx.files.internal("arial_small.fnt"));
        //Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);

        Label howToPlay = new Label("Divide the board into rectangles. Touch and drag to assign a rectangle on the board.\n" +
                "The divided rectangles must follow the following rules:", game.skin);
        Label rule1 = new Label("1. Each rectangle must contain exactly one symbol.", game.skin);
        Label rule2 = new Label("2. A rectangle with a + symbol must be a square", game.skin);
        Label rule3 = new Label("3. A rectangle with a - symbol must have a width greater than its height.", game.skin);
        Label rule4 = new Label("4. A rectangle with a | symbol must have a height greater than its width.", game.skin);
        Label rule5 = new Label("5. Four rectangles may not share the same corner.", game.skin);
        Label goal = new Label("The goal of the game is to completely fill the board with rectangles. Good luck!", game.skin);

        //add instructions to table
        table.add(howToPlay).pad(10).align(Align.left).row();
        table.add(rule1).pad(10).align(Align.left).row();
        table.add(rule2).pad(10).align(Align.left).row();
        table.add(rule3).pad(10).align(Align.left).row();
        table.add(rule4).pad(10).align(Align.left).row();
        table.add(rule5).pad(10).align(Align.left).row();
        //table.add(goal).pad(10).align(Align.left);

        //add table
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
