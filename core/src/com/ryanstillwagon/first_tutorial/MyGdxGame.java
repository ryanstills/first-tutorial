package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Sprite playerCharacter;
	private Sprite background;

	private Texture playerTexture;
	private Texture backgroundTexture;

	private static float xPosPlayer;
	private static float yPosPlayer;

	private static final float moveUnit = 30.0f;  //increase to 30?

	private void loadTextures(){
		backgroundTexture = new Texture("images/walls1.png");
		playerTexture = new Texture("images/spiral.png");

		background = new Sprite(backgroundTexture);
		playerCharacter = new Sprite(playerTexture);
	}

	private void renderBackground(){
		background.draw(batch);

	}
	private void renderPlayerCharacter(){

		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			yPosPlayer += (moveUnit * Gdx.graphics.getDeltaTime());
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			yPosPlayer -= (moveUnit * Gdx.graphics.getDeltaTime());
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			xPosPlayer += (moveUnit * Gdx.graphics.getDeltaTime());
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A)){
			xPosPlayer -= (moveUnit * Gdx.graphics.getDeltaTime());
		}
		playerCharacter.setPosition(xPosPlayer,yPosPlayer);
		playerCharacter.draw(batch);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		loadTextures();
		xPosPlayer = 0;
		yPosPlayer = 0;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
			renderBackground();
			renderPlayerCharacter();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerTexture.dispose();
		backgroundTexture.dispose();
	}
}
