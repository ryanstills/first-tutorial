package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Resources {

    public final int TILE_SIZE = 32;
    TextureAtlas gameTextures;

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

    public Resources(){
        gameTextures = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
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
    }

    public void dispose(){
        gameTextures.dispose();
    }
}
