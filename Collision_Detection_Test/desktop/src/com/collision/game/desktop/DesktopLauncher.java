package com.collision.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.collision.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) (MyGdxGame.WIDTH * MyGdxGame.SCALE);
		config.height = (int) (MyGdxGame.HEIGHT * MyGdxGame.SCALE);
		new LwjglApplication(new MyGdxGame(), config);
	}
}
 