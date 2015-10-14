package com.collision.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.handler.Animation;

public class Enemy {
	
	private Rectangle boundingRectangle;
	private Vector2 position;
	
	private TextureRegion[] spriteRegion;
	private Animation animation;
	
	// ----- Testing -------
		private ShapeRenderer sr;
	
	public Enemy(OrthographicCamera camera){
		
		this.boundingRectangle = new Rectangle(70, 100, 16, 15);
		this.position = new Vector2(70, 100);
		this.sr = new ShapeRenderer();
		this.animation = new Animation();
		
		Texture texture = new Texture(Gdx.files.internal("enemy.png"));
		this.spriteRegion = TextureRegion.split(texture, 16, 15)[1];
		this.animation.setAnimation(spriteRegion, 1/3f, animation);
		animation.setPlaying(true);
		
		sr.setProjectionMatrix(camera.combined);
	}

	public void update(float dt){
	
		boundingRectangle.setPosition(position);
		animation.update(dt);
		
	}
	
	public void render(SpriteBatch batch){
		batch.begin();
		batch.draw(animation.getFrame(), position.x, position.y);
		batch.end();
		
		// Testing
		sr.begin(ShapeType.Line);
		sr.rect(position.x, position.y, 16, 16);
		sr.end();
	}
	
	public Rectangle getBoundingRectangle(){
		return boundingRectangle;
	}
	
}
