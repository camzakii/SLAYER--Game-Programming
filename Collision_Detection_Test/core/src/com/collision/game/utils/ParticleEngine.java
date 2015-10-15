package com.collision.game.utils;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ParticleEngine {

	private static final int NUM_PARTICLES = 10;
	
	private ArrayList<Particle> particles;
	private Random random;
	private float x;
	private float y;
	
	public ParticleEngine(){
		this.particles = new ArrayList<Particle>();
		this.random = new Random();
		
	}
	
	public void update(){
		
		for(int i = 0; i < particles.size(); i++){
			if(particles.get(i).getLife() > 0){
				particles.get(i).update();
			}else{
				particles.remove(i);
			}
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).render(batch);
		}
	}
	
	public void createParticles(float x1, float y1){
		
		this.x = x1 + 22;
		this.y = y1 - 32 / 2;
		
		Texture texture = new Texture(Gdx.files.internal("engine_particle.png"));
		Texture texture_1 = new Texture(Gdx.files.internal("engine_particle_2.png"));
		
		Sprite sprite = new Sprite(texture_1);
		Sprite sprite2 = new Sprite(texture);
		
		for(int i = 0; i <= NUM_PARTICLES; i++){
			
			int randTex = random.nextInt(2);
			
			Sprite spr = new Sprite();
			
			if(randTex == 1) spr = sprite;
			else spr = sprite2;
			
			int randVelX = random.nextInt(2) + 4;
			int randVelY = random.nextInt(2) + 4;
			
			int randX = random.nextInt(2);
			int randY = random.nextInt(2);
			
			int randLife = random.nextInt(20) + 10;
			
			x -= randX;
			y += randY;
					
			Particle particle = new Particle(spr, x, y, randLife, randVelX, randVelY);
			particles.add(particle);
		}
		
	}
	
}