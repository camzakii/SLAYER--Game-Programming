package com.multiplayergame.gp.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.multiplayergame.gp.abilities.Block;
import com.multiplayergame.gp.abilities.Sword;
import com.multiplayergame.gp.entities.Player;

public class PlayerController {

	public static enum PlayerState{
		IDLE,
		ATTACKING,
		MOVING,
		JUMPING,
		DASHING,
		BLOCKING
	}

	private static final int ATTACK_SPEED = 14;
	private static final float SPEED = 0.5f;
	private static final float DASH = 6f;
	
	private Body body;
	private Vector2 direction;
	private PlayerState state;
	
	private Sword sword;
	private Player player;
	private Block block;
	
	private float width;
	private float height;
	
	private float horizontal_velocity;
	private float vertical_velocity;
	
	private TextureRegion[] attack_right_sprites;
	private TextureRegion[] attack_left_sprites;
	private TextureRegion[] idle_right_sprites;
	private TextureRegion[] idle_left_sprites;
	private TextureRegion[] block_right_sprites;
	private TextureRegion[] block_left_sprites;
	
	private Animation attack_right;
	private Animation attack_left;
	private Animation idle_right;
	private Animation idle_left;
	private Animation currentAnimation;
	private Animation block_right;
	private Animation block_left;
	
	public PlayerController(Player player, Body body, Sword sword, Block block){
		
		this.direction = new Vector2(1, 0);
		this.body = body;
		this.sword = sword;
		this.player = player;
		this.block = block;
		
		this.attack_right = new Animation();
		this.attack_left = new Animation();
		this.idle_right = new Animation();
		this.idle_left = new Animation();
		this.block_right = new Animation();
		
		Texture tex = new Texture(Gdx.files.internal("attack_fight_ninja1_extended.png"));
		this.attack_right_sprites = TextureRegion.split(tex, 50, 30)[0];
		this.attack_right.setAnimation(attack_right_sprites, 1/11f, attack_right);
	
		this.attack_left_sprites = TextureRegion.split(tex, 50, 30)[1];
		this.attack_left.setAnimation(attack_left_sprites, 1/11f, attack_left);

		tex = new Texture(Gdx.files.internal("idle_fight_ninja1.png"));
		this.idle_right_sprites = TextureRegion.split(tex, 43, 20)[0];
		this.idle_right.setAnimation(idle_right_sprites, 1/3f, idle_right);

		this.idle_left_sprites = TextureRegion.split(tex, 43, 20)[1];
		this.idle_left.setAnimation(idle_left_sprites, 1/3f, idle_left);
		
		tex = new Texture(Gdx.files.internal("block_fat_fighter.png"));
		this.block_right_sprites = TextureRegion.split(tex, 43, 20)[0];
		this.block_right.setAnimation(block_right_sprites, 1/3f, block_right);
		
		this.currentAnimation = idle_right;
		
		this.width = attack_right_sprites[0].getRegionWidth();
		this.height = attack_right_sprites[0].getRegionHeight();
	}
	
	public void update(float dt){
		
		block.update(dt);
		sword.update(dt);
		
		currentAnimation.setPlaying(true);
		currentAnimation.update(dt);
		
		if(sword.getAttackTimer() <= 0 && state == PlayerState.ATTACKING){
			currentAnimation.setCurrentFrame(0);
			setIdleAnimation();
		}
		
		if(block.getTimer() <= 0 && state == PlayerState.BLOCKING){
			currentAnimation.setCurrentFrame(0);
			setIdleAnimation();
		}
		
		body.applyForceToCenter(horizontal_velocity * SPEED, vertical_velocity, true);
		
		System.out.println("State: " + state);
		
	}
	
	public void render(SpriteBatch batch){
		
		batch.begin();
		batch.draw(currentAnimation.getFrame(), 
				body.getPosition().x * 100 - width / 2, 
				body.getPosition().y * 104 - height / 2);
		batch.end();
	}
	
	public void input(){
		
		horizontal_velocity = 0;
		vertical_velocity = -0.7f;
		
		if(GameKeys.isPressed(GameKeys.W)){
			vertical_velocity = 15;
			state = PlayerState.JUMPING;
		}
		
		if(GameKeys.isDown(GameKeys.A)){
			horizontal_velocity = -1;
			direction.x = -1;
			if(state != PlayerState.ATTACKING) {
				state = PlayerState.MOVING;
				setIdleAnimation();
			}
		}
		
		if(GameKeys.isDown(GameKeys.D)){
			horizontal_velocity = 1;
			direction.x = 1;
			if(state != PlayerState.ATTACKING) {
				state = PlayerState.MOVING;
				setIdleAnimation();
			}
		}
		
		if(GameKeys.isPressed(GameKeys.SPACE)){
			horizontal_velocity = direction.x * DASH;
			state = PlayerState.DASHING;
		}
		
		if(GameKeys.isPressed(GameKeys.S)){
			if(state != PlayerState.BLOCKING) swordAction();
		}
		else if(GameKeys.isPressed(GameKeys.O)){
			currentAnimation = block_right;
			if(state != PlayerState.ATTACKING) blockAction();
			state = PlayerState.BLOCKING;
		}
	}
	
	private void swordAction(){
		Vector2 pos;
		
		if(direction.x == 1) {
			pos = new Vector2(body.getPosition().x + 0.2f, body.getPosition().y);
		}
		else {
			pos = new Vector2(body.getPosition().x - 0.2f, body.getPosition().y);
		}
		
		setAttackAnimation();
		sword.action(pos);
		state = PlayerState.ATTACKING;
	}
	
	private void blockAction(){
		 Vector2 pos;
		 
		if(direction.x == 1) {
				pos = new Vector2(body.getPosition().x + 0.2f, body.getPosition().y);
		}
		else {
				pos = new Vector2(body.getPosition().x - 0.2f, body.getPosition().y);
		}
		block.action(pos);
	}
	
	public void setAttackAnimation(){
		if(direction.x == 1) currentAnimation = attack_right;
		else currentAnimation = attack_left;
	}
	
	public void setIdleAnimation(){
		if(direction.x == 1) currentAnimation = idle_right;
		else currentAnimation = idle_left;
		
		state = PlayerState.IDLE;
	}
	
	public Animation getCurrentAnimation(){
		return currentAnimation;
	}
	
	public Vector2 getDirection(){
		return direction;
	}
	
	public boolean isMoving(){
		return vertical_velocity > 0;
	}
	
}
