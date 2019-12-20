package board;



import java.util.List;
import java.util.Map;

import data.Direction;
import data.Worker;

/**
 * Interface for a common.board that is read only
 * This is important as it becomes our ViewModel but takes
 * advantage of sharing important methods with it's write able
 * counterpart
 */
public interface IReadonlyBoard {

    boolean isOccupied(String worker, Direction direction);

    boolean isOccupied(int row, int column);

    int height(String worker, Direction direction);

    int height(int row, int column);

    boolean isNeighbor(String worker, Direction direction);

    boolean cellExists(int row, int column);

    boolean hasWorker(String workerId);

    Worker findWorker(String workerId);

    List<Worker> getPlayerWorkers(String playerName);

    Map<String, List<Worker>> getPlayerWorkerMap();

    int getMaxRows();

    int getMaxColumns();

    IBoard toBoard();

    String printBoard();

    /**
     * @param playername current player name;
     * @return the name the opponent of the current player;
     */
    String getOpponentName(String playername);
}
