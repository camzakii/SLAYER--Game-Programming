package com.collision.game.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class GameLevel {

	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private TiledMapTileLayer layer;
	
	private float tileSize;
	private int levelWidth;
	private int levelHeight;
	
	public GameLevel(OrthographicCamera camera){
		this.camera = camera;
	
		this.tileMap = new TmxMapLoader().load("level/level_1.tmx");
		this.renderer = new OrthogonalTiledMapRenderer(tileMap);
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("layer1");
		this.tileSize = layer.getTileWidth();
		this.levelWidth = tileMap.getProperties().get("width", Integer.class);
		this.levelHeight = tileMap.getProperties().get("height", Integer.class);

	}
	
	public void render(){
		renderer.setView(camera);
		renderer.render();
	}
	
	public boolean isCellBlocked(float x, float y){
		
		Cell cell = layer.getCell((int) (x / layer.getTileWidth()), (int) (y / layer.getTileHeight()));
		if(cell != null && 
				cell.getTile() != null &&
				cell.getTile().getProperties().containsKey("blocked")){
			return true;
		}
		return false;
	}
	
	public TiledMapTileLayer getLayer(){
		return layer;
	}
	
}
