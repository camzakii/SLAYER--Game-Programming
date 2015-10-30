package com.collision.game.states;

import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.collision.game.MyGdxGame;
import com.collision.game.handler.Animation;
import com.collision.game.handler.GameStateManager;
import com.collision.game.networking.Network;
import com.esotericsoftware.kryonet.Client;

public class GameLobby extends GameState{

	private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private BitmapFont font;
    private BitmapFont samuraiFont;

    private Sprite enterIp;
    private Sprite enterName;
    private Sprite charSelect;
    private Sprite charTitle;
    private Sprite background;
    
    private TextField textFieldIP;
    private TextField textFieldName;
    
    // Testing player selection
    private Animation bluePlayer;
    private Animation redPlayer;
    private Animation player3;
    private Animation player4;
    
    private Animation currentAnimation;
    
    private TextureRegion[] texRegion;
    private TextureRegion[] texRegion2;
    private TextureRegion[] texRegion3;
    private TextureRegion[] texRegion4;
    
    private Array<Animation> animations;
    
    private int index;
    
    private OrthographicCamera playerSelectCamera;
    
	public GameLobby(GameStateManager gsm){
		super(gsm);
		
		  this.batch = new SpriteBatch();
		  this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		  this.stage = new Stage();
		  this.font = new BitmapFont(Gdx.files.internal("data/default.fnt"));
		  this.samuraiFont = new BitmapFont(Gdx.files.internal("menu_assets/white_font.fnt"));
		  
		  Texture texture = new Texture(Gdx.files.internal("menu_assets/enter_ip.png"));
		  enterIp = new Sprite(texture);
		  
		  texture = new Texture(Gdx.files.internal("menu_assets/enter_name.png"));
		  enterName = new Sprite(texture);
		  
		  
		  // Testing choose player sprite
		  
		  this.playerSelectCamera = new OrthographicCamera();
		  this.playerSelectCamera.setToOrtho(false, MyGdxGame.WIDTH / 4.5f, MyGdxGame.HEIGHT / 4.5f);
		  
		  this.bluePlayer = new Animation();
		  this.redPlayer = new Animation();
		  this.player3 = new Animation();
		  this.player4 = new Animation();
		  this.currentAnimation = new Animation();
		  
		   texture = new Texture(Gdx.files.internal("player_sprites/idle_fight_ninja1.png"));
		   this.texRegion = TextureRegion.split(texture, 43, 20)[0];
		   this.bluePlayer.setAnimation(texRegion, 1/3f, bluePlayer);
		   
		   texture = new Texture(Gdx.files.internal("player_sprites/idle_fight_ninja2.png"));
		   this.texRegion2 = TextureRegion.split(texture, 43, 20)[0];
		   this.redPlayer.setAnimation(texRegion2, 1/3f, redPlayer);
		   
		   texture = new Texture(Gdx.files.internal("player_sprites/idle_fight_ninja3.png"));
		   this.texRegion3 = TextureRegion.split(texture, 43, 20)[0];
		   this.player3.setAnimation(texRegion3, 1/3f, player3);
		   
		   texture = new Texture(Gdx.files.internal("player_sprites/idle_fight_ninja4.png"));
		   this.texRegion4 = TextureRegion.split(texture, 43, 20)[0];
		   this.player4.setAnimation(texRegion4, 1/3f, player4);
		   
		   texture = new Texture(Gdx.files.internal("menu_assets/char_select.png"));
		   this.charSelect = new Sprite(texture);
		   
		   texture = new Texture(Gdx.files.internal("menu_assets/char_title.png"));
		   this.charTitle = new Sprite(texture);
		   
		   texture = new Texture(Gdx.files.internal("menu_assets/lobby_bg.png"));
		   this.background = new Sprite(texture);
		   
		   this.animations = new Array<Animation>();
		   animations.add(bluePlayer);
		   animations.add(redPlayer);
		   animations.add(player3);
		   animations.add(player4);
		   
		   index = 0;
		   currentAnimation = bluePlayer;
		   
		  initComponents();
	        
		  Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void update(float dt) {
		setAnimation();
		handleInput();
		
		currentAnimation.setPlaying(true);
		currentAnimation.update(dt);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(background, -40, 0);
		
		batch.draw(enterIp, Gdx.graphics.getWidth() / 2 - 400, 121, 100, 30);
		batch.draw(enterName, Gdx.graphics.getWidth() / 2 - 400, 180, 100, 30);
		
		batch.draw(charTitle, Gdx.graphics.getWidth() / 2 - 90, 380);
		batch.draw(charSelect, Gdx.graphics.getWidth() / 2 - 60, 50);
		
		batch.setProjectionMatrix(playerSelectCamera.combined);
		batch.draw(currentAnimation.getFrame(), 35, 20);
		
		batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Keys.A)){
			index--;
		}
		if(Gdx.input.isKeyJustPressed(Keys.D)){
			index++;
		}
	}

	private void setAnimation(){
		if(index < 0) index = animations.size - 1;
		
		if(index > animations.size - 1)index = 0;
		
		currentAnimation = animations.get(index);
		
	}
	
	public void initComponents(){
		
		final TextButton buttonFind = new TextButton("Find Game", skin, "default");
		buttonFind.setWidth(200f);
		buttonFind.setHeight(20f);
		buttonFind.setPosition(Gdx.graphics.getWidth() /2 - 300f, Gdx.graphics.getHeight()/2 + 70f);
		
		final TextButton buttonJoin = new TextButton("Join Game", skin, "default");
		buttonJoin.setWidth(200f);
		buttonJoin.setHeight(20f);
		buttonJoin.setPosition(Gdx.graphics.getWidth() /2 - 300f, Gdx.graphics.getHeight()/2 + 130f);
		
		final TextButton buttonHost = new TextButton("Host Game", skin, "default");
		buttonHost.setWidth(200f);
		buttonHost.setHeight(20f);
		buttonHost.setPosition(Gdx.graphics.getWidth() /2 - 300f, Gdx.graphics.getHeight()/2 + 200f);
		
		textFieldIP = new TextField("localhost", skin, "default");
		textFieldIP.setPosition(Gdx.graphics.getWidth() / 2 - 250f, 150f);
		
		textFieldName = new TextField("", skin, "default");
		textFieldName.setPosition(Gdx.graphics.getWidth() / 2 - 250f , 230f);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = samuraiFont;
		
		
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
		gsm.setState(gsm.PLAY, false, textFieldIP.getText(), getName(), index);
	}
	
	private void host(){
		gsm.setState(gsm.PLAY, true, "localhost", getName(), index);
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
