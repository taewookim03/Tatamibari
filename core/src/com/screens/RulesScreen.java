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
import com.gameobjects.Tile;
import com.tatamibari.TatamibariGame;

/**
 * Manages the how to play (rules) screen
 */

public class RulesScreen implements Screen {
    private TatamibariGame game;
    private Stage stage;
    private OrthographicCamera camera;

    public RulesScreen(TatamibariGame game){
        this.game = game;
        camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        viewport.apply();
        camera.setToOrtho(false);
        camera.update();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        //make a small example board as illustration of the rules
        Board board = new Board(4, 4, Gdx.graphics.getHeight() * 0.3f, Gdx.graphics.getHeight() * 0.3f,
                Gdx.graphics.getHeight() * 0.001f);
        board.removeAllSymbols();
        board.setSymbol(0, 0, Tile.Symbol.SQUARE);
        board.setSymbol(0, 1, Tile.Symbol.VERTICAL);
        board.setSymbol(1, 0, Tile.Symbol.VERTICAL);
        board.setSymbol(3, 0, Tile.Symbol.SQUARE);
        board.setSymbol(1, 3, Tile.Symbol.SQUARE);
        board.setSymbol(3, 2, Tile.Symbol.HORIZONTAL);

        board.addRegion(0,0, 0,0);
        board.addRegion(1,0, 2,0);
        board.addRegion(0,1, 1,1);
        board.addRegion(3,0, 3,0);
        board.addRegion(2,1, 3,3);
        board.addRegion(0,2, 1,3);

        board.setPosition(board.getX(), Gdx.graphics.getHeight() - board.getHeight() * 1.3f);
        stage.addActor(board);

        Table table = new Table();

        //create instruction labels
        Label howToPlay1 = new Label("Divide the board into rectangles. Touch and drag to assign a partitioned",
                game.skin, "small");
        Label howToPlay2 = new Label("rectangle according to the following rules:", game.skin, "small");

        Label[] rules = new Label[]{
                new Label("    1. Each rectangle must contain exactly one symbol", game.skin, "small"),
                new Label("    2. A rectangle with a + symbol must be a square", game.skin, "small"),
                new Label("    3. A rectangle with a - symbol must have a width greater than its height", game.skin, "small"),
                new Label("    4. A rectangle with a | symbol must have a height greater than its width", game.skin, "small"),
                new Label("    5. Four rectangles may not share the same corner", game.skin, "small")
        };

        Label goal = new Label("The goal of the game is to completely fill the board with rectangles. Good luck!", game.skin, "small");

        //add instructions to table
        float padAmount = (int)(Gdx.graphics.getHeight() * 0.005);
        table.add(new Label("", game.skin)).pad((int)(Gdx.graphics.getHeight() * 0.05)).row();
        table.add(howToPlay1).pad(padAmount).align(Align.left).row();
        table.add(howToPlay2).pad(padAmount).align(Align.left).row();
        for (Label rule : rules){
            table.add(rule).pad(padAmount).align(Align.left).row();
        }

        //table.add(goal).pad(10).align(Align.left);

        table.setX((Gdx.graphics.getWidth() - table.getWidth()) / 2);
        table.setY(board.getY() - table.getHeight() - Gdx.graphics.getHeight() / 8);

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
