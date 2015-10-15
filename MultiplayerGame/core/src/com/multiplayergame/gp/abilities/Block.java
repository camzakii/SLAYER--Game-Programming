package com.multiplayergame.gp.abilities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.multiplayergame.gp.entities.Player;
import com.multiplayergame.gp.utils.BodyFactory;
import com.multiplayergame.gp.utils.Vars;

public class Block {

	private Body body;
	private Array<Body> bodies;
	private World world;
	private Player player;
	
	private Vector2 position;
	
	private float timer;
	private float bodyTimer;
	
	public Block(World world, Player player){
		this.timer = 0;
		this.bodyTimer = 0;
		this.bodies = new Array<Body>();
		this.world = world;
		this.player = player;
		this.position = new Vector2(0, 0);
	}
	
	public void update(float dt){
		if(timer > 0) timer--;
		bodyTimer --;
		
		if(body != null){
			if(player.getDirection().x == 1){
				body.setTransform(player.getPosition().x + 0.19f, player.getPosition().y, 0);
				position.set(player.getPosition().x + 0.19f, player.getPosition().y);
			}else{
				body.setTransform(player.getPosition().x - 0.19f, player.getPosition().y, 0);
				position.set(player.getPosition().x - 0.19f, player.getPosition().y);
			}
		}
		
		removeBodies();
	}
	
	public void action(Vector2 position){
		if(timer <= 0 && bodyTimer <= 0){
			body = BodyFactory.createBody(world, position.x, position.y, 
					5, 20, 2, Vars.BIT_BLOCK, Vars.BIT_ENEMY, true, "block");
			bodies.add(body);
			timer = 40;
			bodyTimer = 20;
		}
	}
	
	public void removeBodies(){
		if(bodyTimer <= 0 && bodies.size > 0){
			world.destroyBody(bodies.get(0));
			bodies.removeIndex(0);
		}
	}
	
	public float getTimer(){
		return timer;
	}
	
}
