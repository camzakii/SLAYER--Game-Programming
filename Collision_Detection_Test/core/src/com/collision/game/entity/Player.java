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
import com.badlogic.gdx.utils.Array;
import com.collision.game.ability.Block;
import com.collision.game.ability.Shuriken;
import com.collision.game.ability.Sword;
import com.collision.game.handler.Animation;
import com.collision.game.handler.CollisionComponent;
import com.collision.game.handler.GameKeys;

public class Player extends PlayerEntity {

	private static final int WIDTH = 43;
	private static final int HEIGHT = 20;
	
	private GameLevel level;
	
	private Rectangle boundingRectangle;	
	private Vector2 direction;
	private Vector2 position;

	private Vector2 velocity;

	private Block block;
	private Sword sword;
	private Array<Shuriken> shurikens;
	private PlayerState state;
	
	private boolean alive;
	
	private TextureRegion[] rightSpriteRegion;
	private TextureRegion[] leftSpriteRegion;
	private TextureRegion[] leftSwordRegion;
	private TextureRegion[] rightSwordRegion;
	private TextureRegion[] leftBlockRegion;
	private TextureRegion[] rightBlockRegion;
	
	private Animation rightAnimation;
	private Animation leftAnimation;
	private Animation leftSwordAnimation;
	private Animation rightSwordAnimation;
	private Animation blockLeftAnimation;
	private Animation blockRightAnimation;
	
	private Animation currentAnimation;

	// Testing
	private ShapeRenderer sr;
	private TiledMapTileLayer layer;
	
	public Player(OrthographicCamera camera, GameLevel level){
		
		this.boundingRectangle = new Rectangle(100, 100, 16, 16);
		
		this.position = new Vector2(100, 100);
		this.direction = new Vector2(1, 0);
		this.level = level;
		this.layer = level.getLayer();
		
		this.shurikens = new Array<Shuriken>();
		this.alive = true;
		this.state = PlayerState.IDLE;
		
		this.sword = new Sword();
		this.block = new Block();
		
		this.velocity = new Vector2();
		
		this.rightAnimation = new Animation();
		this.leftAnimation = new Animation();
		this.currentAnimation = new Animation();
		this.leftSwordAnimation = new Animation();
		this.rightSwordAnimation = new Animation();
		this.blockLeftAnimation = new Animation();
		this.blockRightAnimation = new Animation();
		
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
		
		texture = new Texture(Gdx.files.internal("playerblock.png"));
		this.leftBlockRegion = TextureRegion.split(texture, WIDTH, HEIGHT)[1];
		this.blockLeftAnimation.setAnimation(leftBlockRegion, 1/8f, blockLeftAnimation);
		
		this.rightBlockRegion = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
		this.blockRightAnimation.setAnimation(rightBlockRegion, 1/8f, blockRightAnimation);
		
		currentAnimation = rightAnimation;
		
		// Testing
		this.sr = new ShapeRenderer();
		sr.setProjectionMatrix(camera.combined);
	}
	
	public void update(float dt){

		if(!alive) return;
		
		// Gravity
		velocity.y -= 400 * 2 * dt;
		collisionHandling(dt);
		
		mapWarping();

		animationHandler();
		sword.update(dt);
		block.update(dt);
		for(Shuriken shuriken: shurikens) shuriken.update(dt);
		
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
		for(Shuriken shuriken: shurikens) shuriken.render(batch, sr);
	}
	
	private void animationHandler(){
		
		if(state == PlayerState.MOVING){
			
			if(sword.getTimer() > 0) return;
			
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
		if(state == PlayerState.BLOCKING){
			
			if(direction.x > 0) currentAnimation = blockRightAnimation;
			else currentAnimation = blockLeftAnimation;
			
			if(block.getTimer() == 0){
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
				return true;
			}
		}
		
		return false;
	}
	
	// Player Actions
	
	public void jump(){
		velocity.y = 150;
	}
	
	public void moveLeft(){
			
		velocity.x = -40;
		direction.x = -1;

		if(sword.getTimer() > 0) state = PlayerState.ATTACKING;
		else state = PlayerState.MOVING;
		
		if(block.getTimer() > 0) state = PlayerState.BLOCKING;
		else state = PlayerState.MOVING;
	}
	
	public void moveRight(){
		
		velocity.x = 40;
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
		state = PlayerState.ATTACKING;
	}
	
	public void parryAction(){
		
		if(sword.getTimer() > 0) return;
		if(block.getBlockTimer() > 0) return;
		
		block.action(position, direction, this);
		state = PlayerState.BLOCKING;
	}
	
	public void shurikenAction(){
		Vector2 dir = new Vector2(direction.x, 0);
		Vector2 pos = new Vector2(position.x, position.y);
		Shuriken shuriken = new Shuriken();
		shuriken.action(dir, pos);
		shurikens.add(shuriken);
	}
	
	public Rectangle getBoundingRectangle(){
		return boundingRectangle;
	}
	
	public void hit(){
		alive = false;
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

	public Array<Shuriken> getShurikens(){
		return shurikens;
	}
	
	public Sword getSword(){
		return sword;
	}
	
	public Rectangle getSwordRect(){
		return sword.getBoundingBox();
	}

	public PlayerState getState(){
		return state;
	}
	
	public void setState(PlayerState moving){
		this.state = moving;
	}
	
}
