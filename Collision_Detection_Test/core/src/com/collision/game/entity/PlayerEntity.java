package com.collision.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class PlayerEntity {

	public enum PlayerState{
		IDLE,
		ATTACKING,
		BLOCKING,
		MOVING
	}

	
	public abstract void update(float dt);
	public abstract void render(SpriteBatch batch);
	public abstract Vector2 getPosition();
}
