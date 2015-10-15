package com.multiplayergame.gp.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entity {
	
	public Entity(){
		
	}
	
	public abstract void update(float dt);
	public abstract void render(SpriteBatch batch);
	public abstract boolean isAlive();
	public abstract Body getBody();
	
}
