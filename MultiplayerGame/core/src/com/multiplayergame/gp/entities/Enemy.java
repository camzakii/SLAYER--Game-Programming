package com.multiplayergame.gp.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.multiplayergame.gp.handlers.Animation;
import com.multiplayergame.gp.utils.MathHelper;

public class Enemy extends Entity{

	private Body body;
	
	private TextureRegion[] idle_left_sprites;
	private Animation idle_left;
	
	private int life;
	private int attackTimer;
	
	private float width;
	private float height;
	
	private OrthographicCamera camera;
	private ShapeRenderer sr;
	private Vector2 pos;
	private Vector2 pos2;
	private boolean chase;
	
	public Enemy(Body body, OrthographicCamera camera){
		this.body = body;
		this.body.setUserData(this);
		this.life = 5;
		this.chase = false;
		this.attackTimer = 0;
	
		this.camera = camera;
		this.sr = new ShapeRenderer();
		
		this.idle_left = new Animation();
		
		Texture tex = new Texture(Gdx.files.internal("idle_fight_ninja2.png"));
		this.idle_left_sprites = TextureRegion.split(tex, 43, 20)[1];
		this.idle_left.setAnimation(idle_left_sprites, 1/2f, idle_left);
		this.idle_left.setPlaying(true);
		
		this.width = idle_left_sprites[0].getRegionWidth();
		this.height = idle_left_sprites[0].getRegionHeight();
		
	}
	
	@Override
	public void update(float dt) {
		
		attackTimer--;
		idle_left.update(dt);
		
	}

	@Override
	public void render(SpriteBatch batch) {
		
		batch.begin();
		batch.draw(idle_left.getFrame(),
				body.getPosition().x * 100 - width / 2,
				body.getPosition().y * 100 - height / 2);
		batch.end();
		
	}

	public boolean isAlive(){
		if(life > 0) return true;
		return false;
	}
	
	public void removeLife(){
		life--;
	}
	
	public int getLife(){
		return life;
	}
	
	public Body getBody(){
		return body;
	}
	
	public int getAttackTimer(){
		return attackTimer;
	}
	
	public void restartAttackTimer(){
		attackTimer = 40;
	}
}
