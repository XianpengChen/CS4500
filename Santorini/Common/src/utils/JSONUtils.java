package utils;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

import board.BuildingWorker;
import board.Height;
import board.ICell;
import board.IReadonlyBoard;
import board.ViewModelBoard;
import data.Action;
import data.ActionType;
import data.Direction;
import data.PlaceWorkerAction;
import data.Worker;

/**
 * this class has some methods to convert certain objects to String(in JSON format) or convert String(in JSON format) to
 * certain objects;
 */
public class JSONUtils {

    /**
     * to print an IReadonlyBoard to a String in JSON format;
     *
     * @param board to represent a game state
     * @return a String in JSON format
     */
    public String BoardToString(IReadonlyBoard board) {
        return board.printBoard();
    }

    /**
     * print a list of Action to a String in JSON format;
     *
     * @param actions list of Action
     * @return a String in JSON format
     */
    public String ActionsToString(List<Action> actions) {
        StringBuilder temp = new StringBuilder();
        for (Action action : actions) {
            temp.append(action.printAction());
        }
        return temp.toString();
    }


    /**
     * print a list of action(1 or 2 actions) to a string in JSON format for Server-client communication;
     *
     * @param actions list of Action
     * @return a string
     */
    public String ActionsToStringB(List<Action> actions) {
        String workerId = "\"" + actions.get(0).getWorkerId() + "\"";
        String moveDirection = actions.get(0).getDirection().printDirectionB();
        if (actions.size() == 2) {
            String buildDirection = actions.get(1).getDirection().printDirectionB();
            return "[" + workerId + moveDirection + buildDirection + "]";
        } else {
            return "[" + workerId + moveDirection + "]";
        }
    }

    /**
     * print a placeworkeraction to a String in JSON format;
     *
     * @param action a PlaceWorkerAction
     * @return a string;
     */
    public String PlaceWorkerActionToString(PlaceWorkerAction action) {
        return "[" + "\"" + action.getWorkerId() + "\", " + action.getRow() + ", " + action.getColumn() + "]";
    }

    /**
     * print a placeworkeraction to a String in JSON format;
     *
     * @param action a place worker action;
     * @return a string
     */
    public String PlaceWorkerActionToStringB(PlaceWorkerAction action) {
        return "[" + action.getRow() + ", " + action.getColumn() + "]";
    }

    /**
     * convert the JSONBoard to an IReadonlyBoard;
     *
     * @param JSONBoard a complete JSON board
     * @return an IReadonlyBoard;
     */
    public IReadonlyBoard StringToBoard(String JSONBoard) {
        ICell[][] cells = new ICell[6][6];

        JSONParser parser = new JSONParser();
        JSONArray board = new JSONArray();
        try {
            board = (JSONArray) parser.parse(JSONBoard);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 6; i++) {
            if (i < board.size()) {
                JSONArray currentRow = (JSONArray) board.get(i);
                for (int j = 0; j < 6; j++) {
                    if (j < currentRow.size()) {
                        String currentCell = currentRow.get(j).toString();
                        if (currentCell.length() == 1) {
                            int height = Integer.parseInt(currentRow.get(j).toString());
                            cells[i][j] = new Height(height);
                        } else {
                            int height = Integer.parseInt(currentCell.substring(0, 1));
                            String playerName = currentCell.substring(1, currentCell.length() - 1);
                            int workerNumber = Integer.parseInt(currentCell.substring(currentCell.length() - 1));
                            cells[i][j] = new BuildingWorker(playerName, workerNumber, height);
                        }
                    }
                    else {
                        cells[i][j] = new Height(0);
                    }
                }
            } else {
                for (int j = 0; j < 6; j++) {
                    cells[i][j] = new Height(0);
                }
            }
        }
        return new ViewModelBoard(cells);
    }

    /**
     * convert JSONAction t0 a list of Action;
     *
     * @param JSONActions String in JSON format;
     * @return a list of Action
     */
    public List<Action> StringToActions(String JSONActions) {
        List<Action> actions = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray list = new JSONArray();
        String converter = "[" + JSONActions + "]";
        try {
            list = (JSONArray) parser.parse(converter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            JSONArray currentAction = (JSONArray) list.get(i);

            JSONArray currentDirection = (JSONArray) currentAction.get(2);
            String eastWest = (String) currentDirection.get(0);
            String northSouth = (String) currentDirection.get(1);
            String type = (String) currentAction.get(0);
            String workerId = (String) currentAction.get(1);
            Action curr = new Action(ActionType.from(type), workerId, new Direction(eastWest, northSouth));
            actions.add(curr);
        }
        return actions;
    }

    /**
     * inpterpret a string to a list of action(1 or 2) for server-client communication;
     *
     * @param JSONActions string
     * @return list of Action
     */
    public List<Action> StringToActionsB(String JSONActions) {
        List<Action> actions = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray list;
        try {
            list = (JSONArray) parser.parse(JSONActions);
            String workerId = (String) list.get(0);
            String moveEastWest = (String) list.get(1);
            String moveNorthSouth = (String) list.get(2);
            Action move = new Action(ActionType.MOVE, workerId,
                    new Direction(moveEastWest, moveNorthSouth));
            actions.add(move);
            if (list.size() == 5) {
                String buildEastWest = (String) list.get(3);
                String buildNorthSouth = (String) list.get(4);
                Action build = new Action(ActionType.BUILD, workerId,
                        new Direction(buildEastWest, buildNorthSouth));
                actions.add(build);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actions;

    }

    /**
     * convert a String in JSON format to a PlaceWorkerAction
     *
     * @param PWA PlaceWorkerAction String in JSON format;
     * @return a PlaceWorkerAction
     */
    public PlaceWorkerAction StringToPlaceWorkerAction(String PWA) {
        JSONParser parser = new JSONParser();
        JSONArray action = new JSONArray();
        try {
            action = (JSONArray) parser.parse(PWA);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String workerId = (String) action.get(0);
        int row = Integer.parseInt(action.get(1).toString());
        int column = Integer.parseInt(action.get(2).toString());
        return new PlaceWorkerAction(workerId, row, column);
    }

    /**
     * transfer a list of workers to a string representation;
     *
     * @param workers a list of workers;
     * @return a string representation;
     */
    public String WorkersToString(List<Worker> workers) {
        StringBuilder curr = new StringBuilder("[");
        for (int i = 0; i < workers.size(); i++) {
            if (i == workers.size() - 1) {
                curr.append(WorkerToString(workers.get(i)));
            } else {
                curr.append(WorkerToString(workers.get(i))).append(",");
            }
        }
        curr.append("]");
        return curr.toString();
    }

    /**
     * @param worker a Worker
     * @return a string representation of this worker;
     */
    private StringBuilder WorkerToString(Worker worker) {
        StringBuilder curr = new StringBuilder();
        curr.append("[\"").append(worker.getWorkerId()).append("\", ").append(worker.getColumn()).append(", ")
                .append(worker.getRow()).append("]");
        return curr;
    }
}
