package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/*
	Map size: 		640 x 480
	Character Size:	32px
	Grid layout: 20 x 15
*/
public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Sprite playerCharacter;
	private Sprite breakableWallSprite;
	private int spriteSize = 64;
	private int gridBoxNumber = 233;

	private Texture playerTexture;
	private Texture breakableWallTexture;

	private boolean[] breakWallLocations;

	private TiledMap level;
	private TiledMapRenderer levelRenderer;

	private BitmapFont font;

	private static float xPosPlayer;
	private static float yPosPlayer;

	private int winWidth;
	private int winHeight;

	private float time;
	private String timeDisplay;

	private OrthographicCamera camera;



	private static final float moveUnit = 275.0f;  //increase to 30?

	private void loadTextures(){
		level = new TmxMapLoader().load("images/sample_background.tmx");
		playerTexture = new Texture("images/placeholder_character.png");
		breakableWallTexture = new Texture("images/placeholder_enemy.png");

		levelRenderer = new OrthogonalTiledMapRenderer(level);
		playerCharacter = new Sprite(playerTexture);
		breakableWallSprite = new Sprite(breakableWallTexture);
	}
	private void loadMap() {

		for(int i = 0; i < breakWallLocations.length; i++){

		}
	}
	private void loadFont(){
		font = new BitmapFont();
		font.setColor(Color.YELLOW);
	}

	private void renderLevel(){
		camera.update();
		levelRenderer.setView(camera);
		levelRenderer.render();
		renderBreakableWalls();


	}
	private void renderBreakableWalls(){




	}
	private void renderPlayerCharacter(){

		float elapsedTime = Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			if(yPosPlayer >= winHeight - spriteSize){
				yPosPlayer = winHeight - spriteSize;
			}
			else {
				yPosPlayer += (moveUnit * elapsedTime);
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			if(yPosPlayer <= 32){
				yPosPlayer = 32;
			}
			else{
				yPosPlayer -= (moveUnit * elapsedTime);
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			if(xPosPlayer >= winWidth - spriteSize){
				xPosPlayer = winWidth - spriteSize;
			}
			else{
				xPosPlayer += (moveUnit * elapsedTime);
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A)){
			if(xPosPlayer <= 32){
				xPosPlayer = 32;
			}
			else{
				xPosPlayer -= (moveUnit * elapsedTime);
			}
		}
		playerCharacter.setPosition(xPosPlayer,yPosPlayer);
		playerCharacter.draw(batch);
	}
	private void renderTime(){
		time -= Gdx.graphics.getDeltaTime();
		if(time > 0) {
			timeDisplay = Float.toString(time);
		}
		else{
			timeDisplay = "Time's Up!";
		}
		font.draw(batch, timeDisplay, 560,465);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		breakWallLocations = new boolean[gridBoxNumber];
;
		winWidth = Gdx.graphics.getWidth();
		winHeight = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, winWidth, winHeight);
		camera.update();

		loadTextures();
		loadMap();
		loadFont();
		xPosPlayer = 32;
		yPosPlayer = 32;
		time = 99;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderLevel();

		batch.begin();
			renderPlayerCharacter();
			renderTime();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerTexture.dispose();
		level.dispose();
		font.dispose();
	}
}
