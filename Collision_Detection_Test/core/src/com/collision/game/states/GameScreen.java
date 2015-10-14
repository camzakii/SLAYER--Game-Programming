package com.collision.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.collision.game.entity.Enemy;
import com.collision.game.entity.GameLevel;
import com.collision.game.entity.Player;
import com.collision.game.entity.Player2;
import com.collision.game.handler.CustomInputProcessor;
import com.collision.game.handler.GameKeys;
import com.collision.game.handler.GameStateManager;

public class GameScreen extends GameState{

	private Player player;
	private Player2 player2;
	private Enemy enemy;
	private GameLevel level;
	
	public GameScreen(GameStateManager gsm) {
		super(gsm);
		this.level = new GameLevel(camera);
		this.player2 = new Player2(camera, level);
		this.player = new Player(camera, level);
		this.enemy = new Enemy(camera);
		
		Gdx.input.setInputProcessor(new CustomInputProcessor());
	}

	@Override
	public void update(float dt) {
		
		handleInput();
		
		player.update(dt);
		player2.update(dt);
		enemy.update(dt);
		
		if(player.getBoundingRectangle().overlaps(enemy.getBoundingRectangle())){
			player.getCollisionHandler().preventOverlap(player.getBoundingRectangle(),enemy.getBoundingRectangle());
		}
		
		if(player.getSwordRect().overlaps(player2.getBoundingRectangle())){
			System.out.println("HITT!!");
			player2.hit();
		}
		
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
	
		Gdx.gl.glClearColor(1f, 1f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		level.render();
		player.render(batch);
		player2.render(batch);
		enemy.render(batch);
	}

	@Override
	public void handleInput() {
		if(GameKeys.isPressed(GameKeys.R)){
			gsm.setState(gsm.PLAY);
		}
	}

	@Override
	public void dispose() {
		
	}
	
	
}
