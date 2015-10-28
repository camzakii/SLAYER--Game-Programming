package com.collision.game.networking;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.collision.game.handler.GameHandler;
import com.collision.game.networking.Network.*;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameClient {
	
	private Client client;
	private GameHandler game;
	public int id;
	public String remoteIP;
	private String name;
	
	
	public GameClient(String name){
		
		this.name = name;
		this.client = new Client();
		
		Network.register(client);
		
		Listener listener = new Listener(){	
			public void connected(Connection c){
				handleConnected(c);
			}
			
			public void received(Connection c, Object o) {
				handleMessage(c.getID(), o);
			}
			
			public void disconnected(Connection c){
				handleDisconnect(c);
			}
		};
		
		client.addListener(new Listener.QueuedListener(listener) {
			@Override
			protected void queue(Runnable runnable) {
				Gdx.app.postRunnable(runnable);
			}
		});
		
		this.game = new GameHandler(this);
		this.client.start();
	}
	
	protected void handleConnected(Connection c){
		this.id = c.getID();
		remoteIP = c.getRemoteAddressTCP().toString();
		Login reg = new Login(name, Network.VERSION);
		client.sendTCP(reg);
		client.updateReturnTripTime();
		game.connect(name);
	}
	
	protected void handleDisconnect(Connection c){
		game.onDisconnect();
	}
	
	public void handleMessage(int playerId, Object msg){
		
		if(msg instanceof LeaveJoin){
			LeaveJoin message = (LeaveJoin) msg;
			if(message.hasJoined){
				game.addPlayer(message);
			}else{
				System.out.println("Remove player disconnected!!!");
				game.removePlayer(message);
			}
		} else if(msg instanceof PlayerMovement){
			PlayerMovement message = (PlayerMovement) msg;
			game.playerMoved(message);
		} else if(msg instanceof PlayerAttack){
			PlayerAttack message = (PlayerAttack) msg;
			game.playerAttack(message);
		} else if(msg instanceof PlayerHit){
			PlayerHit message = (PlayerHit) msg;
			game.playerHit(message);
		} else if(msg instanceof PlayerShoot){
			PlayerShoot message = (PlayerShoot) msg;
			game.addShuriken(message);
		} else if(msg instanceof PlayerPowerup){
			PlayerPowerup message = (PlayerPowerup) msg;
			game.playerPowerup(message);
		} else if(msg instanceof PowerupData){
			PowerupData message = (PowerupData) msg;
			game.addPowerup(message);
		} 
	}
	
	public void connectLocal() {
		connect("localhost");
	}
	
	public void connect(String host){
		try {
			client.connect(5000, host, Network.PORT, Network.UDP);
			System.out.println("Client connected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Object message) {
		if (client.isConnected()) {
			client.sendTCP(message);
		}
	}
	
	public void sendMessageUDP(Object message) {
		if (client.isConnected()) {
			client.sendUDP(message);
		}
	}
	
	public void stop(){
		client.stop();
		client.close();
	}
	
	public GameHandler getHandler(){
		return this.game;
	}
	
}
