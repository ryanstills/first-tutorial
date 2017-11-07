package com.ryanstillwagon.first_tutorial;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.ryanstillwagon.first_tutorial.screens.MainMenuScreen;
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
	public SpriteBatch batch;
	public BitmapFont font;
	public int winWidth;
	public int winHeight;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		winWidth = Gdx.graphics.getWidth();
		winHeight = Gdx.graphics.getHeight();
		this.setScreen(new MainMenuScreen(this));
	}
	@Override
	public void render () {
		super.render();

	}
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
