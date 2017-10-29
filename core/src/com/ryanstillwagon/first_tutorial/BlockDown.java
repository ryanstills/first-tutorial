package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.Texture;

class BlockDown extends MovableBlocks {

    BlockDown(int xPos, int yPos, Texture texture){
        super(xPos, yPos, texture);
        contentsKey = 4;
    }
}
