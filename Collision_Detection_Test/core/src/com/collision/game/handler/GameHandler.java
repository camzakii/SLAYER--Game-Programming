package com.collision.game.handler;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.collision.game.MyGdxGame;
import com.collision.game.ability.Shuriken;
import com.collision.game.entity.GameLevel;
import com.collision.game.entity.Player;
import com.collision.game.entity.Player.PlayerState;
import com.collision.game.networking.GameClient;
import com.collision.game.networking.GameServer;
import com.collision.game.networking.Network.LeaveJoin;
import com.collision.game.networking.Network.PlayerAttack;
import com.collision.game.networking.Network.PlayerHit;
import com.collision.game.networking.Network.PlayerMovement;
import com.collision.game.networking.Network.PlayerShoot;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.ControllerListener;

public class GameHandler {

	private Map<Integer, Player> players;
	private Array<Shuriken> shurikens;
	
	private Player player;
	private GameLevel level;
	
	private GameClient client;
	private GameServer server;
	private CollisionHandler collisionHandler;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private boolean isClient;
	
	public GameHandler(GameClient client){
		this.client = client;
		this.isClient = true;
		
		init();
	}
	
	public GameHandler(GameServer server){
		this.server = server;
		this.isClient = false;
		
		init();
	}
	
	// removed synchronized 
	public synchronized void update(float dt){
		
		if(client != null && player != null){
			handleInput();
			client.sendMessageUDP(player.getPlayerMovement());
		}

		for(Player mpPlayer : players.values()){	
			mpPlayer.update(dt);
			for(Player player2 : players.values()){
				if(mpPlayer.getSwordRect().overlaps(player2.getBoundingRectangle())){	
					if(mpPlayer.getState() != PlayerState.BLOCKING){
						if(isClient){
							PlayerHit hit = new PlayerHit(player2.getID(), mpPlayer.getID());
							playerHit(hit);
							client.sendMessage(hit);
					
						}
					}
				}
			}
		}	
		
		for(Shuriken shuriken: shurikens){
			shuriken.update(dt);
			for(Player currentPlayer : players.values()){
				if(shuriken.getBoundingBox().overlaps(currentPlayer.getBoundingRectangle())){
					System.out.println("PLAYER HIT!!");
				}
			}
		}
	}
	
	// removed synchronized 
	public synchronized void render(){
			
		batch.setProjectionMatrix(camera.combined);
		level.render();
		
		for(Player mpPlayer : players.values()){
			mpPlayer.render(batch);
		}
		
		for(Shuriken shuriken: shurikens){
			shuriken.render(batch);
		}
		
	}
	
	public void handleInput(){
		if(!player.isDead()){
			
			if(Gdx.input.isKeyJustPressed(Keys.W)){
				player.jump();
			}
			if(Gdx.input.isButtonPressed(XboxController.BUTTON_A)) {
				player.jump();
			}
			if(Gdx.input.isKeyPressed(Keys.A)){
				player.moveLeft();
			}
			if(Gdx.input.isKeyPressed(Keys.D)){
				player.moveRight();
			}
			if(Gdx.input.isKeyPressed(Keys.S)){
				player.swordAction();
			}
			if(Gdx.input.isKeyPressed(Keys.C)){
				player.parryAction();
			}
			if(!Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)){
				player.setVelocityX(0);
			}
			if(Gdx.input.isKeyJustPressed(Keys.V)){
				player.shurikenAction();
			}
//			if(Gdx.input.isButtonPressed(XboxController.BUTTON_X)){
//				player.shurikenAction();
//			}
		}
	}
	
	public void connect(String name){
		if(this.player == null){
			player = new Player(camera, level, this, true);
			player.setID(client.id);
			player.setName(name);
			players.put(client.id, player);
			System.out.println("Player created");
		}else{
			System.out.println("Player not created");
		}
	}
	
	public void onDisconnect(){
		this.client = null;
		this.players.clear();
	}
	
	// removed synchronized 
	public synchronized void addPlayer(LeaveJoin msg){
		Player newPlayer = new Player(camera, level, this, false);
		newPlayer.setID(msg.playerId);
		newPlayer.setName(msg.name);
		newPlayer.setPosition(new Vector2(250, 100));
		this.players.put(msg.playerId, newPlayer);
		
		System.out.println("Player Added: " + msg.playerId);
		
//		return newPlayer;
	}
	
	public synchronized void removePlayer(LeaveJoin msg){
		System.out.println("Player removed");
		players.remove(msg.playerId);
	}
	
	public synchronized void playerMoved(PlayerMovement msg) {
		Player currentPlayer = players.get(msg.playerId);
		if(currentPlayer != null) currentPlayer.setPlayerMovement(msg);
	}
	
	public synchronized void playerAttack(PlayerAttack msg){
		Player currentPlayer = getPlayerById(msg.playerId);
		if(currentPlayer != null){
			currentPlayer.setBoundingRectangle(msg.boundingBox);
		}
	}
	
	public synchronized void playerHit(PlayerHit msg){
		Player currentPlayer = getPlayerById(msg.playerIdVictim);
		currentPlayer.setDead();
		System.out.println("Player Killed!");
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public synchronized void addShuriken(PlayerShoot msg){
		shurikens.add(new Shuriken(level, level.getLayer(), msg.direction, msg.position));
	}
	
	public synchronized Player getPlayerById(int id){
		return players.get(id);
	}
	
	public synchronized void clientSendMessage(Object msg){
		client.sendMessage(msg);
	}
	
	private void init(){
		this.players = new HashMap<Integer, Player>();
		this.shurikens = new Array<Shuriken>();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, MyGdxGame.WIDTH * 1.3f, MyGdxGame.HEIGHT * 1.3f );
		this.level = new GameLevel(camera);
		this.batch = new SpriteBatch();
		this.collisionHandler = new CollisionHandler();
	}
	
}
