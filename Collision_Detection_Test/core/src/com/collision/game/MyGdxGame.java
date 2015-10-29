package com.collision.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.collision.game.handler.GameStateManager;
import com.badlogic.gdx.audio.Music;

public class MyGdxGame extends ApplicationAdapter {
	
	public static final int WIDTH = 370;
	public static final int HEIGHT = 240;
	public static final float SCALE = 2.5f;
	public static final float STEP = 1/60f;
	
	private float accum;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private GameStateManager gsm;

	@Override
	public void create () {


		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, WIDTH * 2f, HEIGHT * 2f );
		this.gsm = new GameStateManager(this);

		//controller = Controllers.getControllers().first();
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
