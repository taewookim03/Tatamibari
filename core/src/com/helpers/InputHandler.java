package com.helpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.gameobjects.Board;
import com.gameobjects.Region;
import com.gameobjects.Tile;
import com.gameworld.GameWorld;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gamelogic.GameLogic;
import com.screens.MainMenuScreen;
import com.screens.SizeSelectionScreen;
import com.tatamibari.TatamibariGame;


/**
 * Handles user input
 */

public class InputHandler implements InputProcessor{
    private TatamibariGame game;
    private GameWorld world;
    private Board board;
    private GameLogic logic;

    //cache
    private Vector2 tileHitPosition;
    private Tile firstTile;
    private Tile lastTile;
    private Tile currentTile;
    private Region newRegion;


    public InputHandler(TatamibariGame game, GameWorld world){
        this.game = game;
        this.world = world;
        this.board = world.getBoard();
        this.logic = new GameLogic(board);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //System.out.println(endDialog.getX() + ", " + endDialog.getY());
        if (!world.isRunning()) {
            //System.out.println("InputHandler touchDown returning false");
            return false;
            //return world.touchDown(screenX, screenY, pointer, button);
        }

        tileHitPosition = world.stageToScreenCoordinates(new Vector2(screenX, screenY));
        if (world.hit(tileHitPosition.x, tileHitPosition.y, false) instanceof Tile){
            firstTile = (Tile)world.hit(tileHitPosition.x, tileHitPosition.y, false);//get the tile clicked
        }
        else return false;
        //Gdx.app.log("hit","registered by stage");

        if (firstTile != null){//if a tile was selected
            if (firstTile.getRegion() != null){//if the selected tile belongs to an existing region
                board.removeRegion(firstTile.getRegion());
                //firstTile.getRegion().setDraw(false);//if necessary to restore invalidated region, replace above with this
            }

            newRegion = new Region(board);//instantiate a new region with a set color from list? (not implemented)

            firstTile.setSelected(true, newRegion.getColor());

            //System.out.println("(" + firstTile.getRow() + ", " + firstTile.getCol() + ")");
            lastTile = firstTile;//update last tile selected
        }
        //System.out.println("InputHandler touchDown returning true");
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!world.isRunning()) return false;

        tileHitPosition = world.stageToScreenCoordinates(new Vector2(screenX, screenY));
        if (world.hit(tileHitPosition.x, tileHitPosition.y, false) instanceof Tile){
            currentTile = (Tile)world.hit(tileHitPosition.x, tileHitPosition.y, false);
        }
        else return false;
        //Gdx.app.log("drag","registered by stage");

        if (currentTile != null){//if a tile is hit
            if (currentTile != lastTile){//check to only run the following code if currentTile changes
                lastTile = currentTile;//update last tile selected

                for (Tile tile : board.getRectangularSelection(firstTile, lastTile)){
                    if (tile.getRegion() != null){
                        board.removeRegion(tile.getRegion());
                        //currentTile.getRegion().setDraw(false);//if necessary to restore invalidated region, replace above with this
                    }
                }

                board.clearSelection();//to account for cases where selection shrinks
                board.select(firstTile, lastTile, newRegion.getColor());//selects a rectangular region and marks them
                //make sure that the selection color covers the existing tiles for visibility


                //System.out.println("(" + currentTile.getRow() + ", " + currentTile.getCol() + ")");
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //update states when touch up, might want to encapsulate everything into an update() function if needed

        //Gdx.app.log("touchUp", "registered");

        if (!world.isRunning()){//if game is not running, do nothing
            return false;
        }
        if (newRegion == null){//if touchDown happens at a non-tile, then newRegion is not created.
            return false;
        }

        board.addRegion(newRegion);//add new region which was instantiated in touchDown based on tiles selected

        /*
        System.out.println(newRegion.getBottomRow());
        System.out.println(newRegion.getTopRow());
        System.out.println(newRegion.getLeftCol());
        System.out.println(newRegion.getRightCol());
        */

        //System.out.println("one symbol: " + newRegion.hasOneSymbol());
        //System.out.println("symbol match: " + newRegion.matchesSymbol());

        if (logic.hasValidRegions()){//check if adding a new region still complies with the tatamibari rules
            /*
            List<Tile> tiles = newRegion.getTiles();
            System.out.println(tiles.get(0).getRow() + ", " + tiles.get(0).getCol() + ", " +
                    tiles.get(tiles.size()-1).getRow() + ", " +tiles.get(tiles.size()-1).getCol());
                    */

            //if compliant, clear existing region from overlapping tiles
            board.clearOverlappingRegions();
        }
        else{//if the new region is illegal, remove it
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
        if (board.isFilled() && logic.hasValidRegions()){
            world.setSolved();
            //System.out.println("solved!");
            this.showMainMenuDialog();

            return true;
        }

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void showMainMenuDialog() {
        Dialog dialog = new Dialog("You solved it!", game.skin) {

            @Override
            protected void result(Object object) {
                boolean mainMenu = (Boolean) object;
                if (mainMenu) {
                    //go to size selection
                    game.setScreen(new SizeSelectionScreen(game));

                } else {
                    remove();
                }
            }

            @Override
            public Dialog show(Stage stage) {
                return super.show(stage);
            }

            @Override
            public void cancel() {
                super.cancel();
            }

            @Override
            public float getPrefHeight() {
                return Gdx.graphics.getHeight() / 5;
            }

            @Override
            public float getPrefWidth() {
                return getPrefHeight() * 2;
            }
        };

        //dialog.getButtonTable().pad(10).row();
        //dialog.getBackground().setMinHeight(500);
        TextButton backButton = new TextButton("Back", game.skin);
        dialog.button(backButton, true);
        //dialog.getButtonTable().setHeight(300);
        //dialog.button("Back", true);

        //dialog.button("", false);
        dialog.key(Input.Keys.ENTER, true);
        //dialog.key(Input.Keys.ESCAPE, false);
        dialog.setMovable(false);
        dialog.getTitleLabel().setAlignment(Align.center);
        dialog.show(world);
    }
}
