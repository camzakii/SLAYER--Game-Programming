package com.collision.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.collision.game.handler.GameStateManager;

public class MyGdxGame extends ApplicationAdapter {
	
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	public static final float STEP = 1/60f;
	
	private float accum;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private GameStateManager gsm;
	
	@Override
	public void create () {

		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, WIDTH * 1.3f, HEIGHT * 1.3f );
		this.gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while(accum >= STEP){
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
		}
	}
	
	public SpriteBatch getBatch(){
		return this.batch;
	}
	
	public OrthographicCamera getCamera(){
		return this.camera;
	}
	
}
