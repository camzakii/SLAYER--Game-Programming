package com.collision.game.networking;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.entity.Player.PlayerState;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

	public static final int PORT = 6464;
	public static final int UDP = 6466;
	public static final int VERSION = 10;
	
	static public void register(EndPoint endPoint){
		Kryo kryo = endPoint.getKryo();
		kryo.register(Login.class);
		kryo.register(Vector2.class);
		kryo.register(LeaveJoin.class);
		kryo.register(PlayerMovement.class);
		kryo.register(PlayerState.class);
		kryo.register(PlayerAttack.class);
		kryo.register(Rectangle.class);
		kryo.register(PlayerHit.class);
	}
	
	public static class Login{
		public String name;
		public int version;

		public Login() {}
		public Login(String name, int version){
			this.name = name;
			this.version = version;
		}
	}
	
	public static class LeaveJoin{
		public int playerId;
		public String name;
		public boolean hasJoined;
		public Vector2 position;
	
		public LeaveJoin(){};
		public LeaveJoin(int playerId, String name, boolean hasJoined, Vector2 position){
			this.playerId = playerId;
			this.name = name;
			this.hasJoined = hasJoined;
			this.position = position;
		}
	}
	
	public static class PlayerMovement{
		public int playerId;
		public Vector2 position;
		public Vector2 direction;
		public Vector2 velocity;
		public PlayerState state;
		public int timer;
		
		public PlayerMovement(){}
		public PlayerMovement(int playerId, Vector2 position, Vector2 direction, Vector2 velocity, PlayerState state, int timer){
			this.playerId = playerId;
			this.position = position;
			this.direction = direction;
			this.velocity = velocity;
			this.state = state;
			this.timer = timer;
		}
	}
	
	public static class PlayerShoot{
		public int playerId;
		public Vector2 position;
		public Vector2 direction;
		
		public PlayerShoot(){}
		public PlayerShoot(int playerId, Vector2 position, Vector2 direction){
			this.playerId = playerId;
			this.position = position;
			this.direction = direction;
		}
	}
	
	public static class PlayerAttack{
		public int playerId;
		public Rectangle boundingBox;
		public Vector2 position;
		
		public PlayerAttack(){}
		public PlayerAttack(int playerId, Rectangle boundingBox, Vector2 position){
			this.playerId = playerId;
			this.boundingBox = boundingBox;
			this.position = position;
		}
	}
	
	public static class PlayerHit{
		public int playerIdVicitm;
		public int playerIdSource;
		
		public PlayerHit(){}
		public PlayerHit(int playerIdVicitm, int playerIdSource){
			this.playerIdVicitm = playerIdVicitm;
			this.playerIdSource = playerIdSource;
		}
	}
	
}
