package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

class BlockAllDirections extends MovableBlocks {

    BlockAllDirections(int xPos, int yPos, TextureRegion texture){
        super(xPos, yPos, texture);
        contentsKey = 3;
    }
}
