package com.ryanstillwagon.first_tutorial.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

public class MapsManager {

//    private int[] map = {2,1,0,0,0,0,0,0,0,0,1,0,1,1,1,1,2,1,
//            0,1,0,0,3,1,0,1,0,1,0,0,0,0,0,1,1,1,
//            0,1,1,0,0,0,1,0,0,0,0,3,1,0,1,0,0,1,
//            0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,1,0,0,
//            1,1,1,1,0,3,0,4,0,1,1,1,0,1,0,0,1,0,
//            0,0,1,2,1,0,0,0,0,1,2,1,0,1,0,3,0,0,
//            1,0,1,1,1,0,0,0,0,1,1,1,0,0,0,9,0,0,
//            0,0,1,0,0,1,0,1,0,1,0,0,1,0,0,0,0,1,
//            1,1,0,1,2,1,0,0,0,0,0,1,1,0,1,1,0,1,
//            1,1,0,0,0,1,0,1,0,1,0,1,0,0,0,3,0,0};

    private ArrayList<int []> mapList;
    private FileHandle[] mapFiles;

    public MapsManager(){
        mapList = new ArrayList<int []>();
        mapFiles = Gdx.files.local("maps/").list();
        loadMapList();
    }
    private void loadMapList(){
        for(FileHandle map : mapFiles){
            int[] tempMap = new int[233];
            String testString = map.readString();
            for(int i = 0; i < testString.length(); i++) {
                tempMap[i] = (int)testString.charAt(i) - 48;
            }
            mapList.add(tempMap);
        }
    }
    public int[] getMap(int level){
        return mapList.get(level - 1);
    }
}
