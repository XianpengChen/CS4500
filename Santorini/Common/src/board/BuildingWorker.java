package board;

/**
 * Represents an common.board.ICell in a Santorini game common.board with a worker on it
 */
public class BuildingWorker implements ICell {
    // the height of the building at this common.board.ICell
    private int height;

    // the id of the worker at this common.board.ICell
    private String playerName;

    // the number worker this is, i.e. 0 for first worker that belongs to a player
    private int workerNumber;

    // get all fields during construction
    public BuildingWorker(String playerName, int workerNumber, int height) {
        this.playerName = playerName;
        this.height = height;
        this.workerNumber = workerNumber;
    }

    // get the height at this common.board.ICell
    public int getHeight() {
        return this.height;
    }

    // get the playerName at this common.board.ICell
    public String getPlayerName() {
        return this.playerName;
    }

    // get the number worker this is
    public int getWorkerNumber() {
        return this.workerNumber;
    }

    @Override
    public ICell copy() {
        int num = workerNumber;
        int he = height;
        return new BuildingWorker(playerName, num, he);
    }

    @Override
    public String printCell() {
        return "\"" + Integer.toString(height) + playerName + Integer.toString(workerNumber) + "\"";
    }

    // return whether there is a worker there
    public boolean isWorker() {
        return true;
    }
}
