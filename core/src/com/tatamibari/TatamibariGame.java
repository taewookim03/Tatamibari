package com.tatamibari;

/*
 * Tatamibari
 *
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gameobjects.Board;
import com.gameobjects.Tile;
import com.gameworld.GameWorld;
import com.helpers.InputHandler;
import com.screens.GameScreen;
import com.screens.MainMenuScreen;

public class TatamibariGame extends Game {

    private GameWorld world;
	//using default fonts and skins instead of loading assets for now
    public SpriteBatch batch;
    public BitmapFont font;
	public Skin skin;

	@Override
	public void create () {
	    batch = new SpriteBatch();
	    font = new BitmapFont(Gdx.files.internal("default.fnt"));
		skin = new Skin(Gdx.files.internal("uiskin.json"));

        setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		super.dispose();
        batch.dispose();
        font.dispose();
		skin.dispose();
	}

	private void showDialog() {
		Dialog dialog = new Dialog("Choose an action", skin) {

			@Override
			protected void result(Object object) {
				boolean exit = (Boolean) object;
				if (exit) {
					Gdx.app.exit();
				} else {
					remove();
				}
			}

			@Override
			public Dialog show(Stage stage) {
				return super.show(stage);
			}

			@Override
			public void cancel() {
				super.cancel();
			}

			@Override
			public float getPrefHeight() {
				return 50f;
			}
		};
		dialog.button("Yes", true);
		dialog.button("No", false);
		dialog.key(Input.Keys.ENTER, true);
		dialog.key(Input.Keys.ESCAPE, false);
		dialog.show(world);
	}
}
