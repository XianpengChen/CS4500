package player;

import java.util.ArrayList;
import java.util.List;

import board.IReadonlyBoard;
import data.Action;
import data.Outcome;
import data.PlaceWorkerAction;
import data.Worker;
import strategy.Strategy;

import javax.print.attribute.standard.PrinterLocation;

/**
 * as the name suggesting, this class is to create a player who takes infinity to get a place worker action;we tested
 * this in RefereeTest.
 */
public class InfinitePlacePlayer extends Player {
    private static final int infinity = 6000;
    // Constructs a new Player
    public InfinitePlacePlayer(Strategy strategy, String playerName) {
       super(strategy, playerName);
    }
    @Override
    public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) throws InterruptedException {
        List<Worker> workersOnBoard = b.getPlayerWorkers(getPlayerName());
        int nextWorkerNumber = workersOnBoard.size() + 1;
        String workerToPlace = String.format(WORKER_ID, this.playerName, nextWorkerNumber);
        Thread.sleep(infinity);
        return strategy.getPlaceWorker(workerToPlace, b);
    }
}
