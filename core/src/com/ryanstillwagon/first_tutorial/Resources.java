package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Resources {

    TextureAtlas gameTextures;

    TextureRegion playerTexture;
    TextureRegion breakableWallTexture;
    TextureRegion keyTexture;
    TextureRegion masterKeyTexture;
    TextureRegion exitDoorTexture;
    TextureRegion movableBlockAllTexture;
    TextureRegion movableBlockUpTexture;
    TextureRegion movableBlockDownTexture;
    TextureRegion movableBlockLeftTexture;
    TextureRegion movableBlockRightTexture;

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
