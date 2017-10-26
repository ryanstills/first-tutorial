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

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/*
	Map size: 		640 x 480
	Character Size:	32px
	Grid layout: 20 x 15
*/
public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Sprite playerCharacter;
	private Sprite keySprite;
	private static Sprite breakableWallSprite;

	private OrthographicCamera camera;

	private Texture playerTexture;
	private Texture breakableWallTexture;
	private Texture keyTexture;

	private int[] mapContentsLocations;
	private ArrayList<MapContents> mapContentsLayout;

	private TiledMap level;
	private TiledMapRenderer levelRenderer;

	private BitmapFont font;

	private static float xPosPlayer;
	private static float yPosPlayer;
	private int spriteSize = 64;
	private int gridBoxNumber = 233;
	private int winWidth;
	private int winHeight;
	private float gameTime;
	private float movementTime;
	private String timeDisplay;
	private int keyCountDisplay;
	private static final float moveUnit = 32.0f;  //increase to 300?

	private void loadTextures(){
		level = new TmxMapLoader().load("images/sample_background.tmx");
		playerTexture = new Texture("images/placeholder_character.png");
		breakableWallTexture = new Texture("images/breakable_wall_green.png");
		keyTexture = new Texture("images/key.png");

		levelRenderer = new OrthogonalTiledMapRenderer(level);
		playerCharacter = new Sprite(playerTexture);
		breakableWallSprite = new Sprite(breakableWallTexture);
		keySprite = new Sprite(keyTexture);
	}
	private void loadMap() {
		int xPos = 32;
		int yPos = 32;
		int positionCount = 0;
		int keyCount = 5;
		for(int i = 0; i < gridBoxNumber; i++){
			mapContentsLocations[i] = ThreadLocalRandom.current().nextInt(0,10);
		}
		for(int i = 0; i < mapContentsLocations.length; i++){
			if(mapContentsLocations[i] <= 3){
				MapContents temp = MapContents.getContent(xPos, yPos, breakableWallSprite, 1);
				mapContentsLayout.add(temp);
			}
			if(mapContentsLocations[i] == 5 && keyCount > 0){
				MapContents temp = MapContents.getContent(xPos, yPos, keySprite, 2);
				mapContentsLayout.add(temp);
				keyCount--;
			}

			if(positionCount < 17){
				xPos += 32;
				positionCount++;
			}
			else{
				xPos = 32;
				yPos += 32;
				positionCount = 0;
			}
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
	}
	private void renderBreakableWalls(){

		for(int i = 0; i < mapContentsLayout.size(); i++){
			batch.draw(mapContentsLayout.get(i).getSprite(),
					   mapContentsLayout.get(i).getXPos(),
					   mapContentsLayout.get(i).getYPos());
		}
	}
	private void renderPlayerCharacter(float elapsedTime){
		movementTime += elapsedTime;
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			if(yPosPlayer >= winHeight - spriteSize){
				yPosPlayer = winHeight - spriteSize;
			}
			else {
				if(movementTime > 0.15f) {
					yPosPlayer += moveUnit;
					movementTime = 0;
				}
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			if(yPosPlayer <= 32){
				yPosPlayer = 32;
			}
			else{
				if(movementTime > 0.15f) {
					yPosPlayer -= moveUnit;
					movementTime = 0;
				}
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			if(xPosPlayer >= winWidth - spriteSize){
				xPosPlayer = winWidth - spriteSize;
			}
			else{
				if(movementTime > 0.15f) {
					xPosPlayer += moveUnit;
					movementTime = 0;
				}
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A)){
			if(xPosPlayer <= 32){
				xPosPlayer = 32;
			}
			else{
				if(movementTime > 0.15f) {
					xPosPlayer -= moveUnit;
					movementTime = 0;
				}
			}
		}
		playerCharacter.setPosition(xPosPlayer,yPosPlayer);
		playerCharacter.draw(batch);
	}
	private void renderTime(float elapsedTime){
		gameTime -= elapsedTime;
		if(gameTime > 0) {
			timeDisplay = Float.toString(gameTime);
		}
		else{
			timeDisplay = "Time's Up!";
		}
		font.draw(batch, timeDisplay, 560,465);
	}
	private void renderKeyCount(){
		font.draw(batch, Integer.toString(keyCountDisplay) + " keys left", 32, 465);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		mapContentsLocations = new int[gridBoxNumber];
		mapContentsLayout = new ArrayList<MapContents>();

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
		gameTime = 99;
		movementTime = 0;
		keyCountDisplay = 5;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float elapsedTime = Gdx.graphics.getDeltaTime();

		renderLevel();

		batch.begin();
			renderBreakableWalls();
			renderPlayerCharacter(elapsedTime);
			renderTime(elapsedTime);
			renderKeyCount();
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
