package tournament;

import games.*;

import java.util.ArrayList;

/**
 * Sample agent that obtains the MaxMin Payoff.
 * @author Cristian Molina & Bryan Molina
 * @version 2019.04.30
 */

public class MaxMin extends Player{
    protected final String newName = "MaxMin"; //Overwrite this variable in your player subclass

    /**Your constructor should look just like this*/
    public MaxMin() {
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

        double[] minValues = new double[mg.getNumActions(playerNumber)];
        int maxMinIndex = 1;
        for (int row = 1; row < mg.getNumActions(playerNumber); row++) {
            double minValue = Double.MAX_VALUE;
            for (int column = 1; column < mg.getNumActions(playerNumber); column++) {
                int[] actions = {row, column};
                double[] payoffs = mg.getPayoffs(actions);
                if (minValue > payoffs[playerNumber]){
                    minValue = payoffs[playerNumber];
                }
            }
            minValues[row] = minValue;
        }
        double currentMax = Double.MIN_VALUE;

        for (int i = 0; i < minValues.length; i++) {
            if(minValues[i] > currentMax){
                currentMax = minValues[i];
                maxMinIndex = i+1;
            }
        }

        for(int a = 0; a <= mg.getNumActions(playerNumber);a++) {
                ms.setProb(a, 0.0);
        }
        ms.setProb(maxMinIndex, 1.0);


        return ms;
    }

}
