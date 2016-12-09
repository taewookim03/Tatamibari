package com.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.gameobjects.Board;
import com.gameobjects.Region;
import com.gameobjects.Tile;

import java.util.Random;

/**
 * Created by Taewoo Kim on 9/3/2016.
 */
public final class GameLogic {
    private Board board;
    private Random rand;
    public GameLogic(Board board){
        this.board = board;
        rand = new Random();
    }

    public boolean checkRegionCompliance(){
        //check if each region holds one (and only one) symbol and it satisfies the symbol requirement
        for (Region region : board.getRegions()){
            if (!(region.hasOneSymbol() && region.matchesSymbol())){
                return false;
            }
        }

        //check the 4-corner rule (four regions may not share the same corner)
        if (board.hasFourRegionCorner()){
            return false;
        }

        return true;
    }

    public void generateRandomProblem(){
        /*
        Random problem generation algorithm
        1. Select the entire board as a region
        2. Check if region is divisible. If not, stop.
        3. Randomly choose vertical or horizontal division
        4. Randomly choose which row/col to divide by - a random integer k in range [1 ... n-1], where
           k means the region will be separated between (k-1)th and kth row
        5. Check that division would be valid (board is divisible and all possible division satisfies the four corner rule).
        6. Divide into 2 regions based on random parameters from steps 3 and 4
        7. Repeat recursively (or maybe iteratively?) until desired number of regions is reached, or no more division
           is possible
        8. For each region, pick a random tile on the region and assign an appropriate symbol (-, |, +)
        9. Remove all regions created (but not the symbols)
         */

        //select entire board and add it as a region
        board.select(board.getTile(0, 0), board.getTile(board.getRows()-1, board.getCols()-1));
        Region newRegion = new Region(board);
        board.addRegion(newRegion);
        board.clearSelection();


        divideRegion(newRegion);

        //board.setSymbol(1, 3, Tile.Symbol.SQUARE);

    }

    /*
    takes a region, divides it and generates 2 new regions in place of the original one
     */
    private void divideRegion(Region region){
        //base cases
        if (region.getTiles().size() == 1) return;//region has only 1 tile
        



        //randomly choose vertical/horizontal division
        boolean divideVertical = rand.nextBoolean();
        //false: divide horizontally (dividing line is horizontal through the board)
        //true: divide vertically

        //randomly choose which row/column to divide at
        int divideAt;
        if (divideVertical) {//vertical division
            divideAt = rand.nextInt(board.getCols() - 1) + 1;//[1 ... numCols - 1]
        }
        else {//horizontal division
            divideAt = rand.nextInt(board.getRows() - 1) + 1;//[1 ... numRows - 1]
        }

        //get first and last tiles of the region then delete the region
        Tile region1FirstTile = region.getFirstTile();
        Tile region2LastTile = region.getLastTile();
        Tile region1LastTile, region2FirstTile;//determined based on where division occurs

        if (divideVertical){
            //vertical division means the rows are the same between the first tiles, and also between the last tiles
            region1LastTile = board.getTile(region2LastTile.getRow(), divideAt - 1);
            region2FirstTile = board.getTile(region1FirstTile.getRow(), divideAt);
        }
        else{
            //horizontal division so only the rows are separated
            region1LastTile = board.getTile(divideAt - 1, region2LastTile.getCol());
            region2FirstTile = board.getTile(divideAt, region1FirstTile.getCol());
        }
        board.removeRegion(region);

        //create the new regions
        Region region1 = new Region(board);
        Region region2 = new Region(board);

        board.select(region1FirstTile, region1LastTile);
        board.addRegion(region1);
        board.clearSelection();

        board.select(region2FirstTile, region2LastTile);
        board.addRegion(region2);
        board.clearSelection();

        //recursion
        //divideRegion(region1);
        //divideRegion(region2);
    }

}
