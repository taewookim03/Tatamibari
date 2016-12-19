package com.tatamibari;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.screens.MainMenuScreen;

public class TatamibariGame extends Game {
	public Skin skin;

	@Override
	public void create () {
		//Use Freetype, which allows runtime generation of fonts (to save space) that can scale with screen size
		//Freetype is not compatible with html-use default font
		if (Gdx.app.getType().equals(Application.ApplicationType.WebGL)){
			BitmapFont fontTitle = new BitmapFont(Gdx.files.internal("lato60.fnt"));
			BitmapFont font = new BitmapFont(Gdx.files.internal("lato24.fnt"));
			BitmapFont fontSmall = new BitmapFont(Gdx.files.internal("lato18.fnt"));
			skin = new Skin();
			skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin-html.atlas")));
			skin.add("font-title", fontTitle);
			skin.add("font", font);
			skin.add("font-small", fontSmall);
			skin.load(Gdx.files.internal("skin/uiskin-html.json"));
		}

		else{//for non-html implementations, use Freetype to generate dynamic fonts
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Lato2OFL/Lato2OFL/Lato-Regular.ttf"));
			FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			//title size
			float screenSize = Gdx.graphics.getHeight() < Gdx.graphics.getWidth() ?
					Gdx.graphics.getHeight() : Gdx.graphics.getWidth();
			parameter.size = (int)(screenSize * 0.1);
			BitmapFont fontTitle = generator.generateFont(parameter);

			//normal size
			parameter.size = (int)(screenSize * 0.04);
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
		
		//go to main menu
        setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		super.dispose();
		skin.dispose();
	}

}
