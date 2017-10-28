package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

/*
Contents Key:
1 - breakable wall
2 - key object
3 - moveable block
4 - traps (not in use yet)
5 - player character
6 - enemy character
 */

abstract class MapContents {

    public static Map<String, MapContents> mapContentsPositions = new HashMap<String, MapContents>();
    protected int xPos;
    protected int yPos;
    protected int contentsKey;
    protected Sprite sprite;

    static MapContents getContent(int xPos, int yPos, Sprite sprite, int key){
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
    abstract int getContentsKey();
    abstract Sprite getSprite();
    abstract void setXPos(int xPos);
    abstract void setYPos(int yPos);
    abstract void setSprite(Sprite sprite);


}
