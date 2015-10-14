package com.collision.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.collision.game.handler.CustomInputProcessor;
import com.collision.game.handler.GameKeys;

public class Player2 {

	public enum PlayerState{
		IDLE,
		ATTACKING,
		BLOCKING,
		MOVING
	}

	private static final int WIDTH = 43;
	private static final int HEIGHT = 20;
	
	private GameLevel level;
	
	private Rectangle boundingRectangle;	
	private Vector2 direction;
	private Vector2 position;

	private float speed;
	private Vector2 velocity;
	private boolean alive;
	
	private Block block;
	private Sword sword;
	private PlayerState state;
//	private CollisionComponent collisionHandler;
	
	private TextureRegion[] rightSpriteRegion;
	private TextureRegion[] leftSpriteRegion;
	private TextureRegion[] leftSwordRegion;
	private TextureRegion[] rightSwordRegion;
	
	private Animation rightAnimation;
	private Animation leftAnimation;
	private Animation leftSwordAnimation;
	private Animation rightSwordAnimation;
	private Animation currentAnimation;

	// Testing
	private ShapeRenderer sr;
	private TiledMapTileLayer layer;
	
	public Player2(OrthographicCamera camera, GameLevel level){
		
		this.boundingRectangle = new Rectangle(150, 100, WIDTH, HEIGHT);
		
		this.position = new Vector2(150, 100);
		this.direction = new Vector2();
		this.level = level;
		this.layer = level.getLayer();
		
		this.alive = true;
		this.state = PlayerState.IDLE;
		this.sword = new Sword();
		this.block = new Block();
//		this.collisionHandler = new CollisionComponent(this);
		
		this.speed = 150;
		this.velocity = new Vector2();
		
		this.rightAnimation = new Animation();
		this.leftAnimation = new Animation();
		this.currentAnimation = new Animation();
		this.leftSwordAnimation = new Animation();
		this.rightSwordAnimation = new Animation();
		
		Texture texture = new Texture(Gdx.files.internal("player.png"));
		this.rightSpriteRegion = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
		this.rightAnimation.setAnimation(rightSpriteRegion, 1/3f, rightAnimation);
		
		this.leftSpriteRegion = TextureRegion.split(texture, WIDTH, HEIGHT)[1];
		this.leftAnimation.setAnimation(leftSpriteRegion, 1/3f, leftAnimation);
		
		texture = new Texture(Gdx.files.internal("playerattack.png"));
		this.rightSwordRegion = TextureRegion.split(texture,43, 20)[0];
		this.rightSwordAnimation.setAnimation(rightSwordRegion, 1/8f, rightSwordAnimation);
		
		this.leftSwordRegion = TextureRegion.split(texture, 43, 20)[1];
		this.leftSwordAnimation.setAnimation(leftSwordRegion, 1/8f, leftSwordAnimation);
		
		currentAnimation = rightAnimation;
		
		// Testing
		this.sr = new ShapeRenderer();
		sr.setProjectionMatrix(camera.combined);
	}
	
	public void update(float dt){

		if(!alive) return;
		
		handleInput(dt);
		
		// Gravity
		velocity.y -= 400 * 2 * dt;
		collisionHandling(dt);
		
		mapWarping();

		animationHandler();
		sword.update(dt);
		block.update(dt);
		
		boundingRectangle.setPosition(position);
		
		currentAnimation.setPlaying(true);
		currentAnimation.update(dt);
		GameKeys.update();
	}
	
	public void render(SpriteBatch batch){
	
		if(!alive) return;
		
		batch.begin();
		batch.draw(currentAnimation.getFrame(), position.x - WIDTH / 3, position.y);
		batch.end();
		
		sr.begin(ShapeType.Line);
		sr.rect(position.x, position.y, 16, 16);
		sr.end();
	
		sword.render(sr);
		block.render(sr);
	}
	
	public void handleInput(float dt){
		
		if(GameKeys.isDown(GameKeys.U)){
			System.out.println("UP PRESSED");
			velocity.y = speed;
		}
		
		if(GameKeys.isDown(GameKeys.H)){
			System.out.println("LEFT PRESSED");
			velocity.x = -40;
			direction.x = -1;
			state = PlayerState.MOVING;
		}
		
		if(GameKeys.isDown(GameKeys.K)){
			System.out.println("RIGHT PRESSED");
			velocity.x = 40;
			direction.x = 1;
			state = PlayerState.MOVING;
		}
		if(GameKeys.isPressed(GameKeys.J)){
			
			if(sword.getInterval() > 0) return;
			
//			sword.action(position, direction, this);
			state = PlayerState.ATTACKING;
		}
		if(GameKeys.isPressed(GameKeys.O)){
//			block.action(position, direction, this);
			state = PlayerState.BLOCKING;
		}
		if(GameKeys.isUp(GameKeys.H) && GameKeys.isUp(GameKeys.K)){
			velocity.x = 0;
		}
	
	}
	
	private void animationHandler(){
		
		if(state == PlayerState.MOVING){
			if(direction.x > 0) currentAnimation = rightAnimation;
			else currentAnimation = leftAnimation;
		}
		if(state == PlayerState.ATTACKING){
			
			if(direction.x > 0) currentAnimation = rightSwordAnimation;
			else currentAnimation = leftSwordAnimation;
			
			if(sword.getTimer() == 0){
				currentAnimation.setCurrentFrame(0);
				state = PlayerState.MOVING;
			}
		}
	}
	
	public void mapWarping(){
		if(position.x + 16 < 0) position.x = 350;
		if(position.x > 350) position.x = 0;
		if(position.y + 15 < 0) position.y = 400;
	}
	
	public void collisionHandling(float dt){
		
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
		}
		
		position.y += velocity.y * dt * 4;
		
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
				return true;
			}
		}
		
		return false;
	}
	
	public void hit(){
		alive = false;
	}
	
	public Rectangle getBoundingRectangle(){
		return boundingRectangle;
	}
	
	public void setVelocityX(float x){
		this.velocity.x *= x;
	}
	
	public void setVelocityY(float y){
		this.velocity.y *= y;
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
	
	public void setPosition(Vector2 position){
		this.position = position;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public Vector2 getDirection(){
		return direction;
	}
	
	public Rectangle getSwordRect(){
		return sword.getBoundingBox();
	}

//	public CollisionComponent getCollisionHandler(){
//		return this.collisionHandler;
//	}
}
