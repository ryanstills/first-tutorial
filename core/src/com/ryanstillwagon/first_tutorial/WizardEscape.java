package com.ryanstillwagon.first_tutorial;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ryanstillwagon.first_tutorial.managers.MapsManager;

import java.util.concurrent.ThreadLocalRandom;

import static com.ryanstillwagon.first_tutorial.MapContents.*;

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
	private TiledMapRenderer levelRenderer;
	private BitmapFont font;
	private static float xPosPlayer;
	private static float yPosPlayer;
	private int winWidth;
	private int winHeight;
	private int gameTime;
	private int levelScore;
	private float timeCounter;
	private float movementTime;
	private String timeDisplay;
	private int keyCount;
	private static final float moveUnit = 32.0f;
	private KeyObject masterKey;
	private KeyObject exitDoor;
	private MapsManager mapsManager;

	private void loadTextures(){

		levelRenderer = new OrthogonalTiledMapRenderer(res.level);
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
		int level = 2;
		int[] map = mapsManager.getMap(level);

		for(int i = 0; i < map.length; i++){

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
        for(MapContents content : breakableWallPositions.values()){
            batch.draw(content.getSprite(), content.getXPos(), content.getYPos());
        }
        for (MapContents content : keyPositions.values()){
            batch.draw(content.getSprite(), content.getXPos(), content.getYPos());
        }
        for(MapContents content : movableBlockPositions.values()){
            batch.draw(content.getSprite(), content.getXPos(), content.getYPos());
        }
	}
	private void renderPlayerCharacter(float elapsedTime){
		movementTime += elapsedTime;
		boolean move = movementTime > 0.15;
		String movePositionKey;

		if(Gdx.input.isKeyPressed(Input.Keys.W) && move){
		    movePositionKey = stringify((int)xPosPlayer, (int)(yPosPlayer + res.TILE_SIZE));
		    //if the boundary or a breakable block is above the player
		    if(((yPosPlayer >= winHeight - res.TILE_SIZE * 2) ||
                    breakableWallPositions.containsKey(movePositionKey))){
		        yPosPlayer += 0;
            }
            //if a movable block is above the player
            else if(movableBlockPositions.containsKey(movePositionKey)){
                String aboveMovePositionKey = stringify((int)xPosPlayer, (int)yPosPlayer + res.TILE_SIZE * 2);
                //checks if the movable block has a movable block above it
                //checks if the movable block is at the boundary
                //checks if the movable block is the right move type (ALL or UP)
                if(movableBlockPositions.containsKey(aboveMovePositionKey) ||
                        yPosPlayer >= winHeight - res.TILE_SIZE * 3        ||
                        (movableBlockPositions.get(movePositionKey).getContentsKey() != 3 &&
                         movableBlockPositions.get(movePositionKey).getContentsKey() != 5)){
                    yPosPlayer += 0;
                }
                else{
                    if(breakableWallPositions.containsKey(aboveMovePositionKey)){
                        breakableWallPositions.remove(aboveMovePositionKey);
                        movableBlockPositions.get(movePositionKey).setYPos((int)moveUnit);
                        yPosPlayer += moveUnit;
                        movementTime = 0;
                        levelScore += 100;
                    }
                    else{
                        movableBlockPositions.get(movePositionKey).setYPos((int)moveUnit);
                        yPosPlayer += moveUnit;
                        movementTime = 0;
                    }
                }
            }
            else{
		        yPosPlayer += moveUnit;
		        movementTime = 0;
            }
            if(keyPositions.containsKey(movePositionKey)){
                keyPositions.remove(movePositionKey);
                keyCount--;
                levelScore += 500;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S) && move){
            movePositionKey = stringify((int)xPosPlayer, (int)(yPosPlayer - res.TILE_SIZE));
            //if the boundary or a breakable block is above the player
            if(((yPosPlayer <= res.TILE_SIZE) ||
                    breakableWallPositions.containsKey(movePositionKey))  &&
                    !keyPositions.containsKey(movePositionKey)){
                yPosPlayer -= 0;
            }
            //if a movable block is below the player
            else if(movableBlockPositions.containsKey(movePositionKey)){
                String belowMovePositionKey = stringify((int)xPosPlayer, (int)yPosPlayer - res.TILE_SIZE * 2);
                //checks if the movable block has a movable block below it
                //checks if the movable block is at the boundary
                //checks if the movable block is the right move type (ALL or DOWN)
                if(movableBlockPositions.containsKey(belowMovePositionKey) ||
                        yPosPlayer <= res.TILE_SIZE * 2 ||
                        (movableBlockPositions.get(movePositionKey).getContentsKey() != 3 &&
                                movableBlockPositions.get(movePositionKey).getContentsKey() != 4)){
                    yPosPlayer -= 0;
                }
                else{
                    if(breakableWallPositions.containsKey(belowMovePositionKey)){
                        breakableWallPositions.remove(belowMovePositionKey);
                        movableBlockPositions.get(movePositionKey).setYPos((int)-moveUnit);
                        yPosPlayer -= moveUnit;
                        movementTime = 0;
                        levelScore += 100;
                    }
                    else{
                        movableBlockPositions.get(movePositionKey).setYPos((int)-moveUnit);
                        yPosPlayer -= moveUnit;
                        movementTime = 0;
                    }
                }
            }
            else{
                yPosPlayer -= moveUnit;
                movementTime = 0;
            }
            if(keyPositions.containsKey(movePositionKey)){
                keyPositions.remove(movePositionKey);
                keyCount--;
                levelScore += 500;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D) && move){
            movePositionKey = stringify((int)xPosPlayer  + res.TILE_SIZE, (int)(yPosPlayer));
            //if the boundary or a breakable block is to the right of the player
            if((xPosPlayer >= winWidth - res.TILE_SIZE * 2) ||
                    breakableWallPositions.containsKey(movePositionKey) &&
                    !keyPositions.containsKey(movePositionKey)){
                xPosPlayer += 0;
            }
            //if a movable block is to the right of the player
            else if(movableBlockPositions.containsKey(movePositionKey)){
                String rightOfMovePositionKey = stringify((int)xPosPlayer  + res.TILE_SIZE * 2, (int)yPosPlayer);
                //checks if the movable block has a movable block to the right of it
                //checks if the movable block is at the boundary
                //checks if the movable block is the right move type (ALL or Right)
                if(movableBlockPositions.containsKey(rightOfMovePositionKey) ||
                        xPosPlayer >= winWidth - res.TILE_SIZE * 3        ||
                        (movableBlockPositions.get(movePositionKey).getContentsKey() != 3 &&
                                movableBlockPositions.get(movePositionKey).getContentsKey() != 7)){
                    xPosPlayer += 0;
                }
                else{
                    if(breakableWallPositions.containsKey(rightOfMovePositionKey)){
                        breakableWallPositions.remove(rightOfMovePositionKey);
                        movableBlockPositions.get(movePositionKey).setXPos((int)moveUnit);
                        xPosPlayer += moveUnit;
                        movementTime = 0;
                        levelScore += 100;
                    }
                    else{
                        movableBlockPositions.get(movePositionKey).setXPos((int)moveUnit);
                        xPosPlayer += moveUnit;
                        movementTime = 0;
                    }
                }
            }
            else{
                xPosPlayer += moveUnit;
                movementTime = 0;
            }
            if(keyPositions.containsKey(movePositionKey)){
                keyPositions.remove(movePositionKey);
                keyCount--;
                levelScore += 500;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A) && move){
            movePositionKey = stringify((int)xPosPlayer  - res.TILE_SIZE, (int)(yPosPlayer));
            //if the boundary or a breakable block is to the left of the player
            if((xPosPlayer <= res.TILE_SIZE) ||
                    breakableWallPositions.containsKey(movePositionKey) &&
                    !keyPositions.containsKey(movePositionKey)){
                xPosPlayer += 0;
            }
            //if a movable block is to the right of the player
            else if(movableBlockPositions.containsKey(movePositionKey)){
                String rightOfMovePositionKey = stringify((int)xPosPlayer  - res.TILE_SIZE * 2, (int)yPosPlayer);
                //checks if the movable block has a movable block to the left of it
                //checks if the movable block is at the boundary
                //checks if the movable block is the right move type (ALL or Left)
                if(movableBlockPositions.containsKey(rightOfMovePositionKey) ||
                        xPosPlayer <= res.TILE_SIZE * 2        ||
                        (movableBlockPositions.get(movePositionKey).getContentsKey() != 3 &&
                                movableBlockPositions.get(movePositionKey).getContentsKey() != 6)){
                    xPosPlayer += 0;
                }
                else{
                    if(breakableWallPositions.containsKey(rightOfMovePositionKey)){
                        breakableWallPositions.remove(rightOfMovePositionKey);
                        movableBlockPositions.get(movePositionKey).setXPos((int)-moveUnit);
                        xPosPlayer -= moveUnit;
                        movementTime = 0;
                        levelScore += 100;
                    }
                    else{
                        movableBlockPositions.get(movePositionKey).setXPos((int)-moveUnit);
                        xPosPlayer -= moveUnit;
                        movementTime = 0;
                    }
                }
            }
            else{
                xPosPlayer -= moveUnit;
                movementTime = 0;
            }
            if(keyPositions.containsKey(movePositionKey)){
                keyPositions.remove(movePositionKey);
                keyCount--;
                levelScore += 500;
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
			    int x = ThreadLocalRandom.current().nextInt(1,19) * 32;
			    int y = 416;
			    masterKey = new KeyObject(x,y, res.masterKeyTexture);
			    keyPositions.put(stringify(x,y), masterKey);
			}
		}
		else if(keyCount == -1){
			font.draw(batch, "Door has spawned!", 32 , 465);
			if(exitDoor == null) {
			    int x = ThreadLocalRandom.current().nextInt(1, 19) * 32;
			    int y = 0;
				exitDoor = new KeyObject(x,	y, res.exitDoorTexture);
				keyPositions.put(stringify(x,y), exitDoor);
			}
		}
		else{
		    font.draw(batch, "VICTORY", (winWidth / 2) - res.TILE_SIZE, winHeight /2);
        }
	}
	private void renderScore(){
	    font.draw(batch, "Score: " + Integer.toString(levelScore), 275, 465);
    }

	@Override
	public void create () {
		batch = new SpriteBatch();
		contentsTextures = new TextureRegion[11];
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
		levelScore = 0;
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
			renderScore();
		batch.end();
	}
	@Override
	public void dispose () {
		batch.dispose();
		res.dispose();
		font.dispose();
	}
	private String stringify(int xPos, int yPos){
		return ( Integer.toString(xPos) + Integer.toString(yPos) );
	}
}
