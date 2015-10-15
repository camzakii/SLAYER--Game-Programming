package com.multiplayergame.gp.handlers;

import java.util.Stack;

import com.multiplayergame.gp.MultiplayerGame;
import com.multiplayergame.gp.states.GameScreen;
import com.multiplayergame.gp.states.GameState;

public class GameStateManager {

	public static final int PLAY = 2;
	
	private MultiplayerGame game;
	private Stack<GameState> gameStates;
	
	public GameStateManager(MultiplayerGame game){
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
	
	public MultiplayerGame getGame(){
		return this.game;
	}
	
}
