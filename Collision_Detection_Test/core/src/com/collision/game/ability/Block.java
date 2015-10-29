package com.collision.game.ability;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.entity.Player;

public class Block {

	private Rectangle boundingBox;
	private Player player;
	private Vector2 direction;
	
	private int timer;
	private int blockTimer;
	
	public Block(){
		this.boundingBox = new Rectangle();
	}
	
	public void action(Vector2 position, Vector2 direction, Player player){
		this.direction = direction;
		this.player = player;
		
		if(blockTimer > 0) return;
		
		if(direction.x > 0) boundingBox = new Rectangle(position.x + (direction.x * 16), position.y, 5, 15);
		else boundingBox = new Rectangle(position.x - 5, position.y, 5, 15);
		
		this.blockTimer = 60;
		this.timer = 35;
	}
	
	public void update(float dt){
		if(player != null){
			
			if(direction.x > 0) boundingBox.x = player.getPosition().x + (direction.x * 16);
			else boundingBox.x = player.getPosition().x - 5;
			
			boundingBox.y = player.getPosition().y;
		}
		
		if(timer > 0) timer--;
		if(blockTimer > 0) blockTimer--;
		
		if(timer <= 0 ) boundingBox = new Rectangle();
	}
	
	public void render(ShapeRenderer sr){
//		sr.begin(ShapeType.Line);
//		sr.rect(boundingBox.x,s boundingBox.y, 5, 15);
//		sr.end();
	}
	
	public int getTimer(){
		return timer;
	}
	
	public void setTimer(int timer){
		this.timer = timer;
	}
	
	public int getBlockTimer(){
		return blockTimer;
	}
	
	public Rectangle getBoundingBox(){
		return this.boundingBox;
	}
}
