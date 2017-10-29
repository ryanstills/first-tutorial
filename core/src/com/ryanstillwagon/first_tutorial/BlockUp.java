package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.Texture;

class BlockUp extends MovableBlocks{

    BlockUp(int xPos, int yPos, Texture texture){
        super(xPos, yPos, texture);
        contentsKey = 5;
    }
}
