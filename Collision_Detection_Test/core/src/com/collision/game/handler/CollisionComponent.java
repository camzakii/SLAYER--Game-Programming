package com.collision.game.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.collision.game.ability.Shuriken;
import com.collision.game.entity.GameLevel;
import com.collision.game.entity.Player;
import com.collision.game.entity.Player2;
import com.collision.game.entity.PlayerEntity.PlayerState;
import com.collision.game.entity.Powerup;
import com.collision.game.states.GameScreen;
import com.collision.game.utils.ParticleEngine;

public class CollisionComponent {
	
	private Player player;
	private Player2 player2;
	private Powerup powerup;
	private GameScreen game;
	private GameLevel level;
	private ParticleEngine particleEngine;
	
	public CollisionComponent(GameScreen game){
		this.game = game;
		this.powerup = game.getPowerup();
		this.player = game.getPlayer();
		this.player2 = game.getPlayer2();
		this.level = game.getLevel();
		this.particleEngine = new ParticleEngine();
	}
	
	public void update(float dt){
		
		playerCollisions();
		particleEngine.update();
	}
	
	public void render(SpriteBatch batch){
		particleEngine.render(batch);
	}
	
	private void playerCollisions(){
		if(player.getSwordRect().overlaps(player2.getBoundingRectangle())){
			if(player2.getState() == PlayerState.BLOCKING) return;
			
			player2.hit();
			particleEngine.createParticles(player2.getPosition().x - 10, player2.getPosition().y);
		}
		
		if(player2.getSwordRect().overlaps(player.getBoundingRectangle())){
			if(player.getState() == PlayerState.BLOCKING) return;
			
			player.hit();
			particleEngine.createParticles(player.getPosition().x - 10, player.getPosition().y);
		}
		
		// Player 2 Shurikens hit player 1
		Array<Shuriken> shurikens = player2.getShurikens();
		for(Shuriken shuriken: shurikens){
			if(shuriken.getBoundingBox().overlaps(player.getBoundingRectangle())){
				if(player.getState() == PlayerState.BLOCKING) return;
				
				shuriken.setDead();
				shurikens.removeValue(shuriken, true);
				player.hit();
			}
			if(shuriken.getBoundingBox().overlaps(player2.getBoundingRectangle())){
				if(player2.getState() == PlayerState.BLOCKING) return;
				
				shuriken.setDead();
				shurikens.removeValue(shuriken, true);
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
				player1_shurikens.removeValue(shuriken, true);
			}
			if(shuriken.getBoundingBox().overlaps(player.getBoundingRectangle())){
				if(player.getState() == PlayerState.BLOCKING) return;
				
				shuriken.setDead();
				player.hit();
				player1_shurikens.removeValue(shuriken, true);
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
