package com.collision.game.entity;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.handler.Animation;

public class Powerup {

	public enum PowerupType{
		RANGE,
		SPEED
	}
	
	private static final int WIDTH = 16;
	
	private PowerupType powerup_type;
	
	private Rectangle boundingBox;
	private Animation animation;
	private TextureRegion[] powerupRegion;
	
	public Powerup(Vector2 position){
		
		this.boundingBox = new Rectangle(position.x, position.y, WIDTH, WIDTH);
		this.animation = new Animation();
		
		Texture texture = new Texture(Gdx.files.internal("player_sprites/power_up.png"));
		this.powerupRegion = TextureRegion.split(texture, 43, 20)[0];
		this.animation.setAnimation(powerupRegion, 1/3f, animation);
		
		randomPowerup();
		
		animation.setPlaying(true);
	}
	
	public void update(float dt){
		animation.setPlaying(true);
		animation.update(dt);
	}
	
	public void render(SpriteBatch batch){

		
		batch.begin();
		batch.draw(animation.getFrame(), boundingBox.x - 12, boundingBox.y - 5);
		batch.end();
		
//		sr.begin(ShapeType.Line);
//		sr.rect(boundingBox.x, boundingBox.y, WIDTH, WIDTH);
//		sr.end();
	}

	private void randomPowerup(){
		
		Random rand = new Random();
		int randomNum = rand.nextInt(2) + 1;
		
		System.out.println("RAND: " + rand);
		
		if(randomNum == 1){
			powerup_type = PowerupType.RANGE;
		}else{
			powerup_type = PowerupType.SPEED;
		}
		
	}
	
	public Rectangle getBoundingBox(){
		return boundingBox;
	}
	
	public void remove(){
		this.boundingBox.x = -500;
	}
	
	public PowerupType getType(){
		return powerup_type;
	}
	
}
