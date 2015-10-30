package com.collision.game.handler;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.collision.game.MyGdxGame;
import com.collision.game.ability.Shuriken;
import com.collision.game.entity.GameLevel;
import com.collision.game.entity.Player;
import com.collision.game.entity.Player.PlayerState;
import com.collision.game.entity.Powerup;
import com.collision.game.entity.Powerup.PowerupType;
import com.collision.game.hud.GameHud;
import com.collision.game.networking.GameClient;
import com.collision.game.networking.GameServer;
import com.collision.game.networking.Network.*;
import com.collision.game.utils.ParticleEngine;

public class GameHandler {

	private Map<Integer, Player> players;
	private Array<Shuriken> shurikens;
	private Array<Powerup> powerups;
	
	private Player player;
	private GameLevel level;
	private GameHud gameHud;
	
	private Sprite gameWonSprite;
	
	private double powerupCooldown;
	private boolean gameWon;
	
	private GameClient client;
	private GameServer server;
	private ParticleEngine particleEngine;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthographicCamera camera2;
	
	private boolean isClient;
	private int spriteIndex;
	private Sprite returnLobbySprite;
	
	// camera shake
	private float time;
	private float current_time;
	private float power;
	private float current_power;
	
	private Controller controller;
	boolean hasControllers = true;
	
	private float shurikenTimer;
	private float jumpTimer;
	
	public GameHandler(GameClient client, int spriteIndex){
		this.client = client;
		this.isClient = true;
		this.spriteIndex = spriteIndex;
		
		init();
	}
	
	public GameHandler(GameServer server){
		this.server = server;
		this.isClient = false;
		
		init();
	}
	
	public synchronized void update(float dt){
		
		if(client != null && player != null){
			handleInput();
			client.sendMessageUDP(player.getPlayerMovement());
		}
		
		if(camera.zoom < 0.15){
			camera.zoom = 0.15f;
		}
		
		if(shurikenTimer > 0) shurikenTimer -= dt;
		if(jumpTimer > 0) jumpTimer -= dt;
		
		cameraShake(dt);
		
		checkWin();
		gameHud.update(dt, players);
		
		particleEngine.update();

		for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
			Player mpPlayer = playerEntry.getValue();
			mpPlayer.update(dt);
			for(Player player2 : players.values()){
				
				if(mpPlayer.getSwordRect().overlaps(player2.getBoundingRectangle()) &&
						player2.getState() != PlayerState.BLOCKING &&
						mpPlayer.getSword().getSwordOnHit() <= 0 && !player2.equals(mpPlayer)){	
					
					mpPlayer.getSword().setSwordOnHit(5);
					
					particleEngine.createParticles(mpPlayer.getSword().getBoundingBox().x, player.getSword().getBoundingBox().y);
					
					if(isClient){
						PlayerHit hit = new PlayerHit(player2.getID(), mpPlayer.getID());
						this.playerHit(hit);
						client.sendMessage(hit);
					}
				}				
			}
		}	
		
		for(Shuriken shuriken: shurikens){
			shuriken.update(dt);
			for(Player currentPlayer : players.values()){
				if(shuriken.getBoundingBox().overlaps(currentPlayer.getBoundingRectangle()) && 
						currentPlayer.getState() != PlayerState.BLOCKING &&
						shuriken.getOnHitTimer() <= 0){
					
					shuriken.setOnHitTimer(5);
					shuriken.setDead();
					
					if(isClient){
						PlayerHit hit = new PlayerHit(currentPlayer.getID(), -1);
						this.playerHit(hit);
						client.sendMessage(hit);
					}
				}
				if(shuriken.getBoundingBox().overlaps(currentPlayer.getBoundingRectangle()) && 
						currentPlayer.getState() == PlayerState.BLOCKING &&
						shuriken.getOnHitTimer() <= 0){
					if(shuriken.getDirection().x == 1) shuriken.setDirection(new Vector2(-1, 0));
					else shuriken.setDirection(new Vector2(1, 0));
				}
			}
		}
		
		for(Powerup powerup: powerups){
			powerup.update(dt);
			for(Player currentPlayer : players.values()){
				if(powerup.getBoundingBox().overlaps(currentPlayer.getBoundingRectangle())){
					
					PlayerPowerup msg = new PlayerPowerup(currentPlayer.getID(), powerup.getType());
					this.playerPowerup(msg);
					powerups.removeValue(powerup, true);
					
					if(!isClient){
						server.sendMessage(msg);
					}
				}
			}
		}
		
		if(!isClient){	
			if(powerups.size == 0 && powerupCooldown <= 0 && players.size() >= 2){
				PowerupData powerupData = new PowerupData(Powerup.powerup_spawns.random(), Powerup.randomPowerup());
				this.addPowerup(powerupData);
				server.sendMessage(powerupData);
				powerupCooldown = 20;
			}
			if(powerups.size == 0 ) powerupCooldown -= dt;
		}
	}
	
	public synchronized void render(){
			
		batch.setProjectionMatrix(camera.combined);
		level.render();
		
		gameHud.render(batch);
		
		particleEngine.render(batch);
		
		for(Player mpPlayer : players.values()){
			mpPlayer.render(batch);
		}
		
		for(Shuriken shuriken: shurikens){
			shuriken.render(batch, player.getSR());
		}
		
		for(Powerup powerup: powerups){
			powerup.render(batch);
		}
		
		batch.setProjectionMatrix(camera2.combined);
		
		batch.begin();
		if(gameWon){
				batch.draw(gameWonSprite, 0, 200);
				batch.draw(returnLobbySprite, 0, 0);

		}
		batch.end();
	}
	
	public void handleInput(){
		if(!player.isDead() && !gameWon && !hasControllers){
			
			if(Gdx.input.isKeyJustPressed(Keys.W)){
				player.jump();
			}
			if(Gdx.input.isKeyPressed(Keys.A)){
				player.moveLeft();
			}
			if(Gdx.input.isKeyPressed(Keys.D)){
				player.moveRight();
			}
			if(Gdx.input.isKeyPressed(Keys.Y)){
				player.swordAction();
			}
			if(Gdx.input.isKeyPressed(Keys.I)){
				player.parryAction();
			}
			if(!Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)){
				player.setVelocityX(0);
			}
			if(Gdx.input.isKeyJustPressed(Keys.U)){
				player.shurikenAction();
			}
			if(Gdx.input.isKeyJustPressed(Keys.B)){
				player.dashAction();
			}
		}
		else if(!player.isDead() && !gameWon){
			
			if(controller.getAxis(XBox360Pad.AXIS_LEFT_X) > 0.2f ){
				player.moveRight();
			}
			if(controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -0.2f){
				player.moveLeft();
			}
			if(controller.getAxis(XBox360Pad.AXIS_LEFT_X) < 0.2f &&
					controller.getAxis(XBox360Pad.AXIS_LEFT_X) > -0.2f){
				player.setVelocityX(0);
			}
            if(controller.getButton(XBox360Pad.BUTTON_A) && jumpTimer <= 0){
            	player.jump();
            	jumpTimer = 0.2f;
            }
            if(controller.getButton(XBox360Pad.BUTTON_B)){
            	player.parryAction();
            }
            if(controller.getButton(XBox360Pad.BUTTON_Y) && shurikenTimer <= 0){
            	player.shurikenAction();
            	shurikenTimer = 1f;
            }
            if(controller.getButton(XBox360Pad.BUTTON_X)){
            	player.swordAction();
            }
		}
	}
	
	public void checkWin(){
		
		Player p = new Player();
		
		if(!gameStart()) return;
		
		int index = 0;
		
		for(Player player: players.values()){
			if(!player.isDead()) index++;
		}
		
		if(index == 1) {
			for(Player player: players.values()){
				
				if(!player.isDead()){
					
					p = player;
					
					if(player.getID() == 1){
						Texture texture = new Texture(Gdx.files.internal("screen_sprites/P1_wins.png"));
						gameWonSprite = new Sprite(texture);
					}
					if(player.getID() == 2){
						Texture texture = new Texture(Gdx.files.internal("screen_sprites/P2_wins.png"));
						gameWonSprite = new Sprite(texture);
					}
					if(player.getID() == 3){
						Texture texture = new Texture(Gdx.files.internal("screen_sprites/P3_wins.png"));
						gameWonSprite = new Sprite(texture);
					}
					if(player.getID() == 4){
						Texture texture = new Texture(Gdx.files.internal("screen_sprites/P4_wins.png"));
						gameWonSprite = new Sprite(texture);
					}
					
					camera.zoom -= 0.02;
					camera.update();
					
				}
			}
			gameWon = true;
		}
		
		if(gameWon){
			
			if(camera.position.x < p.getPosition().x){
				camera.position.x += 2;
			}
			if(camera.position.x > p.getPosition().x){
				camera.position.x -= 2;
			}
			if(camera.position.y < p.getPosition().y){
				camera.position.y += 2;
			}
			if(camera.position.y > p.getPosition().y){
				camera.position.y -= 2;
			}
		}
	}
	
	public void connect(String name){
		if(this.player == null){
			player = new Player(camera, level, this, true, client.id, spriteIndex);
			player.setName(name);
			players.put(client.id, player);
		}
	}
	
	public void onDisconnect(){
		this.client = null;
		this.players.clear();
	}
	
	public synchronized void addPlayer(LeaveJoin msg){
		
		Player newPlayer = new Player(camera, level, this, false, msg.playerId, msg.spriteIndex);
		newPlayer.setName(msg.name);
		newPlayer.setPosition(new Vector2(250, 100));
		this.players.put(msg.playerId, newPlayer);
	}
	
	private boolean gameStart(){
		
		if(players.size() >= 2) return true;
		return false;
	}
	
	public synchronized void removePlayer(LeaveJoin msg){
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
		Player currentPlayer = players.get(msg.playerIdVictim);
		shake(.8f, .3f);
		
		if(currentPlayer != null){
			currentPlayer.setPosition(level.randomSpawn(players));
			currentPlayer.setDead();
			currentPlayer.resetShurikens();
		}
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public synchronized void addShuriken(PlayerShoot msg){
		shurikens.add(new Shuriken(level, level.getLayer(), msg.direction, msg.position));
	}
	
	public synchronized void addPowerup(PowerupData msg){
		powerups.add(new Powerup(msg.position, msg.type));
	}
	
	public synchronized void playerPowerup(PlayerPowerup msg){
		Player player = players.get(msg.playerId);
		
		System.out.println("POWERUP TYPE: " + msg.type);
		
		if(msg.type.equals(PowerupType.RANGE)) player.getSword().setPowerup(true);
		else if(msg.type.equals(PowerupType.SPEED)) player.setSpeed();
	}
	
	public synchronized Player getPlayerById(int id){
		return players.get(id);
	}
	
	public synchronized void clientSendMessage(Object msg){
		client.sendMessage(msg);
	}
	
	private void shake(float power, float time){
		this.power = power;
		this.time = time;
		this.current_time = 0;
	}
	
	private void cameraShake(float dt){
		if(current_time <= time){
			current_power = power * ((time - current_time) / time);
			
			camera.position.x += (float) ((Math.random() - 0.5f) * 2 * current_power);
			camera.position.y += (float) ((Math.random() - 0.5f) * 2 * current_power);

			camera.update();
			
			current_time += dt;
		}else if(!gameWon){
			camera.position.x = 240.49998f;
			camera.position.y = 156f;
			camera.update();
		}
	}
	
	private void init(){

		Texture texture = new Texture(Gdx.files.internal("screen_sprites/return_lobby.png"));
		this.returnLobbySprite = new Sprite(texture);

		this.players = new HashMap<Integer, Player>();
		this.shurikens = new Array<Shuriken>();
		this.powerups = new Array<Powerup>();
		
		// Libgdx Components
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, MyGdxGame.WIDTH * 1.3f, MyGdxGame.HEIGHT * 1.3f );
		this.camera2 = new OrthographicCamera();
		this.camera2.setToOrtho(false, MyGdxGame.WIDTH * 1.3f, MyGdxGame.HEIGHT * 1.3f );
		this.batch = new SpriteBatch();
		
		// Game components
		this.level = new GameLevel(camera);
		this.particleEngine = new ParticleEngine();
		this.gameHud = new GameHud(camera, this);
		this.gameWon = false;
		
		this.shurikenTimer = 0;
		this.jumpTimer = 0;
		
		if(this.controller == null) hasControllers = false;
		else hasControllers = true;
		
		if(Controllers.getControllers().size == 0) hasControllers = false;
        else controller = Controllers.getControllers().first();
	}
	
	public Map<Integer, Player> getPlayers(){
		return players;
	}
}
