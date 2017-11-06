package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


class BlockLeft extends MovableBlocks {

    BlockLeft(int xPos, int yPos, TextureRegion texture){
        super(xPos, yPos, texture);
        contentsKey = 6;
    }

}
