package com.multiplayergame.gp.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.multiplayergame.gp.MultiplayerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		cfg.width = MultiplayerGame.WIDTH * MultiplayerGame.SCALE;
		cfg.height = MultiplayerGame.HEIGHT * MultiplayerGame.SCALE;
		
		new LwjglApplication(new MultiplayerGame(), cfg);
	}
}
