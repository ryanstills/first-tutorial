package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MovableBlocks extends MapContents {

    protected int xPos;
    protected int yPos;
    protected Sprite sprite;

    public MovableBlocks(int xPos, int yPos, TextureRegion texture){
        this.xPos = xPos;
        this.yPos = yPos;
        sprite = new Sprite(texture);
        mapContentsPositions.put(stringify(xPos, yPos), this);
        movableBlockPositions.put(stringify(xPos, yPos), this);
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

    public void setXPos(int newXPos){
        movableBlockPositions.remove(stringify(xPos, yPos));
        this.xPos += newXPos;
        movableBlockPositions.put(stringify(xPos, yPos), this);
    }
    public void setYPos(int newYPos){
        movableBlockPositions.remove(stringify(xPos,yPos));
        this.yPos += newYPos;
        movableBlockPositions.put(stringify(xPos,yPos), this);
    }
    public void setSprite(Sprite sprite){this.sprite = sprite;}

    public String stringify(int xPos, int yPos){
        return ( Integer.toString(xPos) + Integer.toString(yPos) );
    }
}
