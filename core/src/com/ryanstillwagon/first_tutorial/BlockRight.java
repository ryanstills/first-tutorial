package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.Texture;

class BlockRight extends MovableBlocks {

    BlockRight(int xPos, int yPos, Texture texture){
        super(xPos, yPos, texture);
        contentsKey = 7;
    }
}
