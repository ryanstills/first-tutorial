package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

class BlockUp extends MovableBlocks{

    BlockUp(int xPos, int yPos, TextureRegion texture){
        super(xPos, yPos, texture);
        contentsKey = 5;
    }
}
