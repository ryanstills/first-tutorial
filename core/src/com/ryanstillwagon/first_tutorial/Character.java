package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;

abstract class Character {

    protected int xPos;
    protected int yPos;
    protected int contentsKey;
    protected Sprite sprite;

    static Character getCharacter(int xPos, int yPos, Sprite sprite, int key){

        switch (key){
            case 1: {
                return  new Player(xPos, yPos, sprite);
            }
            default:{
                return null;
            }
        }
    }

    abstract int getXPos();
    abstract int getYPos();
    abstract int getContentsKey();
    abstract Sprite getSprite();

    abstract void setXPos(int xPos);
    abstract void setYPos(int yPos);
    abstract void setSprite(Sprite sprite);
}
