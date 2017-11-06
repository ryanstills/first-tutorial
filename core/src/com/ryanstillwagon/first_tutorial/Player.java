package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends MapContents {

    Player(int xPos, int yPos, TextureRegion texture) {
        this.xPos = xPos;
        this.yPos = yPos;
        sprite = new Sprite(texture);
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
        this.xPos += xPos;
    }
    public void setYPos(int yPos){
        this.yPos += yPos;
    }
    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }



    private String stringify(int xPos, int yPos){
        return ( Integer.toString(xPos) + Integer.toString(yPos) );
    }
}
