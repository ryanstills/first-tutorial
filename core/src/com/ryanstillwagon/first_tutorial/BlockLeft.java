package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.Texture;


class BlockLeft extends MovableBlocks {

    BlockLeft(int xPos, int yPos, Texture texture){
        super(xPos, yPos, texture);
        contentsKey = 6;
    }

}
