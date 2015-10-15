package com.multiplayergame.gp.abilities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.multiplayergame.gp.entities.Player;
import com.multiplayergame.gp.utils.BodyFactory;
import com.multiplayergame.gp.utils.Vars;

public class Sword {

	private World world;
	private int timer;
	private int attackTimer;
	private Array<Body> bodies;
	private Player player;
	private Body body;
	private Vector2 position;
	
	public Sword(World world, Player player){
		this.world = world;
		this.timer = 0;
		this.attackTimer = 0;
		this.bodies = new Array<Body>();
		this.player = player;
		this.position = new Vector2(0,0);
	}
	
	public void update(float dt){
		timer --;
		if(attackTimer > 0) attackTimer--;
		
		if(body != null) {
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
		if(timer <= 0 && attackTimer <= 0){
			body = BodyFactory.createBody(world, position.x * 100, 
					position.y * 100, 20, 5, 2, Vars.BIT_ABILITY, 
					Vars.BIT_ENEMY, true, "attack");
			bodies.add(body);
			timer = 30;
			attackTimer = 20;
		}
	}
	
	public void removeBodies(){
		if(attackTimer <= 0 && bodies.size > 0){
			world.destroyBody(bodies.get(0));
			bodies.removeIndex(0);
		}
	}
	
	public int getAttackTimer(){
		return attackTimer;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
}
