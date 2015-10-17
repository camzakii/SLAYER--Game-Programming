package com.collision.game.networking;

import com.collision.game.MyGdxGame;
import com.collision.game.handler.GameStateManager;
import com.collision.game.states.GameScreen;
import com.esotericsoftware.kryonet.Client;

public class GameClient {
	
	private Client client;
	private GameScreen game;
	
	private String name;
	
	public GameClient(String name){
		this.game = new GameScreen(new GameStateManager(new MyGdxGame()));
		this.name = name;
		
		this.client = new Client();
		this.client.start();
	}
}
