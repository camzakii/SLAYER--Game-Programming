package com.collision.game.handler;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.collision.game.ability.Shuriken;
import com.collision.game.entity.GameLevel;
import com.collision.game.entity.Player;
import com.collision.game.entity.Player2;
import com.collision.game.entity.PlayerEntity.PlayerState;
import com.collision.game.entity.Powerup;
import com.collision.game.states.GameScreen;

public class CollisionComponent {
	
	private Player player;
	private Player2 player2;
	private Powerup powerup;
	private GameScreen game;
	private GameLevel level;
	
	public CollisionComponent(GameScreen game){
		this.game = game;
		this.powerup = game.getPowerup();
		this.player = game.getPlayer();
		this.player2 = game.getPlayer2();
		this.level = game.getLevel();
	}
	
	public void update(float dt){
		
		playerCollisions();
	}
	
	private void playerCollisions(){
		if(player.getSwordRect().overlaps(player2.getBoundingRectangle())){
			if(player2.getState() == PlayerState.BLOCKING) return;
			
			player2.hit();
		}
		
		if(player2.getSwordRect().overlaps(player.getBoundingRectangle())){
			if(player.getState() == PlayerState.BLOCKING) return;
			
			System.out.println("Player 1 hit");
			player.hit();
		}
		
		// Player 2 Shurikens hit player 1
		Array<Shuriken> shurikens = player2.getShurikens();
		for(Shuriken shuriken: shurikens){
			if(shuriken.getBoundingBox().overlaps(player.getBoundingRectangle())){
				if(player.getState() == PlayerState.BLOCKING) return;
				
				shuriken.setDead();
				player.hit();
			}
			if(shuriken.getBoundingBox().overlaps(player2.getBoundingRectangle())){
				if(player2.getState() == PlayerState.BLOCKING) return;
				
				shuriken.setDead();
				player2.hit();
			}
		}
		
		// Player 1 shurikens hit player 2
		Array<Shuriken> player1_shurikens = player.getShurikens();
		for(Shuriken shuriken: player1_shurikens){
			if(shuriken.getBoundingBox().overlaps(player2.getBoundingRectangle())){
				if(player2.getState() == PlayerState.BLOCKING) return;
				
				shuriken.setDead();
				player2.hit();
			}
			if(shuriken.getBoundingBox().overlaps(player.getBoundingRectangle())){
				if(player.getState() == PlayerState.BLOCKING) return;
				
				shuriken.setDead();
				player.hit();
			}
		}
		
		if(player.getBoundingRectangle().overlaps(powerup.getBoundingBox())){
			player.getSword().setPowerup(true);
			powerup.remove();
		}
		
		if(player2.getBoundingRectangle().overlaps(powerup.getBoundingBox())){
			player2.getSword().setPowerup(true);
			powerup.remove();
		}
		
	}
	
	public void preventOverlap(Rectangle playerRectangle, Rectangle otherRectangle){
		
		float distY = Math.abs((playerRectangle.y + playerRectangle.height / 2) - (otherRectangle.y + otherRectangle.height / 2));
		float totalHeight = playerRectangle.height / 2 + otherRectangle.height / 2;
		float overlapY = totalHeight - distY;
		
		float distX = Math.abs((playerRectangle.x + playerRectangle.width / 2) - (otherRectangle.x + otherRectangle.width / 2));
		float totalWidth = (playerRectangle.width / 2 + otherRectangle.width / 2);
		float overlapX = totalWidth - distX;
	
		if(overlapX < overlapY){
			
			float x = player.getPosition().x;
			
			if(playerRectangle.x + playerRectangle.width / 2 < otherRectangle.x){
				x -= overlapX + 1;
				player.setX(x);
				player.setVelocityX(-0.3f);
			} else {
				x += overlapX + 1;
				player.setX(x);
				player.setVelocityX(-0.3f);
			}
		} else {
			
			float y = player.getPosition().y;
			
			if (playerRectangle.y + playerRectangle.height / 2 < otherRectangle.y + otherRectangle.height / 2) {
				y -= overlapY+1;
				player.setY(y);
				player.setVelocityY(-0.3f);
			} else {
				y += overlapY+1;
				player.setY(y);
				player.setVelocityY(-0.3f);
			}
		}
		
	}
	
}
