package com.collision.game.handler;

import java.util.Stack;

import com.collision.game.MyGdxGame;
import com.collision.game.states.GameScreen;
import com.collision.game.states.GameState;

public class GameStateManager {

	public static final int PLAY = 2;
	
	private MyGdxGame game;
	private Stack<GameState> gameStates;
	
	public GameStateManager(MyGdxGame game){
		this.game = game;
		this.gameStates = new Stack<GameState>();
		pushState(PLAY);
	}
	
	public void update(float dt){
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
	private GameState getState(int state){
		
		if(state == PLAY){
			return new GameScreen(this);
		}
		
		return null;
	}
	
	public void setState(int state){
		popState();
		pushState(state);
	}
	
	public void pushState(int state){
		gameStates.push(getState(state));
	}
	
	public void popState(){
		GameState g = gameStates.pop();
		g.dispose();
	}
	
	public MyGdxGame getGame(){
		return this.game;
	}
	
}
