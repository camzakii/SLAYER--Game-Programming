package com.collision.game.handler;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class CustomInputProcessor implements InputProcessor {
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.W){
			GameKeys.setKey(GameKeys.W, true);
		}
		if(keycode == Keys.S){
			GameKeys.setKey(GameKeys.S, true);
		}
		if(keycode == Keys.A){
			GameKeys.setKey(GameKeys.A, true);
		}
		if(keycode == Keys.D){
			GameKeys.setKey(GameKeys.D, true);
		}
		if(keycode == Keys.R){
			GameKeys.setKey(GameKeys.R, true);
		}
		if(keycode == Keys.M){
			GameKeys.setKey(GameKeys.M, true);
		}
		if(keycode == Keys.LEFT){
			GameKeys.setKey(GameKeys.LEFT, true);
		}
		if(keycode == Keys.UP){
			GameKeys.setKey(GameKeys.UP, true);
		}
		if(keycode == Keys.RIGHT){
			GameKeys.setKey(GameKeys.RIGHT, true);
		}
		if(keycode == Keys.DOWN){
			GameKeys.setKey(GameKeys.DOWN, true);
		}
		if(keycode == Keys.C){
			GameKeys.setKey(GameKeys.C, true);
		}
		if(keycode == Keys.V){
			GameKeys.setKey(GameKeys.V, true);
		}
		if(keycode == Keys.N){
			GameKeys.setKey(GameKeys.N, true);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.W){
			GameKeys.setKey(GameKeys.W, false);
		}
		if(keycode == Keys.S){
			GameKeys.setKey(GameKeys.S, false);
		}
		if(keycode == Keys.A){
			GameKeys.setKey(GameKeys.A, false);
		}
		if(keycode == Keys.D){
			GameKeys.setKey(GameKeys.D, false);
		}
		if(keycode == Keys.R){
			GameKeys.setKey(GameKeys.R, false);
		}
		if(keycode == Keys.M){
			GameKeys.setKey(GameKeys.M, false);
		}
		if(keycode == Keys.LEFT){
			GameKeys.setKey(GameKeys.LEFT, false);
		}
		if(keycode == Keys.UP){
			GameKeys.setKey(GameKeys.UP, false);
		}
		if(keycode == Keys.RIGHT){
			GameKeys.setKey(GameKeys.RIGHT, false);
		}
		if(keycode == Keys.DOWN){
			GameKeys.setKey(GameKeys.DOWN, false);
		}
		if(keycode == Keys.C){
			GameKeys.setKey(GameKeys.C, false);
		}
		if(keycode == Keys.V){
			GameKeys.setKey(GameKeys.V, false);
		}
		if(keycode == Keys.N){
			GameKeys.setKey(GameKeys.N, false);
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
	
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		
		return false;
	}

}
