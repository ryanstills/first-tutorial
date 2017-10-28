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
import java.util.Iterator;
import java.util.Map;
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
	private static Sprite keySprite;
	private Sprite breakableWallSprite;
	private Sprite moveableBlockSprite;
	private OrthographicCamera camera;
	private Texture playerTexture;
	private Texture breakableWallTexture;
	private Texture keyTexture;
	private Texture moveableBlockTexture;
	private int[] breakableWallLocations;
	private static ArrayList<MapContents> breakableWallLayout;
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

	private KeyObject keyOne;
	private String keyString;
	private BlockAllDirections testBlock;

	private void loadTextures(){
		level = new TmxMapLoader().load("images/sample_background.tmx");
		playerTexture = new Texture("images/placeholder_character.png");
		breakableWallTexture = new Texture("images/breakable_wall_green.png");
		keyTexture = new Texture("images/key.png");
		moveableBlockTexture = new Texture("images/block_any_direction.png");

		levelRenderer = new OrthogonalTiledMapRenderer(level);
		playerCharacter = new Sprite(playerTexture);
		breakableWallSprite = new Sprite(breakableWallTexture);
		keySprite = new Sprite(keyTexture);
		moveableBlockSprite = new Sprite(moveableBlockTexture);
	}
	private void loadMap() {
		loadWalls();
		loadMoveableBlocks();
		loadKeys();

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
	private void loadMoveableBlocks(){
		testBlock = new BlockAllDirections(128, 128, moveableBlockSprite);
		keyString = stringify(testBlock.getXPos(), testBlock.getYPos());
		mapContentsPositions.put(keyString, testBlock);
	}
	private void loadKeys(){
		keyOne = new KeyObject(96,96, keySprite);
		keyString = stringify(keyOne.getXPos(),keyOne.getYPos());
		mapContentsPositions.put(keyString, keyOne);
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
			//checks for moveable blocks
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 3){

				if(movementTime > 0.15f && yPosPlayer <= winHeight - (spriteSize * 2)){
					yPosPlayer += moveUnit;
					movementTime = 0;
					mapContentsPositions.get(positionKey).setYPos(32);
				}
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
			// Checks for moveable block
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 3){
				if(movementTime > 0.15f && yPosPlayer > 64) {
					yPosPlayer -= moveUnit;
					movementTime = 0;
					mapContentsPositions.get(positionKey).setYPos(-32);
				}
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
			// if player moves into the space of a moveable block
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 3){
				if(movementTime > 0.15f && xPosPlayer <= winWidth - (spriteSize * 2)) {

					xPosPlayer += moveUnit;
					movementTime = 0;
					mapContentsPositions.get(positionKey).setXPos(32);
				}
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
			// if player moves into the space of a moveable block
			else if(mapContentsPositions.containsKey(positionKey) &&
					mapContentsPositions.get(positionKey).getContentsKey() == 3){
				if(movementTime > 0.15f  && xPosPlayer > 64) {
					xPosPlayer -= moveUnit;
					movementTime = 0;
					mapContentsPositions.get(positionKey).setXPos(-32);
				}
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
			renderMap();
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
