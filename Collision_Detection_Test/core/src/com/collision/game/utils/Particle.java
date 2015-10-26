package com.collision.game.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Particle {

	private float x;
	private float y;
	private int life;
	private int yvel;
	private int xvel;
	private boolean xaxis;
	private boolean yaxis;
	
	private Sprite sprite;
	
	public Particle(Sprite sprite, float x, float y, int life, int xvel, int yvel, boolean xaxis, boolean yaxis){
		
		this.sprite = sprite;
		
		this.x = x;
		this.y = y;
		this.life = life;
		this.xvel = xvel;
		this.yvel = yvel;
		this.xaxis = xaxis;
		this.yaxis = yaxis;
	}
	
	public void update(){
		if(yaxis) y += yvel;
		else y -= yvel;
		
		if(xaxis) x += xvel;
		else x -= xvel;
		
		life--;
	}
	
	public void render(SpriteBatch batch){
		batch.begin();
		batch.draw(sprite, x - 20, y + 20);
		batch.end();
	}
	
	public int getLife(){
		return life;
	}
	
}
