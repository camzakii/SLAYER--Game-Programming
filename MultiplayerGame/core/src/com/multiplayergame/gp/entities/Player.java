package com.multiplayergame.gp.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.multiplayergame.gp.abilities.Block;
import com.multiplayergame.gp.abilities.Combo;
import com.multiplayergame.gp.abilities.Sword;
import com.multiplayergame.gp.handlers.Animation;
import com.multiplayergame.gp.handlers.PlayerController;

public class Player extends Entity{
	
	private World world; 
	private Body body;
	private Sword sword;
	private Block block;
	private PlayerController playerController;
	
	private Combo combo;
	private int lives;
	
	public Player(Body body, World world) {
		this.body = body;
		this.body.setUserData(this);
		this.world = world;
		this.block = new Block(world, this);
		this.sword = new Sword(world, this);
		this.combo = new Combo();
		this.playerController = new PlayerController(this, this.body, sword, block);
	
		this.lives = 3;
	}

	public void update(float dt) {
		
		playerController.update(dt);
		combo.update(dt);

	}

	public void render(SpriteBatch batch) {
				
		playerController.render(batch);
	
	}
	
	public void handleInput(){
		
		playerController.input();

	}

	public Vector2 getPosition(){
		return this.body.getPosition();
	}
	
	public Vector2 getSwordPosition(){
		return sword.getPosition();
	}
	
	public Vector2 getDirection(){
		return playerController.getDirection();
	}

	@Override
	public boolean isAlive() {
		return lives > 0;
	}
	
	public boolean isMoving(){
		return playerController.isMoving();
	}
	
	public void removeLife(){
		lives--;
	}

	public int getLifes(){
		return lives;
	}
	
	public Body getBody(){
		return body;
	}
	
	public Combo getCombo(){
		return combo;
	}
}
