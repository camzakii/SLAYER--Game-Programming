package com.collision.game.states;

import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.collision.game.handler.GameStateManager;
import com.collision.game.networking.Network;
import com.esotericsoftware.kryonet.Client;

public class GameLobby extends GameState{

	private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private BitmapFont font;

    private TextField textFieldIP;
    private TextField textFieldName;
    
	public GameLobby(GameStateManager gsm){
		super(gsm);
		
		  this.batch = new SpriteBatch();
		  this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		  this.stage = new Stage();
		  this.font = new BitmapFont(Gdx.files.internal("data/default.fnt"));
		  
		  initComponents();
	        
		  Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void update(float dt) {
		
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void handleInput() {
		
	}

	public void initComponents(){
		
		LabelStyle  style = new LabelStyle(font, Color.RED);
		Label label = new Label("SLAYER", style);
		label.setFontScale(9);
		label.setPosition(100, 350);
		
		final Label labelTitle = new Label("SLAYER", skin);
		labelTitle.setPosition(200f, 300f);
		labelTitle.setColor(Color.RED);
		
		final TextButton buttonFind = new TextButton("Find Game", skin, "default");
		buttonFind.setWidth(200f);
		buttonFind.setHeight(20f);
		buttonFind.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 + 70f);
		
		final TextButton buttonJoin = new TextButton("Join Game", skin, "default");
		buttonJoin.setWidth(200f);
		buttonJoin.setHeight(20f);
		buttonJoin.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 + 130f);
		
		final TextButton buttonHost = new TextButton("Host Game", skin, "default");
		buttonHost.setWidth(200f);
		buttonHost.setHeight(20f);
		buttonHost.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 + 200f);
		
		textFieldIP = new TextField("localhost", skin, "default");
		textFieldIP.setPosition(200f, 50f);
		
		textFieldName = new TextField("", skin, "default");
		textFieldName.setPosition(200f, 100f);
		
		buttonFind.addListener(new ClickListener(){
			@Override 
			public void clicked(InputEvent event, float x, float y){
				System.out.println("find game");
				find();
			}
		});
		
		buttonJoin.addListener(new ClickListener(){
			@Override 
			public void clicked(InputEvent event, float x, float y){
				System.out.println("join game");
				join();
			}
		});
		
		buttonHost.addListener(new ClickListener(){
			@Override 
			public void clicked(InputEvent event, float x, float y){
				System.out.println("host game");
				host();
			}
		});
	        
		stage.addActor(buttonFind);
		stage.addActor(buttonJoin);
		stage.addActor(buttonHost);
		stage.addActor(textFieldName);
		stage.addActor(textFieldIP);
//		stage.addActor(labelTitle);
		stage.addActor(label);
	}
	
	private void find(){
		Client client = new Client();
		client.start();
		InetAddress found = client.discoverHost(Network.UDP, 5000);
		if(found != null) textFieldIP.setText(found.getHostAddress().toString());
		client.stop();
		client.close();
	}
	
	private void join(){
		gsm.setState(gsm.PLAY, false, textFieldIP.getText(), getName());
	}
	
	private void host(){
		gsm.setState(gsm.PLAY, true, "localhost", getName());
	}
	
	private String getName(){
		String name = textFieldName.getText();
		if(name.isEmpty()){
			name = "Guest" + Math.random() + 10000; // default name
		}
		textFieldName.setText(name);
		return name;
	}
	
	@Override
	public void dispose() {
		
	}
}
