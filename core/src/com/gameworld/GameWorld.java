package com.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
public class GameWorld extends Stage {

    public enum GameState{//from menu, choose tile size etc. and run game.
        RUNNING, SOLVED//MENU is handled by a separate screen
    }

    private GameState currentState;

    private Board board;

    private Tile currentTile;
    private Tile firstTile;
    private Tile lastTile;

    private Region newRegion;//cache
    private Vector2 tileHitPosition;//cache

    private GameLogic logic;

    //UI
    private Skin skin;
    //public TextButton toMenuButton;
    public Dialog endDialog;
    public boolean endDialogShowing = false;

    public GameWorld(int rows, int cols){
        super(new ScreenViewport());

        currentState = GameState.RUNNING;
        board = new Board(rows, cols);
        logic = new GameLogic(board);

        //UI
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        /*
        toMenuButton = new TextButton("Return", skin, "default");
        //toMenuButton.setWidth(100);
        //toMenuButton.setHeight(20);

        addActor(toMenuButton);

        toMenuButton.setColor(Color.RED);

        //toMenuButton.setVisible(false);
        toMenuButton.toFront();
        toMenuButton.setPosition(Gdx.graphics.getWidth()/2 - toMenuButton.getWidth()/2,
                Gdx.graphics.getHeight()/2 - toMenuButton.getHeight()/2 - 50);

        toMenuButton.setPosition(0,0);

        toMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("button clicked");
            }
        });
        */
        addActor(board);


        endDialog = new Dialog("Click Message", skin);

        //endDialog.setModal(true);
        //endDialog.setMovable(false);
        //endDialog.setResizable(false);
        //endDialog.show(this).setPosition(100,100);
        endDialog.setName("endDialog");

        Button b = new TextButton("A", skin, "default");

        b.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Clicked", "Yes you did");
            }
        });
        endDialog.button(b);


        //addActor(board);
        //endDialog.show(this);

        //addActor(endDialog);
        //endDialog.hide();

        //endDialog.toFront();


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

        //System.out.println(endDialog.getX() + ", " + endDialog.getY());

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

        //update states when touch up, might want to encapsulate everything into an update() function if needed

        //Gdx.app.log("touchUp", "registered");

        if (newRegion == null){//if touchDown happens at a non-tile, then newRegion is not created.
            return true;
        }
        if (endDialogShowing){
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

        //check if puzzle is solved (it is solved if board is filled completely assuming each region follows the rules)
        if (board.isFilled()){
            currentState = GameState.SOLVED;
            System.out.println("solved!");
            endDialog.show(this).setPosition(0, 100);
            endDialogShowing = true;
            /*
            for (Actor a : this.getActors()){
                a.setTouchable(Touchable.enabled);
            }
            */
        }

            /*
            game.batch.begin();
            game.font.setColor(Color.PINK);
            game.font.draw(game.batch, "Click to go back to main menu", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
            game.batch.end();
            */
            //world.endDialog.show(world).setPosition(100,100);
            //world.toMenuButton.setVisible(true);

        return true;
    }

    public GameState getState(){
        return currentState;
    }
}
