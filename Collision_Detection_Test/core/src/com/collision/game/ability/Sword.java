package com.collision.game.ability;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.entity.Player;

public class Sword {

	private Rectangle boundingBox;
	private Player player;
	private Vector2 direction;
	
	private boolean powerup;
	private int timer;
	private double removeTimer;
	private int swordOnHit;
	private int attackInterval;
	
	public Sword(){
		
		this.boundingBox = new Rectangle(-100, 00, 16, 10);
		this.powerup = false;
	}
	
	public void action(Vector2 position, Vector2 direction, Player player){
		
		this.direction = direction;
		this.player = player;
		
		if(attackInterval > 0) return;
		
		if(!powerup) {
//			this.boundingBox = new Rectangle(position.x +  (direction.x * 16), position.y + 8, 20, 10);
			if(direction.x > 0) {
				this.boundingBox = new Rectangle(position.x + 16, position.y, 16, 10);
			}
			else {
				this.boundingBox = new Rectangle(position.x - 20, position.y, 16, 10);
			}
			
		}
		else{
			if(direction.x > 0){
				this.boundingBox = new Rectangle(position.x + 16, position.y, 30, 10);
			}else{
				this.boundingBox = new Rectangle(position.x - 30, position.y, 30, 10);
			}
		}
		
	
		this.attackInterval = 40;
		this.timer = 20;
	}
	
	public void update(float dt){
		
		if(powerup){
			removeTimer -= dt;
			if(removeTimer <= 0) powerup = false;
		}
		
		if(swordOnHit > 0) swordOnHit--;
		
		if(player != null){
			if(!powerup){
				if(direction.x > 0){
					boundingBox.x = player.getPosition().x + 16;
				}else{
					boundingBox.x = player.getPosition().x - 16;
				}
			}else{
				if(direction.x > 0){
					boundingBox.x = player.getPosition().x + 16;
				}else{
					boundingBox.x = player.getPosition().x - 30;
				}
			}
			
			boundingBox.y = player.getPosition().y + 5;
		}
		
		if(timer > 0) timer--;
		if(attackInterval > 0) attackInterval--;

		if(timer <= 0) boundingBox = new Rectangle();
	}
	
	public void render(ShapeRenderer sr){
		
		sr.begin(ShapeType.Line);
		if(!powerup) sr.rect(boundingBox.x, boundingBox.y, 16, 10);
		else sr.rect(boundingBox.x, boundingBox.y, 30, 10);
		sr.end();
	}
	
	public int getTimer(){
		return this.timer;
	}
	
	public void setTimer(int timer){
		this.timer = timer;
	}
	
	public int getInterval(){
		return attackInterval;
	}
	
	public Rectangle getBoundingBox(){
		return boundingBox;
	}
	
	public void setPowerup(boolean bool){
		this.powerup = bool;
		this.removeTimer = 10;
	}
	
	public boolean getPowerup(){
		return powerup;
	}
	
	public void setSwordOnHit(int swordOnHit){
		this.swordOnHit = swordOnHit;
	}
	
	public int getSwordOnHit(){
		return swordOnHit;
	}
}
