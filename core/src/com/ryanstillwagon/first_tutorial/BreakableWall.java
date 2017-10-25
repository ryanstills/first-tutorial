package com.ryanstillwagon.first_tutorial;

public class BreakableWall {

    private int xPos;
    private int yPos;

    public BreakableWall(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getXpos(){
        return xPos;
    }
    public int getYpos(){
        return yPos;
    }
}
