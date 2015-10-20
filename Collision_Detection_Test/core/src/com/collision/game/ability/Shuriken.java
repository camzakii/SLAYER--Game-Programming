package com.collision.game.ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

	private static final int VELOCITY = 5;
	
	private Animation rightAnimation;
	private Animation leftAnimation;
	private Animation currentAnimation;
	
	private TextureRegion[] shurikenRightRegion;
	private TextureRegion[] shurikenLeftRegion;
	
	private Vector2 direction;
	private Vector2 position;
	private boolean alive;
	private int timer;
	
	private Rectangle boundingBox;
	
	private TiledMapTileLayer layer;
	private GameLevel level;
	
	public Shuriken(GameLevel level, TiledMapTileLayer layer, Vector2 direction, Vector2 position){
		this.boundingBox = new Rectangle();
		this.rightAnimation = new Animation();
		this.leftAnimation = new Animation();
		this.currentAnimation = new Animation();
		
		this.direction = direction;
		this.position = position;
		this.alive = true;
		this.timer = 120;
		
		Texture texture = new Texture(Gdx.files.internal("player_sprites/shuriken_throw.png"));
		this.shurikenRightRegion = TextureRegion.split(texture, 43, 20)[0];
		this.rightAnimation.setAnimation(shurikenRightRegion, 1/3f, rightAnimation);
		
		this.shurikenLeftRegion = TextureRegion.split(texture, 43, 20)[1];
		this.leftAnimation.setAnimation(shurikenLeftRegion, 1/3f, leftAnimation);
		
		this.layer = layer;
		this.level = level;
		
		init();
	}
	
	private void init(){
		
		if(direction.x > 0) {
			boundingBox = new Rectangle(this.position.x + 17, this.position.y + 8, 8, 8);
			currentAnimation = rightAnimation;
		}
		else {
			boundingBox = new Rectangle(this.position.x - 9, this.position.y + 8, 8, 8);
			currentAnimation = leftAnimation;
		}
		
		currentAnimation.setPlaying(true);
		
	}
	
	
	public void update(float dt){
		
		if(!alive) return;
		
		if(alive) boundingBox.x += VELOCITY * direction.x;
		
		currentAnimation.setPlaying(true);
		currentAnimation.update(dt);
		
		timer--;
		if(timer <= 0) setDead();
		
		mapCollision();
		mapWarping();
	}
	
	private void mapWarping(){
		if(boundingBox.x > 400) boundingBox.x = 0;
		if(boundingBox.x < 0) boundingBox.x = 400;
	}
	
	public void render(SpriteBatch batch){
		if(!alive) return;
		
		batch.begin();
		batch.draw(currentAnimation.getFrame() , boundingBox.x - 13,
					boundingBox.y - 4);
		batch.end();
		
//		sr.begin(ShapeType.Line);
//		sr.rect(boundingBox.x, boundingBox.y, 8, 8);
//		sr.end();
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
	
	public void setDead(){
		alive = false;
		boundingBox = new Rectangle(-100, -100, 0, 0);
	}
}
