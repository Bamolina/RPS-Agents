package tournament;

import java.util.ArrayList;

import games.*;
import util.Parameters;

/**
 * Player agent.
 *
 * NOTE TO STUDENTS: The game master will only give player a copy of the game and tell you which player you are.
 * Your role is figure out your strategy.
 *
 * @author Marcus Gutierrez and Oscar Veliz
 * @version 04/15/2015
 */
public abstract class Player{
    protected String playerName = "defaultPlayer"; //Overwrite this variable in your player subclass
    protected MatrixGame game;
    private int gameNumber;
    protected int playerNumber;
    private ArrayList<StrategyHolder> strategies;//interal saved for later use by GameMaster
    private ArrayList<MixedStrategy[]> history;
    private double lastPayoffs[];
    private Parameters param;
    /**
     * Default Constructor
     */
    public Player(){
        strategies = new ArrayList<StrategyHolder>();
        history = new ArrayList<MixedStrategy[]>();
        lastPayoffs = new double[2];
    }
    
    /**
     * Overwrite me
     */
    public void initialize(){
	}

    /**
     * Set game
     * @param game the game in matrix form
     */
    public void setGame(MatrixGame game){
    	this.game = game;
    }
    /**
     * Sets the number of the game
     * @param gameNumber number of the game
     */
    public void setGame(int gameNumber){
    	this.gameNumber = gameNumber;
    }
    /**
     * Standard accessor
     * @return the game number
     */
    public int getGameNumber(){
    	return gameNumber;
    }
    /**
     * Set player number
     * @param playerNumber the player number (starts at 1)
     */
    public void setPlayerNumber(int playerNumber){
    	this.playerNumber = playerNumber;
    }
    /**
     * Standard accessor get current player number
     * @return the player number
     */
    public int getPlayerNumber(){
    	return playerNumber;
    }
    
    /**
     * Standard accessor get opponent player number
     * @return the opponent number
     */
    public int getOpponentNumber(){
		if(playerNumber == 1)
			return 2;
		else
			return 1;
    }

    /**
     * Get Agent Name used by GameMaster.
     * @return Name of player
     */
    public String getName(){return playerName;}

    /**
     * Player logic goes here in extended super agent. Do not try to edit this agent
     * @param mg the game
     * @param playerNum the player number
     * @return the mixed strategy
     */
    protected MixedStrategy solveGame(MatrixGame mg, int playerNum){
    	this.setGame(mg);
    	this.setPlayerNumber(playerNum);
    	return this.solveGame();
    
    }
    /**
     * Wrapper for the solveGame function
     * @return the mixed strategy developed by the player
     */
    protected MixedStrategy solveGame(){
    	return this.solveGame(this.game, this.playerNumber-1);
    }
    /**
     * Game Master stores a copy of the player strategies inside the player.
     * @param index Game number
     * @param ms Agent's strategy in the game when playing as playerNum
     * @param playerNum Row Player = 1, Column Player = 2
     */
    public void addStrategy(int index, MixedStrategy ms, int playerNum){
    	if(strategies.size() == index)
    		strategies.add(new StrategyHolder());
    	strategies.get(index).addStrategy(ms, playerNum);
    }
    /**
     * Standard accessor
     * @param index Game Number
     * @param playerNum Row Player = 1, Column Player = 2
     * @return the mixed strategy
     */
    public MixedStrategy getStrategy(int index, int playerNum){
    	if(index > strategies.size())
    		return null;
    	return strategies.get(index).getStrategy(playerNum);
    }
    
    /**
     * Standard setter
     * @param p new parameter settings
     */
    public void setParameters(Parameters p){
		param = p;
	}
	
	/**
	 * Standard getter
	 * @return current parameters
	 */
	public Parameters getParameters(){
		return param;
	}
	
	/**
	 * Clear the history
	 */
	public void resetHistory(){
		history.clear();
		lastPayoffs[0] = 0;
		lastPayoffs[1] = 0;
	}
	
	/**
	 * Add to history
	 * @param s1 strategy for player 1
	 * @param s2 strategy for player 2
	 */
	public void addHistory(MixedStrategy s1, MixedStrategy s2){
		MixedStrategy ms[] = new MixedStrategy[2];
		ms[0] = new MixedStrategy(s1.getProbs());
		ms[1] = new MixedStrategy(s2.getProbs());
		history.add(ms);
	}
	
	/**
	 * Add to history
	 * @param strats strategy for both players
	 */
	public void addHistory(MixedStrategy strats[]){
		this.addHistory(strats[0],strats[1]);
	}
	
	/**
	 * Informs player of the number of times previously run
	 * @return number of times previously run
	 */
	public int getCurrentRepeatCount(){
		return history.size();
	}
	
	/**
	 * Save last payoffs
	 * @param payoffs both players payoffs {p1,p2}
	 */
	public void saveLastPayoffs(double payoffs[]){
		lastPayoffs[0] = payoffs[0];
		lastPayoffs[1] = payoffs[1];
	}
	
}
