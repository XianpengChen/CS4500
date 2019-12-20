package strategy;


import board.IReadonlyBoard;
import data.PlaceWorkerAction;

public interface IPlacementStrategy {
    /**
     * Get a "place worker" action that will place a worker on the common.board
     */
    PlaceWorkerAction getPlaceWorker(String workerId, IReadonlyBoard b);
}
