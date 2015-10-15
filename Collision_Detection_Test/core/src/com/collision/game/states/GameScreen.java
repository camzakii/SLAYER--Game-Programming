package com.collision.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.entity.Enemy;
import com.collision.game.entity.GameLevel;
import com.collision.game.entity.Player;
import com.collision.game.entity.Player2;
import com.collision.game.entity.PlayerEntity;
import com.collision.game.entity.Powerup;
import com.collision.game.handler.CollisionComponent;
import com.collision.game.handler.CustomInputProcessor;
import com.collision.game.handler.GameKeys;
import com.collision.game.handler.GameStateManager;

public class GameScreen extends GameState{

	private Player player;
	private Player2 player2;
	private Enemy enemy;
	private GameLevel level;
	private Powerup powerup;
	private CollisionComponent collision;
	
	// Testing
	private ShapeRenderer sr;
	
	public GameScreen(GameStateManager gsm) {
		super(gsm);
		this.level = new GameLevel(camera);
		this.player2 = new Player2(camera, level);
		this.player = new Player(camera, level);
		this.enemy = new Enemy(camera);
		this.powerup = new Powerup(new Vector2(300, 200));
		
		this.collision = new CollisionComponent(this);
		
		// Testing
		this.sr = new ShapeRenderer();
		
		Gdx.input.setInputProcessor(new CustomInputProcessor());
	}

	@Override
	public void update(float dt) {
		
		handleInput();
		
		collision.update(dt);
		
		player.update(dt);
		player2.update(dt);
//		enemy.update(dt);
		
		sr.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
	
		Gdx.gl.glClearColor(1f, 0.5f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		level.render();
		powerup.render(batch, sr);
		player.render(batch);
		player2.render(batch);
//		enemy.render(batch);
	}

	@Override
	public void handleInput() {
		
		// Restart Game
		if(GameKeys.isPressed(GameKeys.R)){
			gsm.setState(gsm.PLAY);
		}
		
		// Player 1 Controls
		
		if(GameKeys.isPressed(GameKeys.W)){
			player.jump();
		}
		if(GameKeys.isDown(GameKeys.A)){
			player.moveLeft();
		}
		if(GameKeys.isDown(GameKeys.D)){
			player.moveRight();
		}
		if(GameKeys.isPressed(GameKeys.S)){
			player.swordAction();
		}
		if(GameKeys.isPressed(GameKeys.E)){
			player.parryAction();
		}
		if(GameKeys.isUp(GameKeys.A) && GameKeys.isUp(GameKeys.D)){
			player.setVelocityX(0);
		}
		if(GameKeys.isPressed(GameKeys.V)){
			player.shurikenAction();
		}
		
		// Player 2 Controls
		
		if(GameKeys.isPressed(GameKeys.U)){
			player2.jump();
		}
		if(GameKeys.isDown(GameKeys.H)){
			player2.moveLeft();
		}
		if(GameKeys.isDown(GameKeys.K)){
			player2.moveRight();
		}
		if(GameKeys.isPressed(GameKeys.J)){
			player2.swordAction();
		}
		if(GameKeys.isPressed(GameKeys.O)){
			player2.parryAction();
		}
		if(GameKeys.isPressed(GameKeys.N)){
			player2.shurikenAction();
		}
		if(GameKeys.isUp(GameKeys.H) && GameKeys.isUp(GameKeys.K)){
			player2.setVelocityX(0);
		}
	
	}

	@Override
	public void dispose() {
		
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Player2 getPlayer2(){
		return player2;
	}
	
	public Powerup getPowerup(){
		return powerup;
	}
	
	
}
