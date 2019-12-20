package board;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Direction;
import data.Worker;

public class ViewModelBoard implements IReadonlyBoard {

    private final ICell[][] cells;

    public ViewModelBoard(ICell[][] cells) {
        this.cells = cells;
    }


    // return whether a common.board.BuildingWorker exists at the given common.data.Direction from the
    // given worker
    @Override
    public boolean isOccupied(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int newRow = currentWorker.getRow() + direction.getRowModifier();
        int newColumn = currentWorker.getColumn() + direction.getColumnModifier();
        ICell targetEntity = this.cells[newRow][newColumn];
        return targetEntity.isWorker();
    }

    // overloaded version of isOccupied based on absolute coordinates instead of relative directions
    @Override
    public boolean isOccupied(int row, int column) {
        return this.cells[row][column].isWorker();
    }

    // returns the height of the building in the given direction relative to
    // given worker
    @Override
    public int height(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int newRow = currentWorker.getRow() + direction.getRowModifier();
        int newColumn = currentWorker.getColumn() + direction.getColumnModifier();
        ICell targetCell = this.cells[newRow][newColumn];
        return targetCell.getHeight();
    }

    // overloaded version of height based on absolute coordinates
    @Override
    public int height(int row, int column) {
        return this.cells[row][column].getHeight();
    }

    // return whether there is a Cell in the given direction from the worker
    @Override
    public boolean isNeighbor(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int newRow = currentWorker.getRow() + direction.getRowModifier();
        int newColumn = currentWorker.getColumn() + direction.getColumnModifier();
        boolean rowBounded = newRow >= 0 && newRow <= 5;
        boolean columnBounded = newColumn >= 0 && newColumn <= 5;
        return rowBounded && columnBounded;
    }

    // return whether the specified coordinates exist in the grid
    @Override
    public boolean cellExists(int row, int column) {
        return row >= 0 && row < this.cells.length && column >= 0 && column < this.cells[row].length;
    }

    // return whether a worker exists
    @Override
    public boolean hasWorker(String workerId) {
        return this.findWorker(workerId) != null;
    }

    // find the designated worker in the common.board, assumes that it exists
    @Override
    public Worker findWorker(String workerId) {
        int len = workerId.length();
        int workerNumber = Integer.parseInt(workerId.substring(len - 1));
        String playerName = workerId.substring(0, len - 1);
        for (int row = 0; row < this.cells.length; row++) {
            for (int column = 0; column < this.cells[row].length; column++) {
                ICell currentCell = this.cells[row][column];
                if (currentCell.isWorker()) {
                    if (currentCell.getPlayerName().equals(playerName) && currentCell.getWorkerNumber() == workerNumber) {
                        return new Worker(currentCell.getPlayerName(), currentCell.getWorkerNumber(), row, column);
                    }
                }
            }
        }
        return null;
    }

    // return a list of Workers belonging to the IPlayer with the given name
    @Override
    public List<Worker> getPlayerWorkers(String playerName) {
        if (getPlayerWorkerMap().containsKey(playerName)) {
            return getPlayerWorkerMap().get(playerName);
        }
        else {
            return new ArrayList<>();
        }
    }

    /**
     * Convenience method to obtain all the workers and their players within the board
     * @return a Map of Player Name -> List<Worker>
     */
    @Override
    public Map<String, List<Worker>> getPlayerWorkerMap() {
        Map<String, List<Worker>> resultMap = new HashMap<>();

        for (int row = 0; row < this.cells.length; row++) {
            for (int column = 0; column < this.cells[row].length; column++) {
                ICell currentCell = this.cells[row][column];
                if (currentCell.isWorker()) {
                    String playerName = currentCell.getPlayerName();
                    Worker worker = new Worker(playerName, currentCell.getWorkerNumber(), row, column);

                    if (!resultMap.containsKey(playerName)) {
                        resultMap.put(playerName, new ArrayList<>());
                    }

                    resultMap.get(playerName).add(worker);
                }
            }
        }

        return resultMap;
    }

    @Override
    public int getMaxRows() {
        return cells.length;
    }

    @Override
    public int getMaxColumns() {
        int maxColumn = 0;

        for (ICell[] column : cells) {
            if (maxColumn < column.length) {
                maxColumn = column.length;
            }
        }

        return maxColumn;
    }

    /**
     * Generates a copy of this viewmodel such that it can be acted on in the context of a
     * function that can generate succesor states.
     * @return an instance of IBoard that is mutable.
     */
    @Override
    public IBoard toBoard() {
        int rows = this.cells.length;
        ICell[][] copy = new ICell[rows][];
        for (int i = 0; i < rows; i++) {
            int columns = this.cells[i].length;
            copy[i] = new ICell[columns];

            for (int j = 0; j < columns; j++) {
                copy[i][j] = this.cells[i][j].copy();
            }
        }

        return new Board(copy);
    }

    @Override
    public String printBoard() {
        StringBuilder board = new StringBuilder("[");
        for (int i = 0; i < getMaxRows(); i++) {
            StringBuilder row = new StringBuilder("[");
            for (int j = 0; j < getMaxColumns(); j++) {
                if (j == getMaxColumns() - 1) {
                    row.append(cells[i][j].printCell());
                }
                else {
                    row.append(cells[i][j].printCell()).append(",");
                }
            }
            row.append("]");
            if (i == getMaxRows() - 1) {
                board.append(row).append("]");
            }
            else {
                board.append(row).append(",");
            }
        }
        return board.toString();
    }

    @Override
    public String getOpponentName(String playerName) {
        String name = "";
        for (int i = 0; i < getMaxRows(); i++) {
            for (int j = 0; j < getMaxColumns(); j++) {
                ICell currentCell = cells[i][j];
                if (currentCell instanceof BuildingWorker) {
                    if (!currentCell.getPlayerName().equals(playerName)) {
                        name = currentCell.getPlayerName();
                        return name;
                    }
                }
            }
        }
        return name;
    }
}
