package com.tatamibari;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
		//skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		//Texture fontTexture = new Texture(Gdx.files.internal("skin/lato.png"), true);
		//fontTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);

		//Use Freetype, which allows runtime generation of fonts (to save space) that can scale with screen size
		//Freetype is not compatible with html-use default font
		if (Gdx.app.getType().equals(Application.ApplicationType.WebGL)){
			skin = new Skin(Gdx.files.internal("skin/uiskin-html.json"));
		}

		else{
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Lato2OFL/Lato2OFL/Lato-Regular.ttf"));
			FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			//title size
			float screenSize = Gdx.graphics.getHeight() < Gdx.graphics.getWidth() ?
					Gdx.graphics.getHeight() : Gdx.graphics.getWidth();
			parameter.size = (int)(screenSize * 0.1);
			BitmapFont fontTitle = generator.generateFont(parameter);

			//normal size
			parameter.size = (int)(screenSize * 0.05);
			BitmapFont font = generator.generateFont(parameter);

			//small size
			parameter.size = (int)(screenSize * 0.03);
			BitmapFont fontSmall = generator.generateFont(parameter);

			generator.dispose();

			skin = new Skin();
			skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
			skin.add("font-title", fontTitle);
			skin.add("font", font);
			skin.add("font-small", fontSmall);
			skin.load(Gdx.files.internal("skin/uiskin.json"));
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
