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
		if(keycode == Keys.O){
			GameKeys.setKey(GameKeys.O, true);
		}
		if(keycode == Keys.H){
			GameKeys.setKey(GameKeys.H, true);
		}
		if(keycode == Keys.U){
			GameKeys.setKey(GameKeys.U, true);
		}
		if(keycode == Keys.K){
			GameKeys.setKey(GameKeys.K, true);
		}
		if(keycode == Keys.J){
			GameKeys.setKey(GameKeys.J, true);
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
		if(keycode == Keys.O){
			GameKeys.setKey(GameKeys.O, false);
		}
		if(keycode == Keys.H){
			GameKeys.setKey(GameKeys.H, false);
		}
		if(keycode == Keys.U){
			GameKeys.setKey(GameKeys.U, false);
		}
		if(keycode == Keys.K){
			GameKeys.setKey(GameKeys.K, false);
		}
		if(keycode == Keys.J){
			GameKeys.setKey(GameKeys.J, false);
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
