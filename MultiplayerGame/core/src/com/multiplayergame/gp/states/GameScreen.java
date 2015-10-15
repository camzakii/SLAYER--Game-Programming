package com.multiplayergame.gp.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.multiplayergame.gp.MultiplayerGame;
import com.multiplayergame.gp.entities.Enemy;
import com.multiplayergame.gp.entities.Entity;
import com.multiplayergame.gp.entities.Player;
import com.multiplayergame.gp.handlers.CustomContactListener;
import com.multiplayergame.gp.handlers.CustomInputProcessor;
import com.multiplayergame.gp.handlers.GameKeys;
import com.multiplayergame.gp.handlers.GameStateManager;
import com.multiplayergame.gp.utils.BodyFactory;
import com.multiplayergame.gp.utils.CameraUtility;
import com.multiplayergame.gp.utils.ParticleEngine;
import com.multiplayergame.gp.utils.Vars;

public class GameScreen extends GameState{

	private World world;
	private Box2DDebugRenderer debug;
	private Array<Entity> enemies;
	private CustomContactListener contactListener;
	private OrthographicCamera b2dcam;
	private Player player;
	
	private ParticleEngine particleEngine;
	
	public GameScreen(GameStateManager gsm) {
		super(gsm);
		
		this.world = new World(new Vector2(0, -4f), true);
		this.debug = new Box2DDebugRenderer();
		this.enemies = new Array<Entity>();
		this.player = new Player(BodyFactory.createBody(this.world, 50, 100, 16, 16, 1, Vars.BIT_PLAYER, Vars.BIT_GROUND, false, "player"), world);
		this.b2dcam = new OrthographicCamera();
		this.b2dcam.setToOrtho(false, MultiplayerGame.WIDTH * 2, MultiplayerGame.HEIGHT * 2);
		
		this.particleEngine = new ParticleEngine();
		
		Enemy enemy = new Enemy(BodyFactory.createEnemyBody(this.world, 120, 100, 16, 16, 1), camera);
		enemies.add(enemy);
		
		Gdx.input.setInputProcessor(new CustomInputProcessor());
		this.contactListener = new CustomContactListener();
		world.setContactListener(contactListener);
		
		Body body = BodyFactory.createBody(world, 0, 30, 600, 30, 3, Vars.BIT_GROUND, Vars.BIT_PLAYER, false, "ground");
	}

	@Override
	public void update(float dt) {

		handleInput();
		
		world.step(dt, 6, 2);
		GameKeys.update();

		player.update(dt);
		particleEngine.update();
		
		remove();
		for(Entity entity: enemies) entity.update(dt);
		
		isAttacked();
		
		CameraUtility.lerpToTarget(camera, player.getPosition().scl(100));
		
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
		
		Gdx.gl.glClearColor(1f, 1f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		for(Entity entity: enemies) entity.render(batch);
		player.render(batch);
		
		particleEngine.render(batch);
//		debug.render(world, camera.combined.scl(Vars.UNITS));
	}

	@Override
	public void handleInput() {
		player.handleInput();
	}
	
	public void remove(){
		for(Entity entity: enemies){
			if(!entity.isAlive()){
				enemies.removeValue(entity, true);
				world.destroyBody(entity.getBody());
			}
		}
	}
	
	public void isAttacked(){
		if(contactListener.getAttacked()){
			particleEngine.createParticles(player.getSwordPosition().x * 100 - 16, player.getSwordPosition().y * 100 + 10);
			contactListener.setAttacked(false);
			player.getCombo().setCombo();
		}
		if(player.isMoving()){
			particleEngine.createParticles(player.getPosition().x * 100 - 20, player.getPosition().y * 100 + 4 );
		}
	}
	
	@Override
	public void dispose() {
		
	}
}
