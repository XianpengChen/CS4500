package player;

import java.util.ArrayList;
import java.util.List;

import board.IReadonlyBoard;
import data.Action;
import data.Outcome;
import data.PlaceWorkerAction;
import data.Worker;
import strategy.Strategy;


/**
 * as the name suggesting, this class is to build a player who takes infinity to get a turn action; we tested this in
 * RefereeTest.
 */
public class InfiniteTurnPlayer extends Player {

    private static final int Infinity = 16000;

    public InfiniteTurnPlayer(Strategy strategy, String playerName) {
        super(strategy, playerName);
    }
    @Override
    public List<Action> getTurn(IReadonlyBoard b) throws InterruptedException {
        Thread.sleep(Infinity);
        return strategy.getTurn(this.playerName, b);
    }
}
