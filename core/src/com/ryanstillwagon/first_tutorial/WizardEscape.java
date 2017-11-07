package com.ryanstillwagon.first_tutorial;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ryanstillwagon.first_tutorial.managers.MapsManager;

import java.util.concurrent.ThreadLocalRandom;

import static com.ryanstillwagon.first_tutorial.MapContents.mapContentsPositions;

/*
	Map size: 		640 x 480
	Character Size:	32px
	Grid layout: 18 x 13

Contents Key:
1 - breakable wall
2 - key object
3 - all direction movable block
4 - down only movable block
5 - up only movable block
6 - left only movable block
7 - right only movable block
8 - traps (not in use yet)
9 - player character
10 - enemy character (not in use yet)
*/
public class WizardEscape extends Game {
	private SpriteBatch batch;
	private Sprite playerCharacter;
	private OrthographicCamera camera;
	private Resources res;
	private TextureRegion[] contentsTextures;
	private int[] mapContentsLocations;
	private TiledMap level;
	private TiledMapRenderer levelRenderer;
	private BitmapFont font;
	private static float xPosPlayer;
	private static float yPosPlayer;
	private int spriteSize = 64;
	private int winWidth;
	private int winHeight;
	private int gameTime;
	private float timeCounter;
	private float movementTime;
	private String timeDisplay;
	private int keyCount;
	private static final float moveUnit = 32.0f;
	KeyObject masterKey;
	KeyObject exitDoor;
	private MapsManager mapsManager;

	private void loadTextures(){
		level = new TmxMapLoader().load("sample_background.tmx");

		levelRenderer = new OrthogonalTiledMapRenderer(level);
		playerCharacter = new Sprite(res.playerTexture);

		contentsTextures[1] = res.breakableWallTexture;
		contentsTextures[2] = res.keyTexture;
		contentsTextures[3] = res.movableBlockAllTexture;
		contentsTextures[4] = res.movableBlockDownTexture;
		contentsTextures[5] = res.movableBlockUpTexture;
		contentsTextures[6] = res.movableBlockLeftTexture;
		contentsTextures[7] = res.movableBlockRightTexture;
		contentsTextures[9] = res.playerTexture;
	}
	private void loadMap() {
		int xPos = 32;
		int yPos = 32;
		int positionCount = 0;
		int level = 1;
		int[] map = mapsManager.getMap(level);

		for(int i = 0; i < mapContentsLocations.length; i++){

			if(map[i] == 9){
				xPosPlayer = xPos;
				yPosPlayer = yPos;
			}
			MapContents temp = MapContents.getContent(xPos, yPos, contentsTextures, map[i]);

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
	private void renderMap(){
		for(MapContents content : mapContentsPositions.values()){
			batch.draw(content.getSprite(), content.getXPos(), content.getYPos());
		}
	}
	private void renderPlayerCharacter(float elapsedTime){
		movementTime += elapsedTime;
		boolean move = movementTime > 0.15;
		String positionKey;
		if(Gdx.input.isKeyPressed(Input.Keys.W) && move){
			positionKey = stringify((int)(xPosPlayer),(int)(yPosPlayer + 32));
			//if player is at the top of the screen
			if(yPosPlayer >= winHeight - spriteSize){
				yPosPlayer += 0;
			}
			// If there is a breakable block do not move
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 1){
				yPosPlayer += 0;
			}
			// if player moves into key space
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 2){

					yPosPlayer += moveUnit;
					movementTime = 0;
					mapContentsPositions.remove(positionKey);
					keyCount--;
		}
			//checks for an block that can be moved in all directions or up only
			else if(mapContentsPositions.containsKey(positionKey) &&
					(mapContentsPositions.get(positionKey).getContentsKey() == 3 ||
					 mapContentsPositions.get(positionKey).getContentsKey() == 5)){

				if(yPosPlayer <= winHeight - (spriteSize * 2) &&
						checkForMovableBlockCollision((int)xPosPlayer, (int)yPosPlayer, 0)){
					yPosPlayer += moveUnit;
					movementTime = 0;
					mapContentsPositions.get(positionKey).setYPos(32);
				}
			}
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() != 5){

				yPosPlayer += 0;
			}
			else {
				yPosPlayer += moveUnit;
				movementTime = 0;
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S) && move){
			positionKey = stringify((int)(xPosPlayer),(int)(yPosPlayer - 32));
			if(yPosPlayer <= 32){
				yPosPlayer = 32;
			}
			// If there is a breakable block do not move
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 1){
				yPosPlayer += 0;
			}
			// Checks for key
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 2){

					yPosPlayer -= moveUnit;
					movementTime = 0;
					mapContentsPositions.remove(positionKey);
					keyCount--;
			}
			// Checks for a block that can be move in all directions or down
			else if(mapContentsPositions.containsKey(positionKey) &&
					(mapContentsPositions.get(positionKey).getContentsKey() == 3 ||
					 mapContentsPositions.get(positionKey).getContentsKey() == 4)){
				if(yPosPlayer > 64 &&
						checkForMovableBlockCollision((int)xPosPlayer,(int)yPosPlayer, 1)) {
					yPosPlayer -= moveUnit;
					movementTime = 0;
					mapContentsPositions.get(positionKey).setYPos(-32);
				}
			}
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() != 4){

				yPosPlayer += 0;
			}
			else{
				yPosPlayer -= moveUnit;
				movementTime = 0;
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.D) && move){
			positionKey = stringify((int)(xPosPlayer + 32),(int)(yPosPlayer));
			if(xPosPlayer >= winWidth - spriteSize){
				xPosPlayer = winWidth - spriteSize;
			}
			// If there is a breakable block do not move
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 1){
				xPosPlayer += 0;
			}
			// if player moves into a space with a key in it
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 2){

				xPosPlayer += moveUnit;
				movementTime = 0;
				mapContentsPositions.remove(positionKey);
				keyCount--;

			}
			// checks for a block that can be move in all directions or right
			else if(mapContentsPositions.containsKey(positionKey) &&
					(mapContentsPositions.get(positionKey).getContentsKey() == 3 ||
					 mapContentsPositions.get(positionKey).getContentsKey() == 7)){
				if(xPosPlayer <= winWidth - (spriteSize * 2)&&
						checkForMovableBlockCollision((int)xPosPlayer,(int)yPosPlayer, 2)) {

					xPosPlayer += moveUnit;
					movementTime = 0;
					mapContentsPositions.get(positionKey).setXPos(32);
				}
			}
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() != 7){

				xPosPlayer += 0;
			}
			else{

				xPosPlayer += moveUnit;
				movementTime = 0;

			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A) && move){
			positionKey = stringify((int)(xPosPlayer - 32),(int)(yPosPlayer ));
			if(xPosPlayer <= 32){
				xPosPlayer = 32;
			}
			// If there is a breakable block do not move
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 1){
				xPosPlayer += 0;
			}
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 2){

				xPosPlayer -= moveUnit;
				movementTime = 0;
				mapContentsPositions.remove(positionKey);
				keyCount--;

			}
			//checks for a block that can be moved in all directions or left
			else if(mapContentsPositions.containsKey(positionKey) &&
					(mapContentsPositions.get(positionKey).getContentsKey() == 3 ||
					 mapContentsPositions.get(positionKey).getContentsKey() == 6)){
				if(xPosPlayer > 64 &&
						checkForMovableBlockCollision((int)xPosPlayer, (int) yPosPlayer, 3)) {
					xPosPlayer -= moveUnit;
					movementTime = 0;
					mapContentsPositions.get(positionKey).setXPos(-32);
				}
			}
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() != 6){

				xPosPlayer += 0;
			}
			else{
					xPosPlayer -= moveUnit;
					movementTime = 0;
			}
		}
		playerCharacter.setPosition(xPosPlayer,yPosPlayer);
		playerCharacter.draw(batch);
	}
	private void renderTime(float elapsedTime){

		timeCounter += elapsedTime;
		if(timeCounter > 1.0){
			gameTime -= 1;
			timeCounter = 0;
		}
		if(gameTime > 0) {
			timeDisplay = Integer.toString(gameTime);
		}
		else{
			timeDisplay = "Time's Up!";
		}
		font.draw(batch, timeDisplay, 555,465);
	}
	private void renderKeyCount(){
		if(keyCount > 0) {
			font.draw(batch, Integer.toString(keyCount) + " keys left", 32, 465);
		}
		else if(keyCount == 0){

			font.draw(batch, "Master key has spawned!" , 32, 465);
			if(masterKey == null){
			masterKey = new KeyObject(ThreadLocalRandom.current().nextInt(1,19) * 32,
					416, res.masterKeyTexture);
			}
			batch.draw(masterKey.getSprite(), masterKey.getXPos(), masterKey.getYPos());
		}
		else{
			font.draw(batch, "Door has spawned!", 32 , 465);
			if(exitDoor == null) {
				exitDoor = new KeyObject(ThreadLocalRandom.current().nextInt(1, 19) * 32,
						0, res.exitDoorTexture);
			}
			batch.draw(exitDoor.getSprite(), exitDoor.getXPos(), exitDoor.getYPos());
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		contentsTextures = new TextureRegion[11];
		mapContentsLocations = new int[233];
		mapsManager = new MapsManager();
		res = new Resources();



		winWidth = Gdx.graphics.getWidth();
		winHeight = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, winWidth, winHeight);
		camera.update();

		loadTextures();
		loadMap();
		loadFont();

		gameTime = 99;
		movementTime = 0.0f;
		timeCounter = 0.0f;
		keyCount = 5;
	}
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float elapsedTime = Gdx.graphics.getDeltaTime();

		renderLevel();
		batch.begin();
			renderMap();
			renderPlayerCharacter(elapsedTime);
			renderTime(elapsedTime);
			renderKeyCount();
		batch.end();
	}
	@Override
	public void dispose () {
		batch.dispose();
		res.dispose();
		level.dispose();
		font.dispose();
	}

	private boolean checkForMovableBlockCollision(int xPosPlayer, int yPosPlayer, int directionKey){
		String positionKey;
		//up
		if(directionKey == 0){
			positionKey = stringify(xPosPlayer, yPosPlayer + 64);
			if(!mapContentsPositions.containsKey(positionKey))
				return true;
			return (mapContentsPositions.get(positionKey).getContentsKey() < 3 ||
				    mapContentsPositions.get(positionKey).getContentsKey() > 7);
		}
		//down
		else if(directionKey == 1){
			positionKey = stringify(xPosPlayer, yPosPlayer - 64);
			if(!mapContentsPositions.containsKey(positionKey))
				return true;
			return ((mapContentsPositions.get(positionKey).getContentsKey() < 3) ||
					(mapContentsPositions.get(positionKey).getContentsKey() > 7));
		}
		//right
		else if(directionKey == 2){
			positionKey = stringify(xPosPlayer + 64, yPosPlayer);
			if(!mapContentsPositions.containsKey(positionKey))
				return true;
			return ((mapContentsPositions.get(positionKey).getContentsKey() < 3) ||
					(mapContentsPositions.get(positionKey).getContentsKey() > 7));

		}
		//left
		else{
			positionKey = stringify(xPosPlayer - 64, yPosPlayer);
			if(!mapContentsPositions.containsKey(positionKey))
				return true;
			return ((mapContentsPositions.get(positionKey).getContentsKey() < 3) ||
					(mapContentsPositions.get(positionKey).getContentsKey() > 7));

		}
	}
	private String stringify(int xPos, int yPos){
		return ( Integer.toString(xPos) + Integer.toString(yPos) );
	}
}
