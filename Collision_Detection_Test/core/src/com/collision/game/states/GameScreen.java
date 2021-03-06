package com.collision.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.collision.game.handler.GameHandler;
import com.collision.game.handler.GameStateManager;
import com.collision.game.handler.XBox360Pad;
import com.collision.game.networking.GameClient;
import com.collision.game.networking.GameServer;

public class GameScreen extends GameState{

	private GameHandler gameHandler;
	
	// Networking
	private GameClient client;
	private GameServer server;
	private String ip;
	private boolean isHost;
	
	private Controller controller;
	private boolean hasControllers;
	
	private String name;
	
	// Testing
	private ShapeRenderer sr;
	
	public GameScreen(GameStateManager gsm, boolean isHost, String ip, String name, int spriteIndex) {
		super(gsm);

		this.isHost = isHost;
		
//		this.ip = "localhost";
		
		if(!ip.isEmpty()) this.ip = ip;
		else this.ip = "localhost";
		
		this.name = name;
		
		if(Controllers.getControllers().size == 0) hasControllers = false;
        else controller = Controllers.getControllers().first();
		
		client = new GameClient(name, spriteIndex, controller);
		gameHandler = client.getHandler();
		
		if(isHost){
			server = new GameServer();
			client.connectLocal();
			System.out.println("Server starting");
		}else{
			client.connect(this.ip);
			System.out.println("Client starting");
		}
		
		sr = new ShapeRenderer();
		
	}

	@Override
	public void update(float dt) {
		
		handleInput();
		gameHandler.update(dt);
		if(isHost) server.update(dt);
		
		sr.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
	
		Gdx.gl.glClearColor(1f, 0.5f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gameHandler.render();
		
	}

	@Override
	public void handleInput() {
		
		if(!hasControllers){
			if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
				client.stop();
				
				if(server != null){
					server.stop();
				}
				
				gsm.setState(gsm.LOBBY, false, "", "", 0);
			}
			if(Gdx.input.isKeyPressed(Keys.G)){
				client.stop();
	
				if(server != null){
					server.stop();
				}
	
				gsm.setState(gsm.MENU, false, "", "", 0);
			}
		}else{
			 if(controller.getButton(XBox360Pad.BUTTON_BACK)){
				 client.stop();
					
					if(server != null){
						server.stop();
					}
		
					gsm.setState(gsm.MENU, false, "", "", 0);
			 }
			 if(controller.getButton(XBox360Pad.BUTTON_START)){
				 client.stop();
					
					if(server != null){
						server.stop();
					}
					
					gsm.setState(gsm.LOBBY, false, "", "", 0);
			 }
		}
	}

	@Override
	public void dispose() {
		
	}
}
