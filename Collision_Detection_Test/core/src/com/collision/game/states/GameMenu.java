package com.collision.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.collision.game.handler.GameStateManager;

public class GameMenu extends GameState {

	private Stage stage;
	private BitmapFont font;
	private BitmapFont black_font;
	private TextureAtlas atlas;
	private Skin skin;
	private SpriteBatch batch;
	private Sprite titleSprite;
	
	private TextButton startButton;
	private TextButton quitButton;
	
	public GameMenu(GameStateManager gsm){
		super(gsm);
		
		this.stage = new Stage();
		this.batch = new SpriteBatch();
		
		this.atlas = new TextureAtlas("menu_assets/button.pack");
		this.skin = new Skin(atlas);
		
		this.font = new BitmapFont(Gdx.files.internal("menu_assets/white_font.fnt"), false);
		this.black_font = new BitmapFont(Gdx.files.internal("menu_assets/menu_font.fnt"), false);
		
		initMenu();
		
		Gdx.input.setInputProcessor(stage);
	}

	private void initMenu() {
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button.press");
		textButtonStyle.down = skin.getDrawable("button.press");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetX = -1;
		textButtonStyle.font = black_font;
		
		this.startButton = new TextButton("Play", textButtonStyle);
		this.startButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				gsm.setState(gsm.LOBBY, false, "", "");
			}
		});
		this.startButton.setPosition(100, 100);
		
		Texture texture = new Texture(Gdx.files.internal("menu_assets/title.png"));
		this.titleSprite = new Sprite(texture);
		
		this.stage.addActor(startButton);
	}

	@Override
	public void update(float dt) {
		
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(titleSprite, 150, 300, 600, 250);
		batch.end();
		
		stage.act();
		stage.draw();
	}

	@Override
	public void handleInput() {
		
	}

	@Override
	public void dispose() {
		
	}
	
}
