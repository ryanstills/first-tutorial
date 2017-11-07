package com.ryanstillwagon.first_tutorial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.ryanstillwagon.first_tutorial.KeyObject;
import com.ryanstillwagon.first_tutorial.MapContents;
import com.ryanstillwagon.first_tutorial.Resources;
import com.ryanstillwagon.first_tutorial.WizardEscape;
import com.ryanstillwagon.first_tutorial.managers.MapsManager;

import java.util.concurrent.ThreadLocalRandom;

import static com.ryanstillwagon.first_tutorial.MapContents.mapContentsPositions;

public class GameScreen implements Screen {

    WizardEscape game;
    private Resources res;
    private TextureRegion[] contentsTextures;
    private TiledMap level;
    private TiledMapRenderer levelRenderer;
    private Sprite playerCharacter;
    private static float xPosPlayer;
    private static float yPosPlayer;
    OrthographicCamera camera;
    private int gameTime;
    private float timeCounter;
    private float movementTime;
    private String timeDisplay;
    private int keyCount;
    private static final float moveUnit = 32.0f;
    private KeyObject masterKey;
    private KeyObject exitDoor;
    private MapsManager mapsManager;

    int i = 0;

    public GameScreen(WizardEscape game) {
        this.game = game;
        contentsTextures = new TextureRegion[11];
        mapsManager = new MapsManager();
        res = new Resources();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);

        loadTextures();
        loadMap();
        loadFont();

        gameTime = 99;
        movementTime = 0.0f;
        timeCounter = 0.0f;
        keyCount = 5;
    }

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
        game.font = new BitmapFont();
        game.font.setColor(Color.YELLOW);
    }
    private void renderLevel(){
        camera.update();
        levelRenderer.setView(camera);
        levelRenderer.render();
    }
    private void renderMap(){
        for(MapContents content : mapContentsPositions.values()){
            game.batch.draw(content.getSprite(), content.getXPos(), content.getYPos());
        }
    }
    private void renderPlayerCharacter(float elapsedTime){
        movementTime += elapsedTime;
        boolean move = movementTime > 0.15;
        String positionKey;
        if(Gdx.input.isKeyPressed(Input.Keys.W) && move){
            if(checkForCollision((int)playerCharacter.getX(), (int)playerCharacter.getY(), 0)){
                playerCharacter.setPosition(xPosPlayer, yPosPlayer + moveUnit);
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S) && move){
            if(checkForCollision((int)playerCharacter.getX(), (int)playerCharacter.getY(), 1)){
                playerCharacter.setPosition(xPosPlayer, yPosPlayer + moveUnit);
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D) && move){
            if(checkForCollision((int)playerCharacter.getX(), (int)playerCharacter.getY(), 2)){
                playerCharacter.setPosition(xPosPlayer + 32, yPosPlayer);
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A) && move){
            if(checkForCollision((int)playerCharacter.getX(), (int)playerCharacter.getY(), 3)){
                playerCharacter.setPosition(xPosPlayer - 32, yPosPlayer);
            }

        }
        else {
            playerCharacter.setPosition(xPosPlayer, yPosPlayer);
        }
        playerCharacter.draw(game.batch);
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
        game.font.draw(game.batch, timeDisplay, 555,465);
    }
    private void renderKeyCount(){
        if(keyCount > 0) {
            game.font.draw(game.batch, Integer.toString(keyCount) + " keys left", 32, 465);
        }
        else if(keyCount == 0){

            game.font.draw(game.batch, "Master key has spawned!" ,32,465);
            if(masterKey == null) {
                masterKey = new KeyObject(ThreadLocalRandom.current().nextInt(1, 19) * 32,
                        416, res.masterKeyTexture);
            }
            game.batch.draw(masterKey.getSprite(), masterKey.getXPos(), masterKey.getYPos());
        }
        else if(keyCount == -1){
            game.font.draw(game.batch, "Door has spawned!", 32 , 465);
            if(exitDoor == null) {
                exitDoor = new KeyObject(ThreadLocalRandom.current().nextInt(1, 19) * 32,
                        0, res.exitDoorTexture);
            }
            game.batch.draw(exitDoor.getSprite(), exitDoor.getXPos(), exitDoor.getYPos());
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        renderLevel();
        game.batch.begin();
        	renderMap();
			renderPlayerCharacter(delta);
			renderTime(delta);
			renderKeyCount();
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.setScreen(new MainMenuScreen(game));
            i++;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        res.dispose();
        level.dispose();
    }

    private boolean checkForCollision(int xPosPlayer, int yPosPlayer, int directionKey){
        switch (directionKey){
            //up
            case 0:{
                String movePosition = stringify(xPosPlayer, yPosPlayer + 32);
                return ((playerCharacter.getX() <= game.winHeight - res.TILE_SIZE) &&
                        mapContentsPositions.containsKey(movePosition) &&
                        mapContentsPositions.get(movePosition).getContentsKey() != 1 &&
                        mapContentsPositions.get(movePosition).getContentsKey() >= 2 &&
                        mapContentsPositions.get(movePosition).getContentsKey() <= 8);
            }
            //down
            case 1:{
                String movePosition = stringify(xPosPlayer, yPosPlayer - 32);
                return ((playerCharacter.getX() <= game.winHeight - res.TILE_SIZE) &&
                        mapContentsPositions.containsKey(movePosition) &&
                        mapContentsPositions.get(movePosition).getContentsKey() != 1 &&
                        mapContentsPositions.get(movePosition).getContentsKey() >= 2 &&
                        mapContentsPositions.get(movePosition).getContentsKey() <= 8);

            }
            //right
            case 2:{
                String movePosition = stringify(xPosPlayer + 32, yPosPlayer);
                return ((playerCharacter.getX() <= game.winWidth - res.TILE_SIZE) &&
                        mapContentsPositions.containsKey(movePosition) &&
                        mapContentsPositions.get(movePosition).getContentsKey() != 1 &&
                        mapContentsPositions.get(movePosition).getContentsKey() >= 2 &&
                        mapContentsPositions.get(movePosition).getContentsKey() <= 8);

            }
            //left
            case 3:{
                String movePosition = stringify(xPosPlayer - 32, yPosPlayer);
                return ((playerCharacter.getX() <= game.winWidth - res.TILE_SIZE) &&
                        mapContentsPositions.containsKey(movePosition) &&
                        mapContentsPositions.get(movePosition).getContentsKey()!= 1 &&
                        mapContentsPositions.get(movePosition).getContentsKey() >= 2 &&
                        mapContentsPositions.get(movePosition).getContentsKey() <= 8);

            }
            default:
                return true;
        }
    }
    private String stringify(int xPos, int yPos){
        return ( Integer.toString(xPos) + Integer.toString(yPos) );
    }

}
