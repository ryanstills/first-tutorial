package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class MapContents {

    int xPos;
    int yPos;
    Sprite sprite;

    public static MapContents getContent(int xPos, int yPos, Sprite sprite, int key){
        switch (key){
            case 1:{
                return new BreakableWall(xPos, yPos, sprite);
            }
            case 2:{
                return new KeyObject(xPos, yPos, sprite);
            }
            default:{
                return null;
            }
        }
    }

    abstract int getXPos();
    abstract int getYPos();
    abstract Sprite getSprite();
}
