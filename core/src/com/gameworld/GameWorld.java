package com.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gamelogic.GameLogic;
import com.gameobjects.Board;
import com.gameobjects.Region;
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

    private Region newRegion;

    private Vector2 tileHitPosition;
    // containing all tiles in a rectangle from first tile to last tile (corner to corner)

    private GameLogic logic;

    public GameWorld(int rows, int cols){
        super(new ScreenViewport());

        board = new Board(rows, cols);
        logic = new GameLogic(board);

        addActor(board);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public Tile hit(float stageX, float stageY, boolean touchable) {
        if (super.hit(stageX, stageY, touchable) instanceof Tile){
            return (Tile)super.hit(stageX, stageY, touchable);
        }
        return null;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tileHitPosition = stageToScreenCoordinates(new Vector2(screenX, screenY));
        firstTile = hit(tileHitPosition.x, tileHitPosition.y, false);//what was touchable again (3rd param)
        //Gdx.app.log("hit","registered by stage");

        if (firstTile != null){

            newRegion = new Region();//instantiate a new region with a set color from list? (not implemented)

            firstTile.setSelected(true, newRegion.getColor());

            //System.out.println("(" + firstTile.getRow() + ", " + firstTile.getCol() + ")");
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

                board.select(firstTile, lastTile, newRegion.getColor());//selects a rectangular region and marks them
                //make sure that the selection color covers the existing tiles for visibility

                //maybe just do borders instead of coloring?

                //System.out.println("(" + currentTile.getRow() + ", " + currentTile.getCol() + ")");
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.log("touchUp", "registered");

        if (newRegion == null){//if touchDown happens at a non-tile, then newRegion is not created.
            return true;
        }

        if (logic.checkRegionCompliance()){//check if adding a new region still complies with the tatamibari rules
            board.addRegion(newRegion);//add new region which was instantiated in touchDown
        }

        //System.out.println(board.getX() + ", " + board.getY() + ", " + board.getWidth() + ", " + board.getHeight());

        //run some sort of game logic for rule checking e.g. logic member has board and and calls .checkRules function

        //if a new selection has overlaps with assigned tiles (checked at touchUp) then the old one will be invalidated.
        //need to make a board function to do that as well (keep assigned grouped together maybe use Group of actors?
        // each with diff color and symbol, which determines shape)

        //clean up
        board.clearSelection();

        firstTile = null;
        lastTile = null;
        currentTile = null;

        return true;
    }
}
