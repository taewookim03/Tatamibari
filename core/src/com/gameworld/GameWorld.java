package com.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import java.util.List;
import java.util.Set;

/**
 * manages game objects (tile and board (group of tiles)
 *
 * due to simplicity don't use this class and just use the default Stage claess with custom input inside the TatamibariGame class?
 */
public class GameWorld extends Stage implements Screen {

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public enum GameState{//from menu, choose tile size etc. and run game.
        MENU, RUNNING, SOLVED
    }

    private GameState currentState;

    private Board board;

    private Tile currentTile;
    private Tile firstTile;
    private Tile lastTile;

    private Region newRegion;//cache
    private Vector2 tileHitPosition;//cache

    private GameLogic logic;

    public GameWorld(int rows, int cols){
        super(new ScreenViewport());

        currentState = GameState.RUNNING;
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

        if (currentState == GameState.RUNNING){
            tileHitPosition = stageToScreenCoordinates(new Vector2(screenX, screenY));
            firstTile = hit(tileHitPosition.x, tileHitPosition.y, false);//what was touchable again (3rd param)
            //Gdx.app.log("hit","registered by stage");

            if (firstTile != null){
                //implement region.doDraw thing here
                if (firstTile.getRegion() != null){
                    board.removeRegion(firstTile.getRegion());
                    //firstTile.getRegion().setDraw(false);//if necessary to restore invalidated region, replace above with this
                }

                newRegion = new Region(board);//instantiate a new region with a set color from list? (not implemented)

                firstTile.setSelected(true, newRegion.getColor());

                //System.out.println("(" + firstTile.getRow() + ", " + firstTile.getCol() + ")");
                lastTile = firstTile;
            }
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

                for (Tile tile : board.getRectangularSelection(firstTile, lastTile)){
                    if (tile.getRegion() != null){
                        board.removeRegion(tile.getRegion());
                        //currentTile.getRegion().setDraw(false);//if necessary to restore invalidated region, replace above with this
                    }
                }

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

        board.addRegion(newRegion);//add new region which was instantiated in touchDown

        /*
        System.out.println(newRegion.getBottomRow());
        System.out.println(newRegion.getTopRow());
        System.out.println(newRegion.getLeftCol());
        System.out.println(newRegion.getRightCol());
        */

        //System.out.println("one symbol: " + newRegion.hasOneSymbol());
        //System.out.println("symbol match: " + newRegion.matchesSymbol());

        if (logic.checkRegionCompliance()){//check if adding a new region still complies with the tatamibari rules
            /*
            List<Tile> tiles = newRegion.getTiles();
            System.out.println(tiles.get(0).getRow() + ", " + tiles.get(0).getCol() + ", " +
                    tiles.get(tiles.size()-1).getRow() + ", " +tiles.get(tiles.size()-1).getCol());
                    */

            //if compliant, clear existing region from overlapping tiles
            board.clearOverlappingRegions();
        }
        else{
            board.removeRegion(newRegion);
        }
        board.refreshRegions();

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

        //check if puzzle is solved
        if (board.isFilled()){
            currentState = GameState.SOLVED;
        }

        return true;
    }

    @Override
    public void draw() {
        //here draw additional stuff base on gamestate
        switch(currentState){
            case MENU:

                break;
            case RUNNING:
                super.draw();
                break;
            case SOLVED:
                super.draw();
                //add a text message saying congrats and show button to go back to main menu

                break;
        }

    }
}
