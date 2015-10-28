package com.collision.game.hud;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.collision.game.entity.Player;
import com.collision.game.handler.GameHandler;

public class GameHud {

	private OrthographicCamera camera;
	private GameHandler handler;
	private Map<Integer, Player> players;
	private Sprite healthSprite;
	
	public GameHud(OrthographicCamera camera, GameHandler handler){
		this.camera = camera;
		this.handler = handler;
		this.players = handler.getPlayers();
		
		Texture texture = new Texture(Gdx.files.internal("hud_sprites/life.png"));
		this.healthSprite = new Sprite(texture);
	}
	
	public void update(float dt, Map<Integer, Player> players){
		if(!this.players.equals(players)){
			this.players = players;
		}
	}
	
	public void render(SpriteBatch batch){
		
		if(players == null) return;
		
		batch.begin();
		
		int index = 1;
		int indexY = 1;
		int indexCount = 0;
		
		for(Player mpPlayer : players.values()){
			
			for(int i = 0; i < mpPlayer.getLifes(); i++){
				batch.draw(healthSprite, 14 * index, indexY + 14 * i);	
			}
			
			indexCount++;
			
			if(indexCount == 2) {
				indexY += 220;
				index = 1;
			}
			else if(indexCount > 2){
				index -= 62;
			}
			
			index += 31;
		}
		
		batch.end();
	}
	
	public void playerHit(int playerID){
		
	}
}
