package com.tatamibari;

import com.badlogic.gdx.Application;
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
	public Skin skin;

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		Texture fontTexture = new Texture(Gdx.files.internal("skin/lato.png"), true);
		fontTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
		//FreeTypeFontGenerator generator;

		//Freetype is not compatible with html
		if (Gdx.app.getType().equals(Application.ApplicationType.WebGL)){
			//
		}
		//changing window size for android
		/*
		if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
			//Gdx.graphics.setDisplayMode(500, 500, false);
		} else {
			//Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), true);
		}
		*/

		//go to main menu
        setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		super.dispose();
		skin.dispose();
	}

}
