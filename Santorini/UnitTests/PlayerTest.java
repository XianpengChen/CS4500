import board.*;
import data.Action;
import data.PlaceWorkerAction;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import strategy.DiagonalPlacementStrategy;
import strategy.ITurnStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlayerTest {


  private ICell[][] initializeA() {
    ICell[][] results = new ICell[6][6];

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        results[i][j] = new Height(0);
      }
    }
    results[0][0] = new BuildingWorker("one", 1, 0);
    results[0][3] = new BuildingWorker("two", 1, 2);

    return results;
  }
  private ICell[][] initializeB() {
    ICell[][] results = new ICell[6][6];

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        results[i][j] = new Height(0);
      }
    }
    results[0][0] = new BuildingWorker("one", 1, 0);
    results[0][3] = new BuildingWorker("two", 1, 2);
    results[1][0] = new BuildingWorker("one", 2, 2);
    results[0][2] = new BuildingWorker("two", 2, 2);


    return results;
  }

  @Test
  public void getPlaceWorker() {
    String playerToTest = "one";
    String opponent = "two";


    IBoard board = new Board(initializeA());
    IReadonlyBoard board1 = board.toViewModel();
    ITurnStrategy turnStrategy = new StayAliveStrategy(playerToTest, opponent);
    Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy, 3);
    IPlayer player = new Player(strategy1, playerToTest);
    PlaceWorkerAction action = null;
    try {
      action = player.getPlaceWorker(board1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(action.getColumn(), 1);
    assertEquals(action.getRow(), 1);
  }

  @Test
  public void getTurn() {
    String playerToTest = "one";
    String opponent = "two";
    IBoard board = new Board(initializeB());
    IReadonlyBoard board1 = board.toViewModel();
    ITurnStrategy turnStrategy = new StayAliveStrategy(playerToTest, opponent);
    Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy, 2);
    IPlayer player = new Player(strategy1, playerToTest);
    List<Action> actions = null;
    try {
      actions = player.getTurn(board1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(actions.size(), 2);
    assertEquals(actions.get(0).getWorkerId(), "one1");
  }
}