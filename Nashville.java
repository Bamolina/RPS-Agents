package tournament;

import games.*;

/**
 * Sample agent that obtains the Nash equilibrium .
 * @author Cristian Molina & Bryan Molina
 * @version 2019.04.30
 */
public class Nashville extends Player{
    protected final String newName = "Nashville"; //Overwrite this variable in your player subclass

    /**Your constructor should look just like this*/
    public Nashville() {
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
     * @param playerNumber Row Player = 1, Column Player = 2
     */
    protected MixedStrategy solveGame(MatrixGame mg, int playerNumber){
        MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber));

        boolean[][] p1DominantCoordinates = new boolean[mg.getNumActions(playerNumber)][mg.getNumActions(playerNumber)]; //p1disbig
        boolean[][] p2DominantCoordinates = new boolean[mg.getNumActions(playerNumber)][mg.getNumActions(playerNumber)]; //p2disbig

        for (int row = 1; row < mg.getNumActions(playerNumber); row++) {
            double p1CurrentMax = Double.MIN_VALUE;
            double p2CurrentMax = Double.MIN_VALUE;

            for (int column = 1; column < mg.getNumActions(playerNumber); column++) {
                int[] p1Actions = {row, column};
                int[] p2Actions = {column, row};
                double[] payoffs = mg.getPayoffs(p1Actions);
                if (p1CurrentMax < payoffs[playerNumber]){
                    p1CurrentMax = payoffs[playerNumber];
                }
                payoffs = mg.getPayoffs(p2Actions);
                if (p2CurrentMax < payoffs[opponentNumber()-1]){
                    p2CurrentMax = payoffs[opponentNumber()-1];
                }
            }

            for (int column = 1; column < mg.getNumActions(playerNumber); column++) { //populates boolean 2d array
                int[] p1Actions = {row, column};
                int[] p2Actions = {column, row};
                double[] payoffs = mg.getPayoffs(p1Actions);
                if (p1CurrentMax == payoffs[playerNumber]){
                    p1DominantCoordinates[row][column] = true;
                }
                else p1DominantCoordinates[row][column] = false;
                payoffs = mg.getPayoffs(p2Actions);
                if(p2CurrentMax == payoffs[opponentNumber()-1]){
                    p2DominantCoordinates[column][row] = true;
                }
                else p2DominantCoordinates[column][row] = false;
            }
        }
        double trueMax = Double.MIN_VALUE;
        int[] coordinates = {0,0};

        for (int row = 1; row < mg.getNumActions(playerNumber); row++) {
            for (int column = 1; column < mg.getNumActions(playerNumber); column++) {
                boolean p1IsDominant = p1DominantCoordinates[row][column];
                boolean p2IsDominant = p2DominantCoordinates[row][column];
                if(p1IsDominant && p2IsDominant){
                    int[] actions = {row, column};
                    double[] payoffs = mg.getPayoffs(actions);
                    if(payoffs[playerNumber] > trueMax){
                        trueMax = payoffs[playerNumber];
                        coordinates[0]= row;
                        coordinates[1] = column;
                    }
                }
            }
        }
        for(int a = 0; a <= mg.getNumActions(playerNumber);a++) {
            ms.setProb(a, 0.0);
        }
        if(coordinates[0] != 0) {
            ms.setProb(coordinates[playerNumber] + 1, 1.0);
            return ms;
        }
        else {
            return new MixedStrategy(mg.getNumActions(playerNumber));
        }


    }



}
