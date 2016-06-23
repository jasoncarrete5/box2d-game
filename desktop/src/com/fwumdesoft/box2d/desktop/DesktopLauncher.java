package com.fwumdesoft.box2d.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fwumdesoft.box2d.App;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "box2d-game";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new App(), config);
	}
}
