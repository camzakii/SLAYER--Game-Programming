package com.multiplayergame.gp.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.multiplayergame.gp.MultiplayerGame;
import com.multiplayergame.gp.handlers.GameStateManager;

public abstract class GameState {

	protected GameStateManager gsm;
	protected MultiplayerGame game;
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
	
	protected GameState(GameStateManager gsm){
		this.gsm = gsm;
		this.game = gsm.getGame();
		this.batch = game.getBatch();
		this.camera = game.getCamera();
	}
	
	public abstract void update(float dt);
	public abstract void render();
	public abstract void handleInput();
	public abstract void dispose();
}
