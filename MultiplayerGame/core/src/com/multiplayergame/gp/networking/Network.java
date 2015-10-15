//package com.multiplayergame.gp.networking;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.math.Vector2;
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryonet.EndPoint;
//import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
//
//public class Network {
//
//	public static final int port = 6464;
//	public static final int portUdp = 6466;
//	public static final int version = 10;
//
//	public static void register(EndPoint endpoint){
//		Kryo kryo = endpoint.getKryo();
//		kryo.register(Ping.class);
//		kryo.register(Login.class);
//		kryo.register(Vector2.class);
//	}
//
//	public static class Login{
//		public String name;
//		public int version;
//		public Color color;
//
//		public Login(){}
//
//		public Login(String name, int version, Color color){
//			this.name = name;
//			this.version = version;
//			this.color = color;
//		}
//
//	}
//
//	public static class Movement{
//		public int playerId;
//		public Vector2 position;
//		public Vector2 direction;
//
//
//		public Movement(){}
//
//		public Movement(int playerId, Vector2 position, Vector2 direction){
//			this.playerId = playerId;
//			this.position = position;
//			this.direction = direction;
//		}
//	}
//
//	public static class PlayerFight{
//		public int playerId;
//		public Vector2 position;
//		public Vector2 direction;
//
//
//		public PlayerFight(){}
//
//		public PlayerFight(int playerId, Vector2 position, Vector2 direction){
//			this.playerId = playerId;
//			this.position = position;
//			this.direction = direction;
//		}
//	}
//
//}
