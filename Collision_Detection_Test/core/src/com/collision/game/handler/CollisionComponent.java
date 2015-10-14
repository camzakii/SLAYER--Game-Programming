package com.collision.game.handler;

import com.badlogic.gdx.math.Rectangle;
import com.collision.game.entity.Player;

public class CollisionComponent {
	
	private Player player;
	
	public CollisionComponent(Player player){
		this.player = player;
	}
	
	public void update(float dt){
		
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
