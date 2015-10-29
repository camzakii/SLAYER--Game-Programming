package com.collision.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.collision.game.ability.Block;
import com.collision.game.ability.Sword;
import com.collision.game.handler.Animation;
import com.collision.game.handler.GameHandler;
import com.collision.game.handler.GameKeys;
import com.collision.game.networking.Network.PlayerMovement;
import com.collision.game.networking.Network.PlayerShoot;

public class Player{

	public enum PlayerState{
		IDLE,
		BLOCKING,
		MOVING, 
		ATTACKING,
		SLIDING
	}
	
	private static final int WIDTH = 43;
	private static final int HEIGHT = 20;
	
	private GameLevel level;
	
	private Rectangle boundingRectangle;	
	private Vector2 direction;
	private Vector2 position;
	private Vector2 velocity;
	private Sprite playerIcon;
	
	private int lifes;
	private int numBullets;
	private int jumpCount;
	
	private Block block;
	private Sword sword;
	private boolean speed;
	private double speedTimer;
	private PlayerState state;
	private PlayerState lastState;
	private GameHandler game;

	private Sound jumpSound;
	private boolean alive;
	private int id;
	private String name;
	
	private TextureRegion[] rightSpriteRegion;
	private TextureRegion[] leftSpriteRegion;
	private TextureRegion[] leftSwordRegion;
	private TextureRegion[] rightSwordRegion;
	private TextureRegion[] leftBlockRegion;
	private TextureRegion[] rightBlockRegion;
	private TextureRegion[] leftExtSwordRegion;
	private TextureRegion[] rightExtSwordRegion;
	private TextureRegion[] leftWallSlideRegion;
	private TextureRegion[] rightWallSlideRegion;
	
	
	private Animation rightAnimation;
	private Animation leftAnimation;
	private Animation leftSwordAnimation;
	private Animation rightSwordAnimation;
	private Animation leftExtSwordAnimation;
	private Animation rightExtSwordAnimation;
	private Animation blockLeftAnimation;
	private Animation blockRightAnimation;
	private Animation leftWallSlideAnimation;
	private Animation rightWallSlideAnimation;
	
	private Animation currentAnimation;

	private Sound attack;
	private Sound parry;

	// Testing
	
	private boolean isLocal;
	private ShapeRenderer sr;
	private TiledMapTileLayer layer;
	
	public Player(){}
	
	public Player(OrthographicCamera camera, GameLevel level, GameHandler game, boolean isLocal, int id){
		
//		this.boundingRectangle = new Rectangle(100, 150, 16, 16);
//		
//		this.position = new Vector2(100, 150);
		this.direction = new Vector2(1, 0);
		this.level = level;
		this.layer = level.getLayer();
		this.game = game;
		this.isLocal = isLocal;
		
		this.id = id;
		
		this.numBullets = 3;
		this.lifes = 2;
		this.jumpCount = 0;

		this.alive = true;
		this.state = PlayerState.IDLE;
		
		this.sword = new Sword();
		this.block = new Block();
		
		this.velocity = new Vector2();
		initPlayerPosition();
		
		this.rightAnimation = new Animation();
		this.leftAnimation = new Animation();
		this.currentAnimation = new Animation();
		this.leftSwordAnimation = new Animation();
		this.rightSwordAnimation = new Animation();
		this.blockLeftAnimation = new Animation();
		this.blockRightAnimation = new Animation();
		this.leftExtSwordAnimation = new Animation();
		this.rightExtSwordAnimation = new Animation();
		this.leftWallSlideAnimation = new Animation();
		this.rightWallSlideAnimation = new Animation();

		this.attack = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_attack.mp3"));
		this.parry = Gdx.audio.newSound(Gdx.files.internal("sounds/parry.mp3"));

		
		Texture texture = new Texture(Gdx.files.internal("player_sprites/idle_fight_ninja1.png"));
		this.rightSpriteRegion = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
		this.rightAnimation.setAnimation(rightSpriteRegion, 1/3f, rightAnimation);
		
		this.leftSpriteRegion = TextureRegion.split(texture, WIDTH, HEIGHT)[1];
		this.leftAnimation.setAnimation(leftSpriteRegion, 1/3f, leftAnimation);
		
		texture = new Texture(Gdx.files.internal("player_sprites/attack_fight_ninja1.png"));
		this.rightSwordRegion = TextureRegion.split(texture,43, 25)[0];
		this.rightSwordAnimation.setAnimation(rightSwordRegion, 1/8f, rightSwordAnimation);
		
		this.leftSwordRegion = TextureRegion.split(texture, 43, 25)[1];
		this.leftSwordAnimation.setAnimation(leftSwordRegion, 1/8f, leftSwordAnimation);
		
		texture = new Texture(Gdx.files.internal("player_sprites/block_fight_ninja1.png"));
		this.leftBlockRegion = TextureRegion.split(texture, WIDTH, HEIGHT)[1];
		this.blockLeftAnimation.setAnimation(leftBlockRegion, 1/8f, blockLeftAnimation);
		
		this.rightBlockRegion = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
		this.blockRightAnimation.setAnimation(rightBlockRegion, 1/8f, blockRightAnimation);
		
		texture = new Texture(Gdx.files.internal("player_sprites/attack_fight_ninja1_extended.png"));
		this.rightExtSwordRegion = TextureRegion.split(texture, 50, 30)[0];
		this.rightExtSwordAnimation.setAnimation(rightExtSwordRegion, 1/8f, rightExtSwordAnimation);
		
		this.leftExtSwordRegion = TextureRegion.split(texture, 50, 30)[1];
		this.leftExtSwordAnimation.setAnimation(leftExtSwordRegion, 1/8f, leftExtSwordAnimation);

		texture = new Texture(Gdx.files.internal("player_sprites/P1_wall_slide.png"));
		this.rightWallSlideRegion = TextureRegion.split(texture, 43, 20)[1];
		this.rightWallSlideAnimation.setAnimation(rightWallSlideRegion, 1/2f, rightWallSlideAnimation);

		this.leftWallSlideRegion = TextureRegion.split(texture, 43, 20)[0];
		this.leftWallSlideAnimation.setAnimation(leftWallSlideRegion, 1/4f, leftWallSlideAnimation);
		
		currentAnimation = rightAnimation;
		
		setIcon();
		
//		 Testing
		this.sr = new ShapeRenderer();
		sr.setProjectionMatrix(camera.combined);
	}
	
	public void update(float dt){

		if(!alive) return;
		
		
		if(speed){
			if(speedTimer > 5) speed = false;
			speedTimer += dt;
		}
		
		if(lifes <= 0) alive = false;
		
		// Gravity
		velocity.y -= 300 * 2 * dt;
		collisionHandling(dt);
		
		mapWarping();

		animationHandler();
		sword.update(dt);
		block.update(dt);
		
		boundingRectangle.setPosition(this.position);
		
		currentAnimation.setPlaying(true);
		currentAnimation.update(dt);
		GameKeys.update();
	}
	
	public void render(SpriteBatch batch){

		
		if(!alive) return;
		
		batch.begin();
		batch.draw(currentAnimation.getFrame(), position.x - WIDTH / 3, position.y);
		if(isLocal) batch.draw(playerIcon, position.x , position.y + HEIGHT);
		batch.end();
		
//		sr.begin(ShapeType.Line);
//		sr.rect(boundingRectangle.x, boundingRectangle.y, 16, 16);
//		sr.end();
	
		sword.render(sr);
		block.render(sr);
	}
	
	
	private void animationHandler(){
		
		if(state == PlayerState.MOVING){
			
			if(sword.getTimer() > 0) return;
			
			if(state == PlayerState.SLIDING) return;
			
			if(direction.x > 0) currentAnimation = rightAnimation;
			else currentAnimation = leftAnimation;
		}
		if(state == PlayerState.ATTACKING){
			
			if(sword.getPowerup()){
				if(direction.x > 0) currentAnimation = rightExtSwordAnimation;
				else currentAnimation = leftExtSwordAnimation;
			}else{
				if(direction.x > 0) currentAnimation = rightSwordAnimation;
				else currentAnimation = leftSwordAnimation;
			}
			
			if(sword.getTimer() == 0){
				currentAnimation.setCurrentFrame(0);
				state = PlayerState.MOVING;
			}
		}
		if(state == PlayerState.BLOCKING){
			
			if(direction.x > 0) currentAnimation = blockRightAnimation;
			else currentAnimation = blockLeftAnimation;
			
			if(block.getTimer() == 0){
				currentAnimation.setCurrentFrame(0);
				state = PlayerState.MOVING;
			}
			
		}
		if(state == PlayerState.SLIDING) {
			if(direction.x > 0) currentAnimation = rightWallSlideAnimation;
			else currentAnimation = leftWallSlideAnimation;
		}
	}
	
	public void initPlayerPosition(){
		
		Vector2 position = level.randomSpawn(game.getPlayers());
		
		this.boundingRectangle = new Rectangle(position.x, position.y, 16, 16);
		
		this.position = new Vector2(position.x, position.y);
	}
	
	public void mapWarping(){
		if(position.x < 0) position.x = 465;
		if(position.x > 465) position.x = 0;
		if(position.y < 0) position.y = 310;
		if(position.y > 310) position.y = 0;
	}
	
	public void collisionHandling(float dt){
		
		if(state != PlayerState.SLIDING) lastState = state;
		
		boolean collisionX = false;
		boolean collisionY = false;
		
		float prevX = getX();
		float prevY = getY();
		
		position.x += velocity.x * 5 * dt;

		if(velocity.x < 0){
			collisionX = collisionLeft();
		}else if(velocity.x > 0){
			collisionX = collisionRight();
		}
		
		if(collisionX) {
			position.x = prevX;
			velocity.x = 0;
			velocity.y *= 0.55;
			jumpCount = 0;
			if(velocity.y < -10) {
				state = PlayerState.SLIDING;
			}
		}else{
			state = lastState;
		}
		
		position.y += velocity.y * dt * 4;
		
		if(velocity.y < -200) velocity.y = -150;
		
		if(velocity.y < 0){
			collisionY = collisionBottom();
		} else if (velocity.y > 0){
			collisionY = collisionTop();
		}
		
		if(collisionY){
			position.y = prevY;
			velocity.y = 0;
		}
	}
	
	private boolean collisionLeft(){
		 for(float step = 0; step < 17; step += layer.getTileHeight() / 2)
             if(level.isCellBlocked(position.x, position.y + step))
                     return true;
		 
		 return false;
	}
	
	private boolean collisionRight(){
		for(float step = 0; step < 17; step += layer.getTileHeight() / 2 ){
			if(level.isCellBlocked(position.x + 16, position.y + step)){
				return true;
			}
		}
		return false;
	}
	
	private boolean collisionTop(){
		for(float step = 0; step < 17; step += layer.getTileWidth() / 2)
			if(level.isCellBlocked(position.x + step, position.y + 15)){
				return true;
			}
		
		return false;
	}
	
	private boolean collisionBottom(){
		for(float step = 0; step < 17; step += layer.getTileWidth() / 2){
			if(level.isCellBlocked(position.x + step, position.y)){
				jumpCount = 0;
				return true;
			}
		}
		return false;
	}
	
	// Player Actions
	public void jump(){
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("player_sprites/jump.mp3"));
		jumpSound.play();
		if(jumpCount >= 2) return;


		if(speed) velocity.y = 225;
		else velocity.y = 150;
		
		jumpCount++;
	}
	
	public void moveLeft(){
		if(speed) velocity.x = -80;	
		else velocity.x = -40;
		
		direction.x = -1;
		
		if(sword.getTimer() > 0) state = PlayerState.ATTACKING;
		else state = PlayerState.MOVING;
		
		if(block.getTimer() > 0) state = PlayerState.BLOCKING;
		else state = PlayerState.MOVING;
	}
	
	public void moveRight(){
		
		if(speed) velocity.x = 80;
		else velocity.x = 40;
		
		direction.x = 1;
		
		if(sword.getTimer() > 0) state = PlayerState.ATTACKING;
		else state = PlayerState.MOVING;
		
		if(block.getTimer() > 0) state = PlayerState.BLOCKING;
		else state = PlayerState.MOVING;
	}
	
	public void swordAction(){
		
		if(sword.getInterval() > 0) return;
		if(block.getTimer() > 0) return;
		
		sword.action(position, direction, this);
		attack.play();
		state = PlayerState.ATTACKING;

	}
	
	public void parryAction(){
		
		if(sword.getTimer() > 0) return;
		if(block.getBlockTimer() > 0) return;
		
		block.action(position, direction, this);
		parry.play();
		state = PlayerState.BLOCKING;
	}
	
	public void shurikenAction(){
		
		if(numBullets <= 0) return;
		
		PlayerShoot msg = new PlayerShoot(id, position.cpy(), direction.cpy());
		game.addShuriken(msg);
		numBullets--;
		
		if(isLocal){
			game.clientSendMessage(msg);
		}
	}
	
	public void dashAction(){
		velocity.x = direction.x * 300;
	}
	
	public Rectangle getBoundingRectangle(){
		return boundingRectangle;
	}
	
	public void setBoundingRectangle(Rectangle boundingRectangle){
		this.boundingRectangle = boundingRectangle;
	}

	
	public void setPlayerMovement(PlayerMovement msg){
		this.position = msg.position;
		this.velocity = msg.velocity;
		this.direction = msg.direction;
		this.state = msg.state;
		if(state == PlayerState.ATTACKING) this.sword.setTimer(msg.timer);
		if(state == PlayerState.BLOCKING) this.block.setTimer(msg.timer);
	}
	
	public void setVelocityX(float x){
		this.velocity.x = x;
	}
	
	public void setVelocityY(float y){
		this.velocity.y = y;
	}
	
	public void setX(float x){
		this.position.x = x;
	}

	public void setY(float y){
		this.position.y = y;
	}

	private float getX(){
		return position.x;
	}
	
	private float getY(){
		return position.y;
	}
	
	public int getID(){
		return this.id;
	}
	
	public void setPosition(Vector2 position){
		this.position = position;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public void setDirection(Vector2 direction){
		this.direction = direction;
	}
	
	public Vector2 getDirection(){
		return direction;
	}
	
	public Sword getSword(){
		return sword;
	}
	
	public String getName(){
		return name;
	}
	
	public PlayerMovement getPlayerMovement(){
		
		int time = 0;
		
		if(state == PlayerState.ATTACKING) time = this.sword.getTimer();
		if(state == PlayerState.BLOCKING) time = this.block.getTimer();
		
		return new PlayerMovement(this.id, this.position, this.direction, this.velocity, this.state, time);
	}
	
	public Rectangle getSwordRect(){
		return sword.getBoundingBox();
	}

	public PlayerState getState(){
		return state;
	}
	
	public void setID(int id){
		this.id = id;
		System.out.println("ID Changed to: " + id);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public boolean isDead(){
		return !alive;
	}
	
	public void setDead(){
		lifes--;
		boundingRectangle.x = -300;
	}
	
	public int getLifes(){
		return lifes;
	}
	
	public void setState(PlayerState moving){
		this.state = moving;
	}
	
	private void setIcon(){
		
		System.out.println("Player ID: " + id);
		
		if(this.id == 1){
			Texture texture = new Texture(Gdx.files.internal("hud_sprites/player_tag.png"));
			this.playerIcon = new Sprite(texture);
		}
		if(this.id == 2 ){
			Texture texture = new Texture(Gdx.files.internal("hud_sprites/player_tag_2.png"));
			this.playerIcon = new Sprite(texture);
		}
		if(this.id == 3 ){
			Texture texture = new Texture(Gdx.files.internal("hud_sprites/player_tag_3.png"));
			this.playerIcon = new Sprite(texture);
		}
		if(this.id == 4 ){
			Texture texture = new Texture(Gdx.files.internal("hud_sprites/player_tag_4.png"));
			this.playerIcon = new Sprite(texture);
		}
		
	}
	
	public void setSpeed(boolean speed){
		this.speed = speed;
	}
	
	// helper method
	public ShapeRenderer getSR(){
		return this.sr;
	}
	
}
