package com.gamelogic;

import com.gameobjects.Board;
import com.gameobjects.Region;

/**
 * Created by Gayming on 9/3/2016.
 */
public class GameLogic {
    private Board board;

    public GameLogic(Board board){
        this.board = board;
    }

    public boolean checkRegionCompliance(){
        //check if each region holds one symbol and
        for (Region region : board.getRegions()){
            if (!(region.hasOneSymbol() && region.matchesSymbol())){//&& region matches symbol - add later
                return false;
            }
        }
        return true;
    }
}
