package com.collision.game.networking;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.entity.Player;
import com.collision.game.handler.GameHandler;
import com.collision.game.networking.Network.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {

	private Server server;
	private GameHandler handler;
	
	public GameServer(){
		
		this.server = new Server(){
			protected Connection newConnection(){
				return new GameConnection();
			}
		};
	
		this.handler = new GameHandler(this);
		Network.register(server);
	
		Listener listener = new Listener(){	
			public void received(Connection c, Object message){
				handleRecieved(c, message);
			}
			public void disconnected(Connection c){
				handleDisconnected(c);
			}
		};
		
		server.addListener(new Listener.QueuedListener(listener) {
			@Override
			protected void queue(Runnable runnable) {
				Gdx.app.postRunnable(runnable);
			}
		});
	
		try {
			server.start();
			server.bind(Network.PORT, Network.UDP);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void handleRecieved(Connection c, Object message){
		GameConnection connection = (GameConnection) c;
		
		if(message instanceof Login){
			Login msg = ((Login) message);
			
			if(connection.name != null){
				
				return;
			}
			
			String name = msg.name;
			if(name == null || name.length() == 0) return;
			
			connection.name = name;
			if(handler.getPlayers().size() == 4){
				System.out.println("Max 4 players");
				return;
			}
			
			if(msg.version != Network.VERSION){
				System.out.println("wrong version");
				connection.close();
			}else{
				
				LeaveJoin reply = new LeaveJoin(connection.getID(), connection.name, true, new Vector2(200, 100), msg.spriteIndex);
				server.sendToAllExceptTCP(connection.getID(), reply);
				
				handler.addPlayer(reply);
			
				for(Connection con: server.getConnections()){
					GameConnection conn = (GameConnection)con;
					if(conn.getID() != connection.getID() && conn.name != null){

						Player herePlayer = handler.getPlayerById(conn.getID());
						LeaveJoin hereMsg  = new LeaveJoin(conn.getID(), herePlayer.getName(), true, herePlayer.getPosition(), herePlayer.getSpriteIndex());
						connection.sendTCP(hereMsg); // basic info
						connection.sendTCP(herePlayer.getPlayerMovement()); 
					}
				}
			}
		}
		else if(message instanceof PlayerMovement){
			PlayerMovement msg = (PlayerMovement)message;
			msg.playerId = connection.getID();
			handler.playerMoved(msg);
			server.sendToAllExceptUDP(connection.getID(), msg);
		} else if(message instanceof PlayerAttack){
			PlayerAttack msg = (PlayerAttack) message;
			msg.playerId = connection.getID();
			handler.playerAttack(msg);
			server.sendToAllExceptTCP(connection.getID(), msg);
		} else if(message instanceof PlayerHit){
			PlayerHit msg = (PlayerHit) message;
			handler.playerHit(msg);
			server.sendToAllExceptTCP(connection.getID(), msg);
		} else if(message instanceof PlayerShoot){
			PlayerShoot msg = (PlayerShoot)message;
			msg.playerId = connection.getID();
			server.sendToAllExceptTCP(connection.getID(), msg);
		} else if(message instanceof PlayerPowerup){
			PlayerPowerup msg = (PlayerPowerup) message;
			msg.playerId = connection.getID();
			server.sendToAllExceptTCP(connection.getID(), msg);
		}
	}
	
	public void handleDisconnected(Connection c){
		GameConnection connection = (GameConnection) c;
		
		if(connection.name != null){
			LeaveJoin msg = new LeaveJoin(connection.getID(), connection.name, false, null, 0);
			server.sendToAllExceptTCP(connection.getID(), msg);
			handler.removePlayer(msg);
		}
	}
	
	public void update(float dt){
		handler.update(dt);
	}
	
	public static class GameConnection extends Connection{
		public String name;
	}
	
	public void stop(){
		server.close();
		server.stop();
	}
	
	public void sendMessage(Object message) {
		server.sendToAllTCP(message);
	}
	
}
