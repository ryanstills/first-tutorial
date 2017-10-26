package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class KeyObject extends MapContents {
    private int xPos;
    private int yPos;
    private Sprite sprite;

    public KeyObject(int xPos, int yPos, Sprite wallSprite){
        this.xPos = xPos;
        this.yPos = yPos;
        this.sprite = wallSprite;
    }

    public int getXPos(){
        return xPos;
    }
    public int getYPos(){
        return yPos;
    }
    public Sprite getSprite(){return sprite;}
}
