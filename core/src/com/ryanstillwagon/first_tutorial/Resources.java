package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Resources {

    public final int TILE_SIZE = 32;
    TextureAtlas gameTextures;

    public TextureRegion titleScreen;
    public TextureRegion playerTexture;
    public TextureRegion breakableWallTexture;
    public TextureRegion keyTexture;
    public TextureRegion masterKeyTexture;
    public TextureRegion exitDoorTexture;
    public TextureRegion movableBlockAllTexture;
    public TextureRegion movableBlockUpTexture;
    public TextureRegion movableBlockDownTexture;
    public TextureRegion movableBlockLeftTexture;
    public TextureRegion movableBlockRightTexture;
    public TiledMap level;

    public Resources(){
        gameTextures = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));

        titleScreen = gameTextures.findRegion("title_screen");
        playerTexture = gameTextures.findRegion("placeholder_character");
        breakableWallTexture = gameTextures.findRegion("breakable_wall_green");
        keyTexture = gameTextures.findRegion("key");
        masterKeyTexture = gameTextures.findRegion("master_key");
        exitDoorTexture = gameTextures.findRegion("castledoors");
        movableBlockAllTexture = gameTextures.findRegion("block_any_direction");
        movableBlockUpTexture = gameTextures.findRegion("block_only_up");
        movableBlockDownTexture = gameTextures.findRegion("block_only_down");
        movableBlockLeftTexture = gameTextures.findRegion("block_only_left");
        movableBlockRightTexture = gameTextures.findRegion("block_only_right");

        level = new TmxMapLoader().load("sample_background.tmx");
    }

    public void dispose(){
        gameTextures.dispose();
        level.dispose();
    }
}
