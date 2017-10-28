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
        contentsKey = 2;
        mapContentsPositions.put(stringify(xPos,yPos), this);
    }

    public int getXPos(){
        return xPos;
    }
    public int getYPos(){
        return yPos;
    }
    public int getContentsKey(){return contentsKey;}
    public Sprite getSprite(){
        return sprite;
    }

    public void setXPos(int xPos){this.xPos = xPos;}
    public void setYPos(int yPos){this.yPos = yPos;}
    public void setSprite(Sprite sprite){this.sprite = sprite;}

    public String stringify(int xPos, int yPos){
        return ( Integer.toString(xPos) + Integer.toString(yPos) );
    }
}
