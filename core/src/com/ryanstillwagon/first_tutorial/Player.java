package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Character {

    Player(int xPos, int yPos, Sprite sprite) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.sprite = sprite;
        contentsKey = 3;
    }

    public int getXPos(){
        return xPos;
    }
    public int getYPos(){
        return yPos;
    }
    public int getContentsKey(){
        return contentsKey;
    }
    public Sprite getSprite(){
        return sprite;
    }
    public void setXPos(int xPos){
        this.xPos = xPos;
    }
    public void setYPos(int yPos){
        this.yPos = yPos;
    }
    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

    public void update(int movementTime){
    }

    private String stringify(int xPos, int yPos){
        return ( Integer.toString(xPos) + Integer.toString(yPos) );
    }
}
