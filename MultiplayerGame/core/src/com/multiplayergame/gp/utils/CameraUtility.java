package com.multiplayergame.gp.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.*;

public class CameraUtility {

	public static void lerpToTarget(Camera camera, Vector2 target){
		Vector3 position = camera.position;
		position.x = camera.position.x + (target.x - camera.position.x) * .1f;
//		position.y = camera.position.y + (target.y - camera.position.y) * .1f;
		camera.position.set(position);
		camera.update();
	}
	
	public static void boundary(Camera camera, float startX, float startY, float width, float height){
		
		Vector3 position = camera.position;
		
		if(position.x < startX){
			position.x = startX;
		}
		if(position.y < startY){
			position.y = startY;
		}
		
		if(position.x > startX + width){
			position.x = startX + width;
		}
		if(position.y > startY + height){
			position.y = startY + height;
		}
		
		camera.position.set(position);
		camera.update();
		
	}
	
}
