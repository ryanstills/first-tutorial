package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class MovableBlocks extends MapContents {

    protected int xPos;
    protected int yPos;
    protected Sprite sprite;

    public MovableBlocks(int xPos, int yPos, Sprite wallSprite){
        this.xPos = xPos;
        this.yPos = yPos;
        this.contentsKey = 3;
        this.sprite = wallSprite;
        mapContentsPositions.put(stringify(xPos, yPos), this);
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
        mapContentsPositions.remove(stringify(xPos, yPos));
        this.xPos += newXPos;
        mapContentsPositions.put(stringify(xPos, yPos), this);
        checkForBreakableBlock(stringify(xPos, (yPos)));
    }
    public void setYPos(int newYPos){
        mapContentsPositions.remove(stringify(xPos,yPos));
        this.yPos += newYPos;
        mapContentsPositions.put(stringify(xPos,yPos), this);
        checkForBreakableBlock(stringify(xPos, (yPos)));

    }
    public void setSprite(Sprite sprite){this.sprite = sprite;}

    public String stringify(int xPos, int yPos){
        return ( Integer.toString(xPos) + Integer.toString(yPos) );
    }

    private void checkForBreakableBlock(String positionKey){
        if(mapContentsPositions.containsKey(positionKey) &&
           mapContentsPositions.get(positionKey).getContentsKey() == 1){

            mapContentsPositions.remove(positionKey);
        }
    }
}
