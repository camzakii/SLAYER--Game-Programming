package com.collision.game.handler;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.collision.game.MyGdxGame;
import com.collision.game.states.GameLobby;
import com.collision.game.states.GameMenu;
import com.collision.game.states.GameScreen;
import com.collision.game.states.GameState;

public class GameStateManager {

	public static final int PLAY = 2;
	public static final int MENU = 4;
	public static final int LOBBY = 6;
	
	private MyGdxGame game;
	private Stack<GameState> gameStates;
	private Music menuMusic;
	public GameStateManager(MyGdxGame game){
		this.game = game;
		this.gameStates = new Stack<GameState>();
		pushState(MENU, false, "", "");
	}
	
	public void update(float dt){
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
	private GameState getState(int state, boolean isHost, String ip, String name){



		if(state == PLAY){
			if(isHost) return new GameScreen(this, true, ip, name);
			else return new GameScreen(this, false, ip, name);
		}
		if(state == MENU){

			return new GameMenu(this);
		}
		
		if(state == LOBBY){


			return new GameLobby(this);
		}
		
		return null;
	}
	
	public void setState(int state, boolean isHost, String ip, String name){
		popState();
		pushState(state, isHost, ip, name);
	}
	
	public void pushState(int state, boolean isHost, String ip, String name){
		gameStates.push(getState(state, isHost, ip, name));
	}
	
	public void popState(){
		GameState g = gameStates.pop();
		g.dispose();
	}
	
	public MyGdxGame getGame(){
		return this.game;
	}
	
}
