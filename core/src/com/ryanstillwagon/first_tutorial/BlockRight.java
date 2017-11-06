package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

class BlockRight extends MovableBlocks {

    BlockRight(int xPos, int yPos, TextureRegion texture){
        super(xPos, yPos, texture);
        contentsKey = 7;
    }
}
