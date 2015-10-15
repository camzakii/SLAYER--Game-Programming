package com.multiplayergame.gp.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.multiplayergame.gp.entities.Enemy;
import com.multiplayergame.gp.entities.Player;

public class CustomContactListener implements ContactListener {

	private Array<Body> removeBodies;
	private Array<Body> attackBodies;
	private boolean attacked;
	
	public CustomContactListener(){
		super();
		this.removeBodies = new Array<Body>();
		this.attackBodies = new Array<Body>();
	}
	
	@Override
	public void beginContact(Contact c) {
		
		final Fixture fa = c.getFixtureA();
		final Fixture fb = c.getFixtureB();
		
		if((fa.getUserData() != null && fa.getUserData().equals("enemy")) &&
				(fb.getUserData() != null && fb.getUserData().equals("player"))){

			Player player = (Player) fb.getBody().getUserData();
			Enemy enemy = (Enemy) fa.getBody().getUserData();
			
			if(enemy.getAttackTimer() <= 0) {
				player.removeLife();
				enemy.restartAttackTimer();
			}
			
			if(!player.isAlive()) removeBodies.add(fb.getBody());
			
		}
		
		if((fa.getUserData() != null && fa.getUserData().equals("player")) &&
				(fb.getUserData() != null && fb.getUserData().equals("enemy"))){

			Player player = (Player) fa.getBody().getUserData();
			Enemy enemy = (Enemy) fb.getBody().getUserData();
			
			if(enemy.getAttackTimer() <= 0) {
				player.removeLife();
				enemy.restartAttackTimer();
			}
			
			if(!player.isAlive()) removeBodies.add(fa.getBody());
			
		}
		
		if((fa.getUserData() != null && fa.getUserData().equals("enemy")) &&
				(fb.getUserData() != null && fb.getUserData().equals("attack"))){
			
			Enemy enemy = (Enemy) fa.getBody().getUserData();
			enemy.removeLife();
			
			if(!enemy.isAlive()) {
				removeBodies.add(fa.getBody());
			} 
			
			attacked = true;
		}
		
		if((fa.getUserData() != null && fa.getUserData().equals("attack")) &&
				(fb.getUserData() != null && fb.getUserData().equals("enemy"))){
			
			Enemy enemy = (Enemy) fb.getBody().getUserData();
			enemy.removeLife();
			
			if(!enemy.isAlive()) {
				removeBodies.add(fb.getBody());
			}
			
			attacked = true;
		}
	}

	@Override
	public void endContact(Contact c) {
		
	}

	@Override
	public void preSolve(Contact c, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact c, ContactImpulse impulse) {
		
	}

	public boolean getAttacked(){
		return attacked;
	}
	
	public void setAttacked(boolean bool){
		attacked = bool;
	}
	
}
