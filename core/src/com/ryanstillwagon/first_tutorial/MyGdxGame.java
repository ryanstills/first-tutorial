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

import static com.ryanstillwagon.first_tutorial.MapContents.mapContentsPositions;

/*
	Map size: 		640 x 480
	Character Size:	32px
	Grid layout: 18 x 13
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
	private int[] breakableWallLocations;
	private ArrayList<MapContents> breakableWallLayout;
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
	private int keyCount;
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
		loadWalls();
	}
	private void loadFont(){
		font = new BitmapFont();
		font.setColor(Color.YELLOW);
	}
	private void loadWalls(){
		int xPos = 32;
		int yPos = 32;
		int positionCount = 0;
		for(int i = 0; i < gridBoxNumber; i++){
			breakableWallLocations[i] = ThreadLocalRandom.current().nextInt(0,10);
		}
		for(int i = 0; i < breakableWallLocations.length; i++){
			if(breakableWallLocations[i] <= 3){
				MapContents temp = MapContents.getContent(xPos, yPos, breakableWallSprite, 1);
				breakableWallLayout.add(temp);
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

	private void renderLevel(){
		camera.update();
		levelRenderer.setView(camera);
		levelRenderer.render();
	}
	private void renderBreakableWalls(){

		for(int i = 0; i < breakableWallLayout.size(); i++){
			batch.draw(breakableWallLayout.get(i).getSprite(),
					   breakableWallLayout.get(i).getXPos(),
					   breakableWallLayout.get(i).getYPos());
		}
	}
	private void renderPlayerCharacter(float elapsedTime){
		movementTime += elapsedTime;
		String positionKey;
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			positionKey = stringify((int)(xPosPlayer),(int)(yPosPlayer + 32));
			if(yPosPlayer >= winHeight - spriteSize){
				yPosPlayer += 0;
			}
			// If there is a breakable block do not move
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 1){
				yPosPlayer += 0;
			}
			else {
				if(movementTime > 0.15f) {
					yPosPlayer += moveUnit;
					movementTime = 0;
				}
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			positionKey = stringify((int)(xPosPlayer),(int)(yPosPlayer - 32));
			if(yPosPlayer <= 32){
				yPosPlayer = 32;
			}
			// If there is a breakable block do not move
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 1){
				yPosPlayer += 0;
			}
			else{
				if(movementTime > 0.15f) {
					yPosPlayer -= moveUnit;
					movementTime = 0;
				}
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			positionKey = stringify((int)(xPosPlayer + 32),(int)(yPosPlayer));
			if(xPosPlayer >= winWidth - spriteSize){
				xPosPlayer = winWidth - spriteSize;
			}
			// If there is a breakable block do not move
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 1){
				xPosPlayer += 0;
			}
			else{
				if(movementTime > 0.15f) {
					xPosPlayer += moveUnit;
					movementTime = 0;
				}
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A)){
			positionKey = stringify((int)(xPosPlayer - 32),(int)(yPosPlayer ));
			if(xPosPlayer <= 32){
				xPosPlayer = 32;
			}
			// If there is a breakable block do not move
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 1){
				xPosPlayer += 0;
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
		font.draw(batch, Integer.toString(keyCount) + " keys left", 32, 465);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		breakableWallLocations = new int[gridBoxNumber];
		breakableWallLayout = new ArrayList<MapContents>();

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
		keyCount = 5;
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
		breakableWallTexture.dispose();
		keyTexture.dispose();
		level.dispose();
		font.dispose();
	}

	private String stringify(int xPos, int yPos){
		return ( Integer.toString(xPos) + Integer.toString(yPos) );
	}
}
