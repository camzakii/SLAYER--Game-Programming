package com.multiplayergame.gp.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Animation {
	
	private TextureRegion[] frames;
	private float time;
	private float delay;
	private int currentFrame;
	private int timesPlayed;
	private boolean playing;
	
	private Vector2 position;
	
	public Animation(){
		
	}
	
	public Animation(TextureRegion[] frames){
		this(frames, 1/12f);
		this.position = new Vector2(0, 0);
	}
	
	public Animation(TextureRegion[] frames, float delay){
		setFrames(frames, delay);
	}
	
	public void setFrames(TextureRegion[] frames, float delay){
		this.playing = false;
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	
	public void update(float dt){
		if(playing){
			if(delay <= 0) return;
			time += dt;
			while(time >= delay){
				step();
			}
		}else{
			currentFrame = 1;
		}
	}
	
	public void step(){
		time -= delay;
		currentFrame++;
		if(currentFrame == frames.length){
			currentFrame = 0;
			timesPlayed++;
		}
	}
	
	public void setAnimation(TextureRegion[] region, float d, Animation anim){
		anim.setFrames(region, d);
	}
	
	public int getCurrentFrame(){ 
		return currentFrame; 
	}
	
	public void setCurrentFrame(int currentFrame){
		this.currentFrame = currentFrame;
	}
	
	public int getFrameLength(){ 
		return frames.length; 
	}
	
	public TextureRegion getFrame(){ 
		return frames[currentFrame]; 
	}
	
	public int getTimesPlayed(){ 
		return timesPlayed; 
	}
	
	public void setPlaying(boolean playing){ 
		this.playing = playing; 
	}
	
	public boolean isPlaying(){ 
		return playing; 
	}
	
	public void setPosition(Vector2 position){ 
		this.position = position; 
	}
	
	public Vector2 getPosition(){ 
		return this.position; 
	}
}
