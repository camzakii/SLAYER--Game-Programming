package com.multiplayergame.gp.abilities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Gun {

	private Body body;
	private Vector2 direction;
	
	public Gun(){
		this.direction = new Vector2();
	}
	
	public void update(float dt){
		
	}
	
	public void render(SpriteBatch batch){
		
		batch.begin();
		
		batch.end();
		
	}
	
	public void shoot(){
		
	}
	
}
