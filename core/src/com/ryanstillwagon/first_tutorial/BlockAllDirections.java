package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.Texture;

class BlockAllDirections extends MovableBlocks {

    BlockAllDirections(int xPos, int yPos, Texture texture){
        super(xPos, yPos, texture);
        contentsKey = 3;
    }
}
