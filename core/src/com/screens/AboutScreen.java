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
import com.gameobjects.Region;
import com.gameobjects.Tile;
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
        //create a couple small boards for tatami layout illustration
        Board board1 = new Board(3, 3, Gdx.graphics.getHeight() / 4, Gdx.graphics.getHeight() / 4, 0.0f);
        board1.removeAllSymbols();
        //set all tile outlines to not draw
        board1.setDrawTileOutlines(false);

        board1.select(board1.getTile(0, 0), board1.getTile(0, 1));
        board1.addRegion();
        board1.clearSelection();
        board1.select(board1.getTile(0, 2), board1.getTile(1, 2));
        board1.addRegion();
        board1.clearSelection();
        board1.select(board1.getTile(1, 0), board1.getTile(2, 0));
        board1.addRegion();
        board1.clearSelection();
        board1.select(board1.getTile(2, 1), board1.getTile(2, 2));
        board1.addRegion();
        board1.clearSelection();
        board1.select(board1.getTile(1, 1), board1.getTile(1, 1));
        board1.addRegion();
        board1.clearSelection();

        stage.addActor(board1);

        Board board2 = new Board(4, 4, Gdx.graphics.getHeight() / 3, Gdx.graphics.getHeight() / 3, 0.0f);
        board2.removeAllSymbols();
        board2.setDrawTileOutlines(false);

        board2.select(board2.getTile(0, 0), board2.getTile(0, 1));
        board2.addRegion();
        board2.clearSelection();
        board2.select(board2.getTile(0, 2), board2.getTile(0, 3));
        board2.addRegion();
        board2.clearSelection();
        board2.select(board2.getTile(1, 0), board2.getTile(2, 0));
        board2.addRegion();
        board2.clearSelection();
        board2.select(board2.getTile(1, 1), board2.getTile(1, 2));
        board2.addRegion();
        board2.clearSelection();
        board2.select(board2.getTile(2, 1), board2.getTile(2, 2));
        board2.addRegion();
        board2.clearSelection();
        board2.select(board2.getTile(1, 3), board2.getTile(2, 3));
        board2.addRegion();
        board2.clearSelection();
        board2.select(board2.getTile(3, 0), board2.getTile(3, 1));
        board2.addRegion();
        board2.clearSelection();
        board2.select(board2.getTile(3, 2), board2.getTile(3, 3));
        board2.addRegion();
        board2.clearSelection();

        board1.setPosition((Gdx.graphics.getWidth() - (board1.getWidth() * 2 + board2.getWidth())) / 2,
                Gdx.graphics.getHeight() - board2.getHeight() * 1.2f);
        board2.setPosition(board1.getX() + board1.getWidth() * 2,
                Gdx.graphics.getHeight() - board2.getHeight() * 1.25f);

        stage.addActor(board2);


        Table table = new Table();
        //table.setFillParent(true);
        //table.left();

        Label gameDescription = new Label("Tatamibari is a logic puzzle game by Nikoli based on Japanse tatami mats.\n" +
                "In a traditional Japanese room, the flooring consists of 2:1 rectangular mats\n" +
                "and optional square mats, which are laid out in a variety of configurations.\n" +
                "Above are some examples of tatami room layouts.", game.skin, "small");
        Label comment = new Label("I enjoyed solving problems that were available online, but I quickly ran out\n" +
                "and just had to implement a random problem generator for the game.", game.skin, "small");
        Label credit = new Label("Programmed by Taewoo Kim\ntaewookim03@gmail.com", game.skin, "small");

        float padAmount = (int)(Gdx.graphics.getHeight() * 0.005);
        table.add(gameDescription).pad(padAmount).align(Align.left).row();
        //table.add(comment).pad(padAmount).align(Align.left).row();
        //table.add(credit).pad(padAmount).align(Align.left).row();

        table.setX((Gdx.graphics.getWidth() - table.getWidth()) / 2);
        table.setY(board2.getY() - table.getHeight() - Gdx.graphics.getHeight() / 8);

        stage.addActor(table);
        stage.addActor(credit);

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
