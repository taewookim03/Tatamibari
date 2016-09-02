package com.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gameobjects.Board;
import com.gameobjects.Tile;

import java.util.HashSet;
import java.util.Set;

/**
 * manages game objects (tile and board (group of tiles)
 *
 * due to simplicity don't use this class and just use the default Stage claess with custom input inside the TatamibariGame class?
 */
public class GameWorld extends Stage {

    private Board board;

    private Tile currentTile;
    private Tile firstTile;
    private Tile lastTile;

    private Vector2 tileHitPosition;
    private Set<Tile> tilesSelected;//this needs to be a rectangular region
    // containing all tiles in a rectangle from first tile to last tile (corner to corner)

    public GameWorld(int rows, int cols){
        super(new ScreenViewport());

        board = new Board(rows, cols);
        tilesSelected = new HashSet<Tile>();

        addActor(board);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public Tile hit(float stageX, float stageY, boolean touchable) {
        return (Tile)super.hit(stageX, stageY, touchable);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tileHitPosition = stageToScreenCoordinates(new Vector2(screenX, screenY));
        firstTile = hit(tileHitPosition.x, tileHitPosition.y, false);//what was touchable again (3rd param)
        //Gdx.app.log("hit","registered by stage");

        //encapsulate into a board function (something like addRegion)
        if (firstTile != null){
            tilesSelected.add(firstTile);

            board.clearSelection();
            firstTile.setSelected(true);

            System.out.println("(" + firstTile.getRow() + ", " + firstTile.getCol() + ")");
            lastTile = firstTile;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        tileHitPosition = stageToScreenCoordinates(new Vector2(screenX, screenY));
        currentTile = hit(tileHitPosition.x, tileHitPosition.y, false);
        //Gdx.app.log("drag","registered by stage");

        if (currentTile != null){//if a tile is hit
            if (currentTile != lastTile){//check to only run the following code if currentTile changes
                lastTile = currentTile;

                board.clearSelection();//to account for cases where selection shrinks
                board.select(firstTile, lastTile);//selects a rectangular region and marks them red (for now)

                System.out.println("(" + currentTile.getRow() + ", " + currentTile.getCol() + ")");
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        //clean up
        //set tiles' assigned to true to keep track of tiles that are assigned a color.
        board.assignColorToSelection(Color.RED);

        //if a new selection has overlaps with assigned tiles (checked at touchUp) then the old one will be invalidated.
        //need to make a board function to do that as well (keep assigned grouped together maybe use Group of actors?
        // each with diff color and symbol, which determines shape)

        firstTile = null;
        lastTile = null;
        currentTile = null;

        return true;
    }
}
