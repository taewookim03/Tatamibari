package com.tatamibari;

/*
 * Tatamibari
 *
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gameobjects.Board;
import com.gameobjects.Tile;
import com.gameworld.GameWorld;
import com.helpers.InputHandler;
import com.screens.GameScreen;
import com.screens.MainMenuScreen;

public class TatamibariGame extends Game {

    private GameWorld world;
    public SpriteBatch batch;
    public BitmapFont font;

	@Override
	public void create () {
	    batch = new SpriteBatch();
	    font = new BitmapFont(Gdx.files.internal("default.fnt"));

        this.setScreen(new MainMenuScreen(this));
	}


	@Override
	public void render () {
	    super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
        batch.dispose();
        font.dispose();
	}

	public void setWorld(GameWorld world){
	    this.world = world;
    }

	public GameWorld getWorld(){
	    return world;
    }
}
