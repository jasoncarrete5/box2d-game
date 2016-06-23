package com.fwumdesoft.box2d;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class App extends Game {
	public static class Assets {
		
	}
	
	public static App game;
	public static Skin uiskin;
	public static AssetManager asset;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);
		Box2D.init();
		
		game = this;
		uiskin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		asset = new AssetManager();
		
		setScreen(new MainMenuScreen());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		asset.dispose();
		uiskin.dispose();
		Gdx.app.log("App", "Game Disposed");
	}
}
