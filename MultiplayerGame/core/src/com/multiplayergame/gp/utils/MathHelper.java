package com.multiplayergame.gp.utils;

import com.badlogic.gdx.math.Vector2;

public class MathHelper {

	public static Vector2 pointOnCircle(float radius, float degrees, Vector2 origin){
		
		Vector2 vec = new Vector2();
		vec.x = (float)(radius * Math.cos(degrees * Math.PI / 180)) + origin.x;
		vec.y = (float)(radius * Math.sin(degrees * Math.PI / 180)) + origin.y;
		
		return vec;
	}
	
}
