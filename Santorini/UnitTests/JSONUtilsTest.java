
import board.*;
import data.Action;
import data.ActionType;
import data.Direction;
import data.PlaceWorkerAction;
import org.junit.Test;
import utils.JSONUtils;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JSONUtilsTest {
    private JSONUtils utils = new JSONUtils();
    private ICell[][] formulateAdvancedBoard() {
        ICell[][] results = new ICell[6][6];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                results[i][j] = new Height(0);
            }
        }
        results[0][0] = new BuildingWorker("tom", 1, 0);
        results[0][1] = new BuildingWorker("tom", 2, 0);
        results[1][0] = new Height(3);
        results[2][0] = new BuildingWorker("two", 1, 0);
        results[2][1] = new BuildingWorker("two", 2, 0);
        return results;
    }


    @Test
    public void boardToString() {
        IReadonlyBoard board = new ViewModelBoard(formulateAdvancedBoard());
        String JSONBoard = utils.BoardToString(board);
        assertEquals("[[\"0tom1\",\"0tom2\",0,0,0,0],[3,0,0,0,0,0],[\"0two1\",\"0two2\",0,0,0,0],[0,0,0,0,0,0]" +
                ",[0,0,0,0,0,0],[0,0,0,0,0,0]]", JSONBoard);
    }

    @Test
    public void actionsToString() {
        Action move = new Action(ActionType.MOVE,"tom2", new Direction("EAST", "PUT"));
        Action build = new Action(ActionType.BUILD,"tom2", new Direction("EAST", "PUT"));
        List<Action> actions = Arrays.asList(move, build);
        assertEquals("[\"move\", \"tom2\", [\"EAST\", \"PUT\"]][\"build\", \"tom2\", [\"EAST\", \"PUT\"]]",
                utils.ActionsToString(actions));
    }

    @Test
    public void placeWorkerActionToString() {
        PlaceWorkerAction PWA = new PlaceWorkerAction("tom1", 3, 4);
        assertEquals("[\"tom1\", 3, 4]", utils.PlaceWorkerActionToString(PWA));
    }

    @Test
    public void stringToBoard() {
        String JSONBoard = "[[\"0tom1\",\"0tom2\",0,0,0,0],[3,0,0,0,0,0],[\"0two1\",\"0two2\",0,0,0,0],[0,0,0,0,0,0]" +
                ",[0,0,0,0,0,0],[0,0,0,0,0,0]]";
        String JSONBoard1 = "[[\"0one1\", \"0one2\"],\n" +
                "[3, 0],\n" +
                "[\"0two1\", \"0two2\"]]";
        String JSONBoard2 = "[[\"0one1\",\"0one2\",0,0,0,0],[3,0,0,0,0,0],[\"0two1\",\"0two2\",0,0,0,0],[0,0,0,0,0,0]," +
                "[0,0,0,0,0,0],[0,0,0,0,0,0]]";
        IReadonlyBoard board = utils.StringToBoard(JSONBoard);
        assertTrue(board.isOccupied(0,0));
        assertTrue(board.isOccupied(0,1));
        assertTrue(board.isOccupied(2,0));
        assertTrue(board.isOccupied(2,1));
        assertEquals(JSONBoard, board.printBoard());
        IReadonlyBoard board1 = utils.StringToBoard(JSONBoard1);
        assertEquals(JSONBoard2, board1.printBoard());
    }

    @Test
    public void stringToActions() {
        String JSONActions = "[\"move\", \"tom2\", [\"EAST\", \"PUT\"]][\"build\", \"tom2\", [\"EAST\", \"PUT\"]]";
        List<Action> actions = utils.StringToActions(JSONActions);
        assertEquals(actions.get(0).getType(), ActionType.MOVE);
        assertEquals(actions.get(1).getType(), ActionType.BUILD);

    }

    @Test
    public void stringToPlaceWorkerAction() {
        String PWA = "[\"tom1\", 3, 4]";
        PlaceWorkerAction action = utils.StringToPlaceWorkerAction(PWA);
        assertEquals(action.getColumn(), 4);
        assertEquals(action.getRow(), 3);
        assertEquals(action.getWorkerId(), "tom1");
    }
}
