package com.collision.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Powerup {

	private static final int WIDTH = 16;
	
	private Rectangle boundingBox;
	
	public Powerup(Vector2 position){
		
		this.boundingBox = new Rectangle(position.x, position.y, WIDTH, WIDTH);
		
	}
	
	public void render(SpriteBatch batch, ShapeRenderer sr){
		
		sr.begin(ShapeType.Line);
		sr.rect(boundingBox.x, boundingBox.y, WIDTH, WIDTH);
		sr.end();
	}
	
	public Rectangle getBoundingBox(){
		return boundingBox;
	}
	
	public void remove(){
		this.boundingBox = new Rectangle();
	}
	
}
