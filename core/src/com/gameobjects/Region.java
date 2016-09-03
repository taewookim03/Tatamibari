package com.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private ShapeRenderer sr;
    private Vector2 maxXY, minXY;//for drawing the region (borders)

    private static final float BORDER_THICKNESS = 3.0f;

    public Region(Color color){
        this.color = color;//idea: maybe color-code -, |, + ?
        tiles = new ArrayList<Tile>();
        sr = new ShapeRenderer();
        maxXY = new Vector2(0, 0);
        minXY = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public Color getColor(){
        return color;
    }

    public void addTile(Tile tile){
        tile.setRegion(this);
        tile.setColor(color);
        tiles.add(tile);

        //check to update min/max corner coordinates of the region
        Vector2 tilePosition = new Vector2(tile.getParent().getX() + tile.getX(), tile.getParent().getY() + tile.getY());
        if (tilePosition.x < minXY.x){
            minXY.x = tilePosition.x;
        }
        if (tilePosition.x + tile.getWidth() > maxXY.x){
            maxXY.x = tilePosition.x + tile.getWidth();
        }
        if (tilePosition.y < minXY.y){
            minXY.y = tilePosition.y;
        }
        if (tilePosition.y + tile.getHeight() > maxXY.y){
            maxXY.y = tilePosition.y + tile.getHeight();
        }

    }
    //need some kind of a container of colors to assign to each region and free up as a region is deleted, etc.

    public void clearRegionFromTiles(){
        for (Tile tile : tiles){
            tile.setRegion(null);
            tile.setColor(Color.WHITE);
        }
        tiles.clear();

    }

    public void draw(Batch batch, float parentAlpha){
        //Gdx.app.log("region", "draw function called");

        //drawing border for the region
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);

        sr.rectLine(minXY.x, maxXY.y, maxXY.x, maxXY.y, BORDER_THICKNESS);//top
        sr.rectLine(minXY.x, minXY.y, maxXY.x, minXY.y, BORDER_THICKNESS);//bottom
        sr.rectLine(minXY.x, minXY.y, minXY.x, maxXY.y, BORDER_THICKNESS);//left
        sr.rectLine(maxXY.x, minXY.y, maxXY.x, maxXY.y, BORDER_THICKNESS);//right

        sr.end();

    }

}
