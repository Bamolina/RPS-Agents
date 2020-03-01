package tournament;

import games.*;

/**
 * Sample agent that obtains the MinMax Regret.
 * @author Cristian Molina & Bryan Molina
 * @version 2019.04.30
 */

public class MinRegret extends Player{
    protected final String newName = "MinRegret"; //Overwrite this variable in your player subclass

    /**Your constructor should look just like this*/
    public MinRegret() {
        super();
        playerName = newName;
    }

    /**
     * Initialize is called at beginning of tournament.
     * Use this time to decide on a strategy depending
     * on the parameters.
     */
    public void initialize(){
        //System.out.println("Solid Rock "+getParameters().getDescription());
    }

    /**
     * THIS METHOD SHOULD BE OVERRIDDEN
     * GameMaster will call this to compute your strategy.
     * @param mg The game your agent will be playing
     * @param playerNumber Row Player = 0, Column Player = 1
     */
    protected MixedStrategy solveGame(MatrixGame mg, int playerNumber){
        MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber));
        int numberOfActions = mg.getNumActions(playerNumber);
        double[][] regretTable = null;
        int minMaxIndex = 1;
        double[] bestPayoffs = new double[numberOfActions];
        for (int column = 1; column <= numberOfActions; column++) {
            for (int row = 1; row <= numberOfActions; row++) {
                int[] actions = {row, column};
                double[] payoffs = mg.getPayoffs(actions);
                if (bestPayoffs[column-1] < payoffs[playerNumber]){ //was bestPayoffs[column]
                    bestPayoffs[column-1] = payoffs[playerNumber];
                }
            }
            regretTable = regretTable(mg,playerNumber,bestPayoffs);
        }
        minMaxIndex = rowIndex(regretTable);

        for(int a = 0; a <= numberOfActions;a++) {
            ms.setProb(a, 0.0);
        }
        ms.setProb(minMaxIndex, 1.0);


        return ms;

    }
    //TODO
    //FIX HELPER METHODS
    //INDEX OUT OF BOUNDS
    //DEBUG PLS
    //
//debug regret table
    private double[][] regretTable(MatrixGame mg, int playerNumber, double[] bestPayoffs){
        double[][] regretTable = new double[mg.getNumActions(playerNumber)][mg.getNumActions(playerNumber)];
        for (int column = 1; column < mg.getNumActions(playerNumber); column++) { //was 0 index
            for (int row = 1; row < mg.getNumActions(playerNumber); row++) { //was 0 index
                int[] actions = {row, column};
                double[] payoffs = mg.getPayoffs(actions);
                regretTable[row][column] = bestPayoffs[column] - payoffs[playerNumber];
            }

        }
        return regretTable;
    }

    private int rowIndex(double[][] regretTable){
        double minMaxRegret = Double.MIN_VALUE;
        int minMaxIndex = 0;
        for (int row = 0; row < regretTable.length; row++) {
            double currentMaxRegret = Double.MIN_VALUE;
            for (int column = 0; column < regretTable[row].length; column++) {
                if(currentMaxRegret < regretTable[row][column])
                    currentMaxRegret = regretTable[row][column];
            }
            if (minMaxRegret < currentMaxRegret){
                minMaxRegret = currentMaxRegret;
                minMaxIndex = row;          //WHAT IF COL PLAYER
            }
        }
        return minMaxIndex + 1; //row+1 since ms uses row index + 1
    }
    
    private void arrayPrep(double[] array){
        for (int i = 0; i < array.length; i++) {
            array[i] = Double.MIN_VALUE;
        }
    }
}
