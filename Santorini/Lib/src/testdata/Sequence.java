package testdata;


import board.Board;
import board.IBoard;
import board.ICell;
import data.Action;

import java.util.List;

public class Sequence {
    private final IBoard board;
    private final List<Action> actions;

    public Sequence(ICell[][] cells, List<Action> actions) {
        this.board = new Board(cells);
        this.actions = actions;
    }

    public IBoard getBoard() {
        return board;
    }

    public List<Action> getActions() {
        return actions;
    }
}
