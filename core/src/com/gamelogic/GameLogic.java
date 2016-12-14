package com.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.gameobjects.Board;
import com.gameobjects.Region;
import com.gameobjects.Tile;

import java.util.*;

/**
 * Manages the logical operations of the game including rule checking and random problem generation
 */
public final class GameLogic {
    private Board board;
    private Random rand;
    public GameLogic(Board board){
        this.board = board;
        rand = new Random();
    }

    public boolean hasFourRegionCorner(){
        Map<Integer, Integer> cornerCount = new HashMap<Integer, Integer>();
        //assign each corner unique index by calculating
        //corner = row grid * number of column grids in a row + column grid
        int[] corners = new int[4];

        //calculate corner indices of each corner of each region and remember the count
        for (Region region : board.getRegions()){
            corners[0] = region.getBottomRow() * (board.getCols() + 1) + region.getLeftCol();//lower left corner
            corners[1] = region.getBottomRow() * (board.getCols() + 1) + region.getRightCol() + 1;//lower right corner
            corners[2] = (region.getTopRow() + 1) * (board.getCols() + 1) + region.getLeftCol();//upper left corner
            corners[3] = (region.getTopRow() + 1) * (board.getCols() + 1) + region.getRightCol() + 1;//upper right corner

            //store the corners into their corner indices
            for (int corner : corners){
                if (cornerCount.containsKey(corner)) {
                    cornerCount.put(corner, cornerCount.get(corner) + 1);
                }
                else{
                    cornerCount.put(corner, 1);
                }
            }
        }

        //see if any corner occurs 4 times
        for (int corner: cornerCount.keySet()){
            if (cornerCount.get(corner) >= 4){
                //System.out.println("Corner index " + corner + " has " + cornerCount.get(corner) + " corners.");
                return true;
            }
        }
        return false;
    }

    public boolean hasValidRegions(){
        //check if each region holds one (and only one) symbol and it satisfies the symbol requirement
        for (Region region : board.getRegions()){
            if (!(region.hasOneSymbol() && region.matchesSymbol())){
                return false;
            }
        }

        //check the 4-corner rule (four regions may not share the same corner)
        if (hasFourRegionCorner()){
            return false;
        }

        return true;
    }


    //a pair class used to hold divide direction (bool indicating vertical/horizontal) and divide index (int)
    private class Pair{
        private final boolean direction;//true = vertical, false = horizontal
        private final int divideIndex;

        public Pair(boolean direction, int divideIndex){
            super();
            this.direction = direction;
            this.divideIndex = divideIndex;
        }

        @Override
        public int hashCode() {
            int hashDirection = new Boolean(direction).hashCode();
            int hashIndex = divideIndex;
            return 31 * hashIndex + hashDirection;//31 chosen as a small prime for the hash
        }

        @Override
        public boolean equals(Object other) {//two pairs are equal if the members are equal
            if (other instanceof Pair){
                Pair otherPair = (Pair)other;
                return this.direction == otherPair.direction && this.divideIndex == otherPair.divideIndex;
            }
            return false;
        }

        @Override
        public String toString() {
            return (direction ? "vertical" : "horizontal") + "," + divideIndex;
        }

        public boolean getDirection(){
            return direction;
        }
        public int getDivideIndex(){
            return divideIndex;
        }
    }


    public void generateRandomProblem(int divisionDepth, int depthCoefficient){
        boolean randomized = false;//boolean indicating successful generation of a random problem
        while (!randomized){
            //System.out.println("---------------NEW PROBLEM-----------------");
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
            board.select(board.getTile(0, 0), board.getTile(board.getRows() - 1, board.getCols() - 1));
            Region newRegion = new Region(board);
            board.addRegion(newRegion);
            board.clearSelection();

            //divide region recursively
            divideRegion(divisionDepth, depthCoefficient, newRegion);

            //check if board is not very randomized (edge cases, e.g. the division extremely biased toward a single symbol)
            //then call this function again
            randomized = true;//initialize true

            //count the number of the types of regions
            int horizontal = 0, vertical = 0, square = 0;
            for (Region region : board.getRegions()){
                if (region.isHorizontal()) ++horizontal;
                else if (region.isVertical()) ++vertical;
                else ++square;

            }

            //if symbol ratios are extreme (e.g. horizontal:vertical is 4:1)
            double directionRatioCutoff = 4.0;
            if (horizontal == 0 || vertical == 0 || square == 0
                    || horizontal / vertical > directionRatioCutoff || vertical / horizontal > directionRatioCutoff
                    || square / vertical > directionRatioCutoff || square / horizontal > directionRatioCutoff){
                //System.out.println("Failed direction ratio check");
                randomized = false;
            }

            //if number of regions is too small or too large compared to board size
            double regionDensity = (double)board.getRegions().size() / (board.getRows() * board.getCols());
            if (regionDensity < 0.2 || regionDensity > 0.45) {
                //System.out.println("Failed region ratio check: " + regionDensity);
                randomized = false;
            }

            //one edge has mostly 1x1 squares, which limits possible divisions in other areas of the board
            //use an acceptable ratio r (up to r/100% squares on an edge is OK)
            double squaresCutoff = 0.5;
            int squaresTopRow = 0, squaresBottomRow = 0, squaresLeftCol = 0, squaresRightCol = 0;

            //iterate over all regions and check if it is a square on any edge of the board, and keep count
            for (Region region : board.getRegions()){
                if (region.isSquare()){
                    if (region.getBottomRow() == 0) ++squaresBottomRow;
                    else if (region.getTopRow() == board.getRows() - 1) ++squaresTopRow;
                    if (region.getLeftCol() == 0) ++squaresLeftCol;
                    else if (region.getRightCol() == board.getCols() - 1) ++squaresRightCol;
                }
            }

            //if number of squares on an edge exceeds cutoff, randomize again
            if (squaresTopRow > squaresCutoff * board.getCols() || squaresBottomRow > squaresCutoff * board.getCols()
                    || squaresLeftCol > squaresCutoff * board.getRows() || squaresRightCol > squaresCutoff * board.getRows()){
                //System.out.println("Failed edge squares check: " + squaresTopRow + " " +squaresBottomRow + " " +
                //squaresLeftCol + " " + squaresRightCol);
                randomized = false;
            }

            if (!randomized){//if not a good randomization, remove all regions
                board.removeAllRegions();
            }
        }//end while loop

        //now assign each region a symbol at a random location in the region
        for (Region region : board.getRegions()){
            //pick a random position
            int symbolTilePosition = rand.nextInt(region.getTiles().size());//[0 ... n-1]
            Tile symbolTile = region.getTiles().get(symbolTilePosition);
            Tile.Symbol s = Tile.Symbol.NONE;
            //assign appropriate symbol
            if (region.isVertical()){
                s = Tile.Symbol.VERTICAL;
            }
            else if (region.isHorizontal()){
                s = Tile.Symbol.HORIZONTAL;
            }
            else{//row and col are equal in number
                s = Tile.Symbol.SQUARE;
            }
            symbolTile.setSymbol(s);
        }

        //delete all regions, leaving only symbols (comment this out to see divided regions)
            board.removeAllRegions();
    }




    //takes a region, divides it and generates 2 new regions in place of the original one
    private void divideRegion(int depth, int depthCoefficient, Region region){
        //base cases
        if (depth <= 0) return;//no more recursion required by the recursion depth
        if (region.getTiles().size() <= 1) return;//region has only 1 tile or fewer

        //get first and last tiles of the original region then delete the region
        Tile region1FirstTile = region.getFirstTile();
        Tile region2LastTile = region.getLastTile();
        Tile region1LastTile, region2FirstTile;//determined based on where division occurs

        int originalRegionRows = region.getRows();//save the original number of rows and columns for the random generation
        int originalRegionCols = region.getCols();
        board.removeRegion(region);

        //randomly choose vertical/horizontal direction for division, one followed by the other (in case the first
        //direction does not have any valid divisions)
        //true: divide vertically (dividing line is vertical across the board)
        //false: divide horizontally (dividing line is horizontal across the board)
        //boolean divideVertical = rand.nextBoolean();
        /*
        boolean[] divideDirections = new boolean[2];
        divideDirections[0] = rand.nextBoolean();
        divideDirections[1] = !divideDirections[0];
        */

        boolean divided = false;//boolean to track if the region has successfully been divided
        //create the new regions but don't add them yet
        Region region1 = new Region(board);
        Region region2 = new Region(board);

        //fill array with all possible division (direction index pair)
        ArrayList<Pair> divPairs = new ArrayList<Pair>();
        //possible vertical divisions [1 ... c - 1]
        for (int i = 0; i < originalRegionCols - 1; i++) {
            divPairs.add(new Pair(true, i + 1));
        }
        //possible horizontal divisions [1 ... r - 1]
        for (int i = 0; i < originalRegionRows - 1; i++) {
            divPairs.add(new Pair(false, i + 1));
        }
        shuffleArray(divPairs);//shuffle the possible division options

        //iterate over the possible division indices and see if it is valid (i.e. does not violate four corner rule)
        for (Pair pair : divPairs){
            boolean divideVertical = pair.direction;
            int divideAt = pair.getDivideIndex();

            if (divideVertical){
                //vertical division means the rows are the same between the first tiles, and also between the last tiles
                region1LastTile = board.getTile(region2LastTile.getRow(), region1FirstTile.getCol() + divideAt - 1);
                region2FirstTile = board.getTile(region1FirstTile.getRow(), region1FirstTile.getCol() + divideAt);
            }
            else{
                //horizontal division so only the rows are separated
                region1LastTile = board.getTile(region1FirstTile.getRow() + divideAt - 1, region2LastTile.getCol());
                region2FirstTile = board.getTile(region1FirstTile.getRow() + divideAt, region1FirstTile.getCol());
            }

            //create the new regions and add to board
            board.select(region1FirstTile, region1LastTile);
            board.addRegion(region1);
            board.clearSelection();

            board.select(region2FirstTile, region2LastTile);
            board.addRegion(region2);
            board.clearSelection();

            //check that board is valid
            if (!hasFourRegionCorner()){//if valid, quit the loop
                /*
                System.out.println("has valid regions");
                System.out.println("region 1: " + region1);
                System.out.println("region 2: " + region2);
                */
                divided = true;
                break;
            }
            //if not, delete the regions and continue the loop

            /*
            System.out.println("invalid regions");
            System.out.println("region 1: " + region1);
            System.out.println("region 2: " + region2);
            */

            board.removeRegion(region1);
            board.removeRegion(region2);
        }

            //check if division is successful
            //if not, it means there are no valid division in the division direction randomly chosen
            //flip the direction and try again - track that both directions have been tried
        if (divided){
            //recursion - randomize depth reduction - randomly reduce by a number in range [1 ... 3], for example
            //determine the sweet spot range for a good generator by trial and error depending on board size

            //int depthReduction = 1;
            int depthReduction = rand.nextInt(depthCoefficient) + 1;//reduce depth randomly
            divideRegion(depth - depthReduction, depthCoefficient, region1);

            depthReduction = rand.nextInt(depthCoefficient) + 1;
            divideRegion(depth - depthReduction, depthCoefficient, region2);
        }
        if (!divided){//if a division has not occurred, restore the original region
            board.select(region1FirstTile, region2LastTile);
            board.addRegion(new Region(board));
            board.clearSelection();
        }
    }


    //Fisher-Yates shuffle to randomly shuffle an array
    //integer array needs an overloaded function since int is a primitive type
    private static void shuffleArray(int[] array){
        Random random = new Random();
        int k, temp;
        for (int i = array.length - 1; i > 0; i--){
            k = random.nextInt(i + 1);//random number in range [0 ... i]
            if (i != k){
                //swap elements
                temp = array[i];
                array[i] = array[k];
                array[k] = temp;
            }
        }
    }

    private static <T> void shuffleArray(ArrayList<T> list){
        Random random = new Random();
        int k;
        for (int i = list.size() - 1; i > 0; i--){
            k = random.nextInt(i + 1);//random number in range [0 ... i]
            if (i != k){
                Collections.swap(list, i, k);
            }
        }
    }
}
