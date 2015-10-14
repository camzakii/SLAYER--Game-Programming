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
		
		this.boundingBox = new Rectangle(position.x + (direction.x * 16), position.y + 8, 10, 5);
		this.blockTimer = 50;
		this.timer = 30;
	}
	
	public void update(float dt){
		if(player != null){
			boundingBox.x = player.getPosition().x + (direction.x * 16);
			boundingBox.y = player.getPosition().y + 8;
		}
		
		if(timer > 0) timer--;
		if(blockTimer > 0) blockTimer--;
		
		if(timer <=0 ) boundingBox = new Rectangle();
	}
	
	public void render(ShapeRenderer sr){
		sr.begin(ShapeType.Line);
		sr.rect(boundingBox.x, boundingBox.y, 20, 5);
		sr.end();
	}
	
}
