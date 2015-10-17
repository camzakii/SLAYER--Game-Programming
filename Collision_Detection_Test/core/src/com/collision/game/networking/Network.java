package com.collision.game.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

	public static final int PORT = 6464;
	public static final int UDP = 6466;
	public static final int VERSION = 10;
	
	public static void register(EndPoint endPoint){
		Kryo kryo = endPoint.getKryo();
		kryo.register(Login.class);
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
	
}
