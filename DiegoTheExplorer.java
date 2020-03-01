package tournament;

import java.util.Random;
import games.*;

/**
 * Sample agent that obtains the MinMax Regret.
 * @author Cristian Molina & Bryan Molina
 * @version 2019.05.10
 */

public class DiegoTheExplorer extends Player{
    protected final String newName = "DiegoTheExplorer"; //Overwrite this variable in your player subclass

    /**Your constructor should look just like this*/
    public DiegoTheExplorer() {
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
        int round = this.getGameNumber();
        int lastPlayIndex = -1;
        boolean didIWin = false;


        /*fixing opponent number*/
        int opponentNumber = opponentNumber();
//                if(playerNumber == 0)
//                    opponentNumber = 1;


        /*getting index of my last play, if history exists*/
        if(history.size()>0) {
            lastPlayIndex = getIndexOfLastPlay(this, round) -1;
            didIWin=didIWin(this,opponentNumber);
        }


        if (history.size() == 0)
            return ms;
        else{
            for (int i = 1; i <= mg.getNumActions(playerNumber); i++) {
                if (i != lastPlayIndex){
                    if (didIWin){
                        double punishment = ms.getProb(i) - 1/30.0*2;
                        if(punishment < 0)
                            punishment = 0;
                        ms.setProb(i, punishment);
                    }

                    else {
                        double reward = ms.getProb(i) + (1/30.0)*4;
                        if (reward > 1)
                            reward = 1;
                        ms.setProb(i, reward);
                    }

                }

                else {

                    if (didIWin) {
                        double reward = ms.getProb(i) + (1/30.0)*4;
                        if (reward > 1)
                            reward = 1;
                        ms.setProb(i, reward);
                    }

                    else {
                        double punishment = (ms.getProb(i) - (1/30.0)*8);
                        if(punishment < 0)
                            punishment = 0;
                        ms.setProb(i, punishment);
                    }

                }
            }
        }

        /*explore every option once*/

//        double max = Double.MIN_VALUE;
//        for (int i = 1; i <= mg.getNumActions(playerNumber) ; i++) {
//            if (max < ms.getProb(i))
//                max = ms.getProb(i);
//        }
//
//        for (int i = 1; i <= mg.getNumActions(playerNumber) ; i++) {
//            if (ms.getProb(i) == max)
//                ms.setProb(i, 1.0);
//            else
//                ms.setProb(i, 0.0);
//        }
        double sum = 0;
        for (int i = 1; i <= mg.getNumActions(playerNumber); i++) {
            sum += ms.getProb(i);
        }
        if (sum > 1.0 || sum < 1.0) {
            double max = Double.MIN_VALUE;
            int counter = mg.getNumActions(playerNumber);
            for (int i = 1; i <= mg.getNumActions(playerNumber); i++) {
                if (max < ms.getProb(i)) {
                    max = ms.getProb(i);
                    counter--;
                }
            }

            if (counter == 1) {
                for (int i = 1; i <= mg.getNumActions(playerNumber); i++) {
                    if (ms.getProb(i) != max && ms.getProb(i) != 0)
                        ms.setProb(i, 1 - max);
                }
            }
            if (counter == 2){
                for (int i = 1; i <= mg.getNumActions(playerNumber); i++) {
                    if (ms.getProb(i) != max && counter == 2) {
                        ms.setProb(i, 1 - (2*max));
                        counter--;
                    }
                }
            }
        }

        return ms;

    }


    private int getIndexOfLastPlay(Player p, int round){
        int i = 1;
        for (; i <=3 ; i++) {
            if(round < 1) {
                double temp = p.history.get(0)[playerNumber-1].getProb(i);
                if (temp == 1.0)
                    break;
            }
            else {
                double temp = p.history.get(0)[playerNumber-1].getProb(i);
                if (temp == 1.0)
                    break;
            }
        }
        return i;
    }

    private boolean didIWin(Player p, int opponentNumber){
        double result = p.lastPayoffs[playerNumber-1] - p.lastPayoffs[opponentNumber-1];
        if(result > 0)
            return true;
        return false;
    }


}
