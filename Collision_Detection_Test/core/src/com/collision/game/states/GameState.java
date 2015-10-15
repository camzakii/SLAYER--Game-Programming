package com.collision.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.collision.game.MyGdxGame;
import com.collision.game.handler.GameStateManager;


public abstract class GameState {

	protected GameStateManager gsm;
	protected MyGdxGame game;
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
