package com.collision.game.ability;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.entity.GameLevel;
import com.collision.game.entity.PlayerEntity;
import com.collision.game.handler.Animation;

public class Shuriken {

	private static final int VELOCITY = 10;
	
	private Animation animation;
	private TextureRegion[] shurikenRegion;
	
	private Vector2 direction;
	private Vector2 position;
	private boolean alive;
	
	private Rectangle boundingBox;
	
	private TiledMapTileLayer layer;
	private GameLevel level;
	
	public Shuriken(GameLevel level, TiledMapTileLayer layer){
		this.boundingBox = new Rectangle();
		this.animation = new Animation();
		this.alive = false;
		
		this.layer = layer;
		this.level = level;
	}
	
	public void action(Vector2 direction, Vector2 position){
		this.direction = direction;
		this.position = position;
		this.alive = true;
		
		if(direction.x > 0) boundingBox = new Rectangle(this.position.x, this.position.y + 8, 5, 5);
		else boundingBox = new Rectangle(this.position.x, this.position.y + 8, 5, 5);
	}
	
	
	public void update(float dt){
		
		if(!alive) return;
		
		if(alive) {
			boundingBox.x += VELOCITY * direction.x;
		}
		
		mapCollision();
		
		mapWarping();
	}
	
	private void mapWarping(){
		if(boundingBox.x > 400) boundingBox.x = 0;
		if(boundingBox.x < 0) boundingBox.x = 400;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer sr){
		if(!alive) return;
		
		sr.begin(ShapeType.Line);
		sr.rect(boundingBox.x, boundingBox.y, 5, 5);
		sr.end();
	}

	private void mapCollision(){
		if(direction.x > 0) {
			if(collisionRight()) alive = false;
		}
		else{
			if(collisionLeft()) alive = false;
		}
	}
	

	private boolean collisionLeft(){
		 for(float step = 0; step < 5; step += layer.getTileHeight() / 2)
            if(level.isCellBlocked(boundingBox.x, boundingBox.y + step)){
                    return true;
            }
		 return false;
	}
	
	private boolean collisionRight(){
		for(float step = 0; step < 5; step += layer.getTileHeight() / 2 ){
			if(level.isCellBlocked(boundingBox.x + 5, boundingBox.y + step)){
				return true;
			}
		}
		return false;
	}
	
	
	public Rectangle getBoundingBox(){
		return boundingBox;
	}
}
