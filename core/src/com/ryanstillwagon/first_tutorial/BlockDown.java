package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

class BlockDown extends MovableBlocks {

    BlockDown(int xPos, int yPos, TextureRegion texture){
        super(xPos, yPos, texture);
        contentsKey = 4;
    }
}
