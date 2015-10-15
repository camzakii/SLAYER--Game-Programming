package com.collision.game.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Particle {

	private float x;
	private float y;
	private int life;
	private int yvel;
	private int xvel;
	
	private Sprite sprite;
	
	public Particle(Sprite sprite, float x, float y, int life, int xvel, int yvel){
		
		this.sprite = sprite;
		
		this.x = x;
		this.y = y;
		this.life = life;
		this.xvel = xvel;
		this.yvel = yvel;
	}
	
	public void update(){
		y += yvel;
//		x += xvel;
		life--;
	}
	
	public void render(SpriteBatch batch){
		batch.begin();
		batch.draw(sprite, x, y);
		batch.end();
	}
	
	public int getLife(){
		return life;
	}
	
}
