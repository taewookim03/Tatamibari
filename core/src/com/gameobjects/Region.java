package com.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gayming on 9/1/2016.
 */
public class Region {
    //Region contains tiles and stores information about the region such as:
    //symbol, symbol location, all tiles selected (part of group), color, etc.
    private Color color;
    private Tile symbolTile;
    private List<Tile> tiles;

    public Region(Color color){
        this.color = color;
        tiles = new ArrayList<Tile>();
    }
    public void addTile(Tile tile){
        tiles.add(tile);
    }

}
