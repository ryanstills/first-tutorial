package com.ryanstillwagon.first_tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

/*
Contents Key:
1 - breakable wall
2 - key object
3 - all direction movable block
4 - down only movable block
5 - up only movable block
6 - left only movable block
7 - right only movable block
8 - traps (not in use yet)
9 - player character
10 - enemy character (not in use yet)
*/

abstract class MapContents {

    public static Map<String, MapContents> mapContentsPositions = new HashMap<String, MapContents>();
    protected int xPos;
    protected int yPos;
    protected int contentsKey;
    protected Sprite sprite;

    static MapContents getContent(int xPos, int yPos, Texture[] textures, int key){
        switch (key){
            case 1:{
                return new BreakableWall(xPos, yPos, textures[1]);
            }
            case 2:{
                return new KeyObject(xPos, yPos, textures[2]);
            }
            case 3:{
                return new BlockAllDirections(xPos, yPos, textures[3]);
            }
            case 4:{
                return new BlockDown(xPos, yPos, textures[4]);
            }
            case 5:{
                return new BlockUp(xPos, yPos, textures[5]);
            }
            case 6:{
                return new BlockLeft(xPos, yPos, textures[6]);
            }
            case 7:{
                return new BlockRight(xPos,yPos,textures[7]);
            }
            case 9:{
                return new Player(xPos, yPos, textures[9]);
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
