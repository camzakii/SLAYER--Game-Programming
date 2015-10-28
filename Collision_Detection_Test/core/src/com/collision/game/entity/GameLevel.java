package com.collision.game.entity;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class GameLevel {

	private static final Vector2 SPAWN_1 = new Vector2(300, 250);
	private static final Vector2 SPAWN_2 = new Vector2(100, 150);
	private static final Vector2 SPAWN_3 = new Vector2(330, 100);
	private static final Vector2 SPAWN_4 = new Vector2(100, 250);
	private ArrayList<Vector2> spawns = new ArrayList<Vector2>();
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private TiledMapTileLayer layer;
	
	private float tileSize;
	private int levelWidth;
	private int levelHeight;
	
	public GameLevel(OrthographicCamera camera){
		this.camera = camera;
	
		spawns.add(SPAWN_1);
		spawns.add(SPAWN_2);
		spawns.add(SPAWN_3);
		spawns.add(SPAWN_4);
		
		this.tileMap = new TmxMapLoader().load("level/level_3.tmx");
		this.renderer = new OrthogonalTiledMapRenderer(tileMap);
		
		this.layer = (TiledMapTileLayer) tileMap.getLayers().get("a");
		
		this.tileSize = layer.getTileWidth();
		this.levelWidth = tileMap.getProperties().get("width", Integer.class);
		this.levelHeight = tileMap.getProperties().get("height", Integer.class);

	}
	
	public void render(){
		renderer.setView(camera);
		renderer.render();
	}
	
	public boolean isCellBlocked(float x, float y){
		
		Cell cell = this.layer.getCell((int) (x / this.layer.getTileWidth()), (int) (y / this.layer.getTileHeight()));
		if(cell != null && cell.getTile() != null){
			return true;
		}
		return false;
	}
	
	public TiledMapTileLayer getLayer(){
		return layer;
	}
	
	public Vector2 randomSpawn(Map<Integer, Player> players){
		
		TreeMap<Float, Vector2> distanceSpawns = new TreeMap<Float, Vector2>();
		
		for(Player player: players.values()){
			
			if(players.isEmpty()) System.out.println("NO PLAYERS!");
			
			for(Vector2 spawnPosition: spawns){
				
				if(spawnPosition == null) System.out.println("SPAWN POSITION IS NULL");
				
				float x = player.getPosition().x - spawnPosition.x;
				x *= x;
				
				float y = player.getPosition().y - spawnPosition.y;
				y *= y;
				
				distanceSpawns.put((float) Math.sqrt(x + y), spawnPosition);
			}
		}
		
		if(distanceSpawns.isEmpty()) {
			System.out.println("IS EMPTY!");
			return SPAWN_1;
		}
		
		return distanceSpawns.lastEntry().getValue();
	}
	
}
