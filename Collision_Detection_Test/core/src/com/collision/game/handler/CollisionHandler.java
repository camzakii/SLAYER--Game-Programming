package com.collision.game.handler;

import java.util.Map;

import com.collision.game.entity.Player;
import com.collision.game.entity.Player.PlayerState;

public class CollisionHandler {

	private GameHandler handler;
	
	public CollisionHandler(){
		
	}
	
	public void playerCollision(Player player, Map<Integer, Player> players){
		
		for(Player mpPlayer : players.values()){
			if(player.getSwordRect().overlaps(mpPlayer.getBoundingRectangle())){
				if(mpPlayer.getState() != PlayerState.BLOCKING){
					System.out.println("ENEMY HIT!");
					mpPlayer.setDead();
				}
			}
		}
	}
	
}
