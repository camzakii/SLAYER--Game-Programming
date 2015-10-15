package com.multiplayergame.gp.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {

	public static Body createBody(World world, float x, float y, float width, float height, int bodyType, short category, short maskBits, boolean isSensor, String data){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / Vars.UNITS, y / Vars.UNITS);
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 10f;
		
		switch(bodyType){
		case 1:
			bodyDef.type = BodyDef.BodyType.DynamicBody;
			break;
		case 2:
			bodyDef.type = BodyDef.BodyType.KinematicBody;
			break;
		case 3:
			bodyDef.type = BodyDef.BodyType.StaticBody;
			break;
		}
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 / Vars.UNITS, height / 2 / Vars.UNITS);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1;
		fixtureDef.filter.categoryBits = category;
		fixtureDef.filter.maskBits = (short) (maskBits | Vars.BIT_ENEMY);
		fixtureDef.isSensor = isSensor;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData(data);
		
		return body;
	}
	
	public static Body createEnemyBody(World world, float x, float y, float width, float height, int bodyType){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / Vars.UNITS, y / Vars.UNITS);
		bodyDef.linearDamping = 0;
		bodyDef.fixedRotation = true;
		
		switch(bodyType){
		case 1:
			bodyDef.type = BodyDef.BodyType.DynamicBody;
			break;
		case 2:
			bodyDef.type = BodyDef.BodyType.KinematicBody;
			break;
		case 3:
			bodyDef.type = BodyDef.BodyType.StaticBody;
			break;
		}
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 / Vars.UNITS, height / 2 / Vars.UNITS);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1000;
		fixtureDef.filter.categoryBits = Vars.BIT_ENEMY;
		fixtureDef.filter.maskBits = Vars.BIT_GROUND | Vars.BIT_ABILITY | Vars.BIT_PLAYER;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData("enemy");
		
		return body;
		
	}
}
