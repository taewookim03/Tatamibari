package com.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.gameobjects.Board;
import com.gameobjects.Region;
import com.gameobjects.Tile;

/**
 * Created by Taewoo Kim on 9/3/2016.
 */
public final class GameLogic {
    private Board board;

    public GameLogic(Board board){
        this.board = board;
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
        5. Divide into 2 regions based on steps 3 and 4
        6. Repeat recursively (or maybe iteratively?) until desired number of regions is reached, or no more division
           is possible
        7. For each region, pick a random tile on the region and assign an appropriate symbol (-, |, +)
        8. Remove all regions created (but not the symbols)
         */

        //select entire board and add it as a region
        board.select(board.getTile(0, 0), board.getTile(board.getRows()-1, board.getCols()-1), Color.GREEN);
        board.addRegion(new Region(board));

        //board.setSymbol(2, 2, Tile.Symbol.SQUARE);

    }
}
