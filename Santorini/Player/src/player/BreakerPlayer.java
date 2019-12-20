package player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import board.IReadonlyBoard;
import data.Action;
import data.ActionType;
import data.Direction;
import data.Outcome;
import data.PlaceWorkerAction;
import data.Worker;
import strategy.Strategy;


/**
 * as the name suggesting, this class is to build a breaker player who gets actions in time, but those actions are illegal;
 * we tested this in RefereeTest;
 */
public class BreakerPlayer extends Player {


    // Constructs a new Player
    public BreakerPlayer(Strategy strategy, String playerName) {
        super(strategy, playerName);
    }
    @Override
    public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
        List<Worker> workersOnBoard = b.getPlayerWorkers(getPlayerName());
        int nextWorkerNumber = workersOnBoard.size() + 1;
        String workerToPlace = String.format(WORKER_ID, this.playerName, nextWorkerNumber);
        return new PlaceWorkerAction(workerToPlace, -1, -1);
    }

    @Override
    public List<Action> getTurn(IReadonlyBoard b) {
        Action move = new Action(ActionType.MOVE, playerName + "3", new Direction("east", "put"));
        Action build = new Action(ActionType.BUILD, playerName + "3", new Direction("east", "put"));
        return Arrays.asList(move, build);
    }
}
