package com.collision.game.handler;

public class GameKeys {

	private static boolean[] keys;
	private static boolean[] pkeys;
	
	private static final int NUM_KEYS = 11;
	
	public static final int W = 0;
	public static final int S = 1;
	public static final int A = 2;
	public static final int	D = 3;
	public static final int O = 5;
	
	public static final int H = 6;
	public static final int K = 7;
	public static final int U = 8;
	public static final int J = 9;
	public static final int R = 4;

	
	static{
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update(){
		for(int i = 0; i < NUM_KEYS; i++){
			pkeys[i] = keys[i];
		}
	}
	
	public static void setKey(int k, boolean b){
		keys[k] = b;
	}
	
	public static boolean isDown(int k){
		return keys[k];
	}
	
	public static boolean isUp(int k){
		return keys[k] == false;
	}
	
	
	public static boolean isPressed(int k){
		return keys[k] && !pkeys[k];
	}
}
