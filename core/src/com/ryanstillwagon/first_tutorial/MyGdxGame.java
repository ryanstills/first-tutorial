package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
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
public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Sprite playerCharacter;
	private OrthographicCamera camera;
	private Texture playerTexture;
	private Texture breakableWallTexture;
	private Texture keyTexture;
	private Texture masterKeyTexture;
	private Texture exitDoorTexture;
	private Texture movableBlockAllTexture;
	private Texture movableBlockUpTexture;
	private Texture movableBlockDownTexture;
	private Texture movableBlockLeftTexture;
	private Texture movableBlockRightTexture;
	private Texture[] contentsTextures;
	private int[] mapContentsLocations;
	private static ArrayList<MapContents> levelLayout;
	private TiledMap level;
	private TiledMapRenderer levelRenderer;
	private BitmapFont font;
	private static float xPosPlayer;
	private static float yPosPlayer;
	private int spriteSize = 64;
	private int winWidth;
	private int winHeight;
	private float gameTime;
	private float movementTime;
	private String timeDisplay;
	private int keyCount;
	private static final float moveUnit = 32.0f;

	private KeyObject keyOne;
	private String keyString;
	private boolean masterKeySpawned;
	private boolean exitDoorSpawned;

	private int[] map = {2,1,0,0,0,0,0,0,0,0,1,0,1,1,1,1,2,1,
						 0,1,0,0,3,1,0,1,0,1,0,0,0,0,0,1,1,1,
						 0,1,1,0,0,0,1,0,0,0,0,3,1,0,1,0,0,1,
						 0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,1,0,0,
						 1,1,1,1,0,3,0,4,0,1,1,1,0,1,0,0,1,0,
						 0,0,1,2,1,0,0,0,0,1,2,1,0,1,0,3,0,0,
						 1,0,1,1,1,0,0,0,0,1,1,1,0,0,0,9,0,0,
						 0,0,1,0,0,1,0,1,0,1,0,0,1,0,0,0,0,1,
						 1,1,0,1,2,1,0,0,0,0,0,1,1,0,1,1,0,1,
						 1,1,0,0,0,1,0,1,0,1,0,1,0,0,0,3,0,0};

	//FileHandle readFile = Gdx.files.local("test_level.txt");




	private void loadTextures(){
		level = new TmxMapLoader().load("images/sample_background.tmx");
		playerTexture = new Texture("images/placeholder_character.png");
		breakableWallTexture = new Texture("images/breakable_wall_green.png");
		keyTexture = new Texture("images/key.png");
		masterKeyTexture = new Texture("images/master_key.png");
		exitDoorTexture = new Texture("images/castledoors.png");

		movableBlockAllTexture = new Texture("images/block_any_direction.png");
		movableBlockUpTexture = new Texture("images/block_only_up.png");
		movableBlockDownTexture = new Texture("images/block_only_down.png");
		movableBlockLeftTexture = new Texture("images/block_only_left.png");
		movableBlockRightTexture = new Texture("images/block_only_right.png");

		levelRenderer = new OrthogonalTiledMapRenderer(level);
		playerCharacter = new Sprite(playerTexture);

		contentsTextures[1] = breakableWallTexture;
		contentsTextures[2] = keyTexture;
		contentsTextures[3] = movableBlockAllTexture;
		contentsTextures[4] = movableBlockDownTexture;
		contentsTextures[5] = movableBlockUpTexture;
		contentsTextures[6] = movableBlockLeftTexture;
		contentsTextures[7] = movableBlockRightTexture;
		contentsTextures[9] = playerTexture;
	}
	private void loadMap() {
		int xPos = 32;
		int yPos = 32;
		int positionCount = 0;

		for(int i = 0; i < map.length; i++){
			mapContentsLocations[i] = map[i];
		}
		for(int i = 0; i < mapContentsLocations.length; i++){

			MapContents temp = MapContents.getContent(xPos, yPos, contentsTextures, mapContentsLocations[i]);
			levelLayout.add(temp);

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
		String positionKey;
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
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
				if(movementTime > 0.15f) {
					yPosPlayer += moveUnit;
					movementTime = 0;
					mapContentsPositions.remove(positionKey);
					keyCount--;
				}
			}
			//checks for an block that can be moved in all directions or up only
			else if(mapContentsPositions.containsKey(positionKey) &&
					(mapContentsPositions.get(positionKey).getContentsKey() == 3 ||
					 mapContentsPositions.get(positionKey).getContentsKey() == 5)){

				if(movementTime > 0.15f && yPosPlayer <= winHeight - (spriteSize * 2) &&
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
			// Checks for key
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 2){
				if(movementTime > 0.15f) {
					yPosPlayer -= moveUnit;
					movementTime = 0;
					mapContentsPositions.remove(positionKey);
					keyCount--;
				}
			}
			// Checks for a block that can be move in all directions or down
			else if(mapContentsPositions.containsKey(positionKey) &&
					(mapContentsPositions.get(positionKey).getContentsKey() == 3 ||
					 mapContentsPositions.get(positionKey).getContentsKey() == 4)){
				if(movementTime > 0.15f && yPosPlayer > 64 &&
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
			// if player moves into a space with a key in it
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 2){
				if(movementTime > 0.15f) {
					xPosPlayer += moveUnit;
					movementTime = 0;
					mapContentsPositions.remove(positionKey);
					keyCount--;
				}
			}
			// checks for a block that can be move in all directions or right
			else if(mapContentsPositions.containsKey(positionKey) &&
					(mapContentsPositions.get(positionKey).getContentsKey() == 3 ||
					 mapContentsPositions.get(positionKey).getContentsKey() == 7)){
				if(movementTime > 0.15f && xPosPlayer <= winWidth - (spriteSize * 2)&&
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
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 2){
				if(movementTime > 0.15f) {
					xPosPlayer -= moveUnit;
					movementTime = 0;
					mapContentsPositions.remove(positionKey);
					keyCount--;
				}
			}
			//checks for a block that can be moved in all directions or left
			else if(mapContentsPositions.containsKey(positionKey) &&
					(mapContentsPositions.get(positionKey).getContentsKey() == 3 ||
					 mapContentsPositions.get(positionKey).getContentsKey() == 6)){
				if(movementTime > 0.15f  && xPosPlayer > 64 &&
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
		if(keyCount > 0) {
			font.draw(batch, Integer.toString(keyCount) + " keys left", 32, 465);
		}
		else if(keyCount == 0){
			font.draw(batch, "Master key has spawned!" , 32, 465);
		}
		else{
			font.draw(batch, "Door has spawned!", 32 , 465);
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		levelLayout = new ArrayList<MapContents>();
		contentsTextures = new Texture[11];
		mapContentsLocations = new int[233];

		winWidth = Gdx.graphics.getWidth();
		winHeight = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, winWidth, winHeight);
		camera.update();

		loadTextures();
		loadMap();
		loadFont();

		xPosPlayer = 544;
		yPosPlayer = 320;
		gameTime = 99;
		movementTime = 0;
		keyCount = 5;
		masterKeySpawned = false;
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
		for(int i = 0; i < contentsTextures.length; i++){
			if(contentsTextures[i] != null) {
				contentsTextures[i].dispose();
			}
		}
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
