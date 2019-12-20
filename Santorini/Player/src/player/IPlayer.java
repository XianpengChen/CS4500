package player;


import java.util.List;

import board.IReadonlyBoard;
import data.Action;
import data.Outcome;
import data.PlaceWorkerAction;
import strategy.Strategy;


//TODO those breaker, infinite players should extend from Player rather implementing this interface?

/**
 * This a representation of how our player class will be able to
 * make decisions in the context of santorini. This is important as it allows us
 * to make concrete implementations for how a player will act whether it be
 * via an AI implementation, Network implementation of a remote player, or
 * a local console implementation
 */
public interface IPlayer {
    /**
     * This method is used to get an action that places a worker on a common.board. It has it's own method
     * since placing a worker is fundamentally different from a normal turn, which the IActor will make the
     * rest of the game
     *
     * @param b IReadonlyBoard representation of the common.board to make a decision with
     * @return the specified action for placing a working at the beginning
     */
    PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) throws InterruptedException;

    /**
     * This methods gets a normal turn's worth of actions from the IActor, as defined by what each implementation of
     * IActor thinks a turn is
     *
     * @param b IReadonlyBoard representation of the baord to make a decision with
     * @return List<Action> which denotes a sequence of atomic actions on the common.board making up a turn
     * of santorini
     */
    List<Action> getTurn(IReadonlyBoard b) throws InterruptedException;

    /**
     * Method to get the name of this player from the source of input, printing a message to humans
     * or receiving a String from an AI
     *
     * @return Gets the name of this player
     */
    String getPlayerName();

    /**
     * receive outcome from referee;
     *
     * @param outcome an Outcome object representing the outcome of a game or a series
     */
    void receiveOutcome(Outcome outcome);

    /**
     * get the strategy of the player
     *
     * @return a Strategy object;
     */
    Strategy getStrategy();

    /**
     * get the Outcomes of games this player has played;
     *
     * @return a list of Outcome
     */
    List<Outcome> getResults();

    /**
     * receive the name assigned in a tournament
     *
     * @param nameInTournament a String;
     */
    void receiveNameInTournament(String nameInTournament);

    /**
     * @return the name assigned in a tournament;
     */
    String getNameInTournament();

    /**
     * reset phase to 0(placement phase) for every new game
     */
    void setPhaseA();

    /**
     * set the phase to 1(turn actions phase) for turn actions
     */
    void setPhaseB();

    /**
     * assign an opponent for a best of n series;
     *
     * @param playAgainst the opponent name
     */
    void assignOpponent(String playAgainst);

    /**
     * receive encounters results in a tournament;
     */
    void receiveTournamentEncounter(String results);

}
