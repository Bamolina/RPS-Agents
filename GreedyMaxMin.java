package tournament;

import games.*;

/**
 * @author Cristian Molina & Bryan Molina
 * @version 2019.05.10
 */
@SuppressWarnings("Duplicates")
public class GreedyMaxMin extends Player{
    protected final String newName = "GreedyMaxMin"; //Overwrite this variable in your player subclass

    /**Your constructor should look just like this*/
    public GreedyMaxMin() {
        super();
        playerName = newName;
    }

    /**
     * Initialize is called at beginning of tournament.
     * Use this time to decide on a strategy depending
     * on the parameters.
     */
    public void initialize(){
        //System.out.println(" "+getParameters().getDescription());
    }

    /**
     * THIS METHOD SHOULD BE OVERRIDDEN
     * GameMaster will call this to compute your strategy.
     * @param mg The game your agent will be playing
     * @param playerNumber Row Player = 0, Column Player = 1
     */
    protected MixedStrategy solveGame(MatrixGame mg, int playerNumber){
        MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber));
        int opponentNumber = 0;
        if (playerNumber == 0)
            opponentNumber = 1;
        int[][] winningPlaces = new int[mg.getNumActions(playerNumber)][mg.getNumActions(playerNumber)];
        makeWinningPlaces(mg, winningPlaces, playerNumber, opponentNumber);

        double[] opponentMS = opponentMS(opponentNumber, winningPlaces);
        double[] playerMS = playerMS(playerNumber, mg, opponentMS, opponentNumber);

        for (int i = 1; i <= playerMS.length; i++) {
            ms.setProb(i, playerMS[i-1]);
        }

        return ms;
    }


    private void makeWinningPlaces(MatrixGame mg, int[][] winningPlaces, int playerNumber, int opponentNumber){
        for (int row = 1; row <= mg.getNumActions(playerNumber); row++) {
            for (int column = 1; column <= mg.getNumActions(playerNumber); column++) {
                int[] actions = {row, column};
                double[] payoffs = mg.getPayoffs(actions);
                if(payoffs[playerNumber] < payoffs[opponentNumber]) {
                    winningPlaces[row-1][column-1] = 0;             //winning places is 0 indexed
                }
                else
                    winningPlaces[row-1][column-1] = 1;             //opponent wins at slots with 0
            }
        }

    }

    private double[] opponentMS(int opponentNumber,int[][] winningPlaces) {
        double[] opponentMS = {0, 0, 0};
        double max = 0;
        int counter = 0;
        for (int row = 1; row <= winningPlaces.length; row++){
            for (int column = 1; column <= winningPlaces.length; column++) {
                if(opponentNumber == 0){
                    if(winningPlaces[row-1][column-1] == 0)     //if the opponent wins in that slot
                        opponentMS[row-1] += 1;
                }
                else
                if(opponentNumber == 1){
                    if(winningPlaces[column-1][row-1] == 0)
                        opponentMS[row-1] = opponentMS[row-1] + 1;
                }
            }
        }
        for (double value: opponentMS) {
            if(value > max) {
                max = value;
            }
        }

        for (double value : opponentMS) {
            if (value == max)
                counter++;
        }
        for (int i = 0; i < opponentMS.length; i++) {
            if (opponentMS[i] != max)
                opponentMS[i] = 0;
            else opponentMS[i] = (double) 1 / counter;
        }

        return opponentMS;
    }


    private double[] playerMS(int playerNumber, MatrixGame mg, double[] opponentMS, int opponentNumber){
        double [] playerMS = {0,0,0};

        double max = Double.MIN_VALUE;
        int counter = 0;
        for (int row = 1; row <= mg.getNumActions(playerNumber); row++) {
            for (int column = 1; column <= mg.getNumActions(playerNumber); column++) {
                if (playerNumber == 0) {
                    if (opponentMS[column-1] != 0) {
                        int[] actions = {row, column};
                        double[] payoffs = mg.getPayoffs(actions);
                        double delta = payoffs[playerNumber] - payoffs[opponentNumber];
                        if (delta >= 0) {
                            if (delta >= this.param.getPayoffUncertainty()) {
                                playerMS[row - 1] = Double.MAX_VALUE;
                                break;
                            }

                            if (max <= delta) {
                                max = delta;
                                playerMS[row-1] = max;
                            }

                        }
                    }
                }
                else {
                    if (opponentMS[row-1] != 0) {
                        int[] actions = {row, column};
                        double[] payoffs = mg.getPayoffs(actions);
                        double delta = payoffs[playerNumber] - payoffs[opponentNumber];
                        if (delta >= 0) {
                            if (delta >= this.param.getPayoffUncertainty()) {
                                playerMS[column - 1] = Double.MAX_VALUE;
                                break;
                            }
                            if (max <= delta) {
                                max = delta;
                                playerMS[column-1] = max;
                            }

                        }
                    }
                }
            }
        }

        for (double value : playerMS) {
            if (value == max)
                counter++;
        }

        if (counter != 0) {
            for (int i = 0; i < playerMS.length; i++) {
                if (playerMS[i] != max)
                    playerMS[i] = 0;
                else playerMS[i] = (double) 1 / counter;
            }
        }
        else {//only happens when you can't win
            for (int i = 0; i < playerMS.length; i++){
                playerMS[i] = (double) 1 / playerMS.length;
            }
        }

        return playerMS;
    }
}
