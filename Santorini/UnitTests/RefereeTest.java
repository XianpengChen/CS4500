import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import Referee.IReferee;
import Referee.Referee;
import board.IReadonlyBoard;
import data.Outcome;
import data.Worker;
import player.*;
import rules.StandardSantoriniRulesEngine;
import strategy.CartesianDistancePlacementStrategy;
import strategy.DiagonalPlacementStrategy;
import strategy.ITurnStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RefereeTest {

  private List<IPlayer> initializePlayers() {
    String playerToTest = "bob";
    String opponent = "tom";
    ITurnStrategy turnStrategy1 = new StayAliveStrategy(playerToTest, opponent);
    ITurnStrategy turnStrategy2 = new StayAliveStrategy(opponent, playerToTest);
    Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy1, 2);
    Strategy strategy2 = new Strategy(new CartesianDistancePlacementStrategy(playerToTest),
            turnStrategy2, 2);
    IPlayer player1 = new Player(strategy1, playerToTest);
    IPlayer player2 = new Player(strategy2, opponent);

    return Arrays.asList(player1, player2);
  }
  private List<IPlayer> initializePlayersB() {
    String playerToTest = "dog";
    String opponent = "cat";
    ITurnStrategy turnStrategy1 = new StayAliveStrategy(playerToTest, opponent);
    ITurnStrategy turnStrategy2 = new StayAliveStrategy(opponent, playerToTest);

    Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy1, 1);
    Strategy strategy2 = new Strategy(new CartesianDistancePlacementStrategy(playerToTest),
            turnStrategy2, 1);
    IPlayer player1 = new Player(strategy1, playerToTest);
    IPlayer player2 = new Player(strategy2, opponent);

    return Arrays.asList(player1, player2);
  }



  @org.junit.Test
  public void setUpGame() {
    List<IPlayer> players = initializePlayers();
    IReferee referee1 = new Referee(players, new StandardSantoriniRulesEngine());
    referee1.setUpGame();
    IReadonlyBoard gameState = referee1.getGameState();
    assertTrue(gameState.isOccupied(0, 0));
    assertTrue(gameState.isOccupied(5, 5));
    assertTrue(gameState.isOccupied(1,1));
    assertTrue(gameState.isOccupied(4, 5));

  }

  @org.junit.Test
  public void executeTurn() {
    List<IPlayer> players = initializePlayers();
    IReferee referee1 = new Referee(players, new StandardSantoriniRulesEngine());
    referee1.setUpGame();
    referee1.executeTurn();
    referee1.executeTurn();
    referee1.executeTurn();
    referee1.executeTurn();

    assertTrue(referee1.getGameState().isOccupied(0,2));
    assertTrue(referee1.getGameState().isOccupied(1,1));
    assertTrue(referee1.getGameState().isOccupied(2,5));
    assertTrue(referee1.getGameState().isOccupied(5,5));
  }

  @org.junit.Test
  public void runAGame() {
    List<IPlayer> players = initializePlayersB();
    IReferee referee1 = new Referee(players, new StandardSantoriniRulesEngine());
    Outcome output = referee1.runAGame();
    assertEquals(output.toString(), "Player dog won 1 of 1 games! because dog won by a worker reaching 3rd floor");
    assertEquals(referee1.getGameState().height(3, 5), 4);
  }

  @org.junit.Test
  public void playNGames() {
    List<IPlayer> players = initializePlayersB();
    IReferee referee1 = new Referee(players, new StandardSantoriniRulesEngine());
    List<Outcome> output = referee1.playNGames(3);
    List<Outcome> results1 = players.get(1).getResults();
    assertEquals(output.get(0).getLoser(), results1.get(0).getLoser());
    assertEquals(referee1.mergeOutcomes(output).toString(),
            "Player dog won 3 of 3 games! because  won 3 games out of a best of 3");
  }

  @org.junit.Test
  public void whoIsFirst() {
    List<IPlayer> players = initializePlayersB();
    IReferee referee1 = new Referee(players, new StandardSantoriniRulesEngine());
    referee1.setUpGame();
    Worker worker1 = referee1.getGameState().findWorker("cat1");

    assertEquals(worker1.getPlayerName(), "cat");


  }

  @Test
  public void getGameState() {
    List<IPlayer> players = initializePlayers();
    IReferee referee1 = new Referee(players, new StandardSantoriniRulesEngine());
    referee1.setUpGame();
    Worker worker1 = referee1.getGameState().findWorker("tom1");

    assertEquals(worker1.getPlayerName(), "tom");
  }

  @Test
  public void testInfinitePlacePlayer() {
    String playerToTest = "dog";
    String opponent = "cat";
    ITurnStrategy turnStrategy1 = new StayAliveStrategy(playerToTest);
    ITurnStrategy turnStrategy2 = new StayAliveStrategy(opponent);

    Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy1, 1);
    Strategy strategy2 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy2, 1);
    IPlayer player1 = new Player(strategy1, playerToTest);
    IPlayer player2 = new InfinitePlacePlayer(strategy2, opponent);
    List<IPlayer> players1 = Arrays.asList(player1, player2);
    IReferee referee1 = new Referee(players1, new StandardSantoriniRulesEngine());
    Outcome out = referee1.runAGame();
    assertTrue(out.getReason().contains("is a breaker or into infinite loop"));
    assertEquals(out.getLoser(), opponent);
  }

  @Test
  public void testInfiniteTurnPlayer() {
    String playerToTest = "tom";
    String opponent = "bob";
    ITurnStrategy turnStrategy1 = new StayAliveStrategy(playerToTest, opponent);
    ITurnStrategy turnStrategy2 = new StayAliveStrategy(opponent, playerToTest);

    Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy1, 1);
    Strategy strategy2 = new Strategy(new CartesianDistancePlacementStrategy(playerToTest),
            turnStrategy2, 1);
    IPlayer player1 = new Player(strategy1, playerToTest);
    IPlayer player2 = new InfiniteTurnPlayer(strategy2, opponent);
    List<IPlayer> players1 = Arrays.asList(player1, player2);
    IReferee referee1 = new Referee(players1, new StandardSantoriniRulesEngine());
    Outcome out = referee1.runAGame();
    assertTrue(out.getReason().contains("is a breaker or into infinite loop"));
    assertEquals(out.getLoser(), opponent);
  }

  @Test
  public void testBreakerPlayer() {
    String playerToTest = "sam";
    String opponent = "jason";
    ITurnStrategy turnStrategy1 = new StayAliveStrategy(playerToTest, opponent);
    ITurnStrategy turnStrategy2 = new StayAliveStrategy(opponent, playerToTest);

    Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy1, 1);
    Strategy strategy2 = new Strategy(new CartesianDistancePlacementStrategy(playerToTest),
            turnStrategy2, 1);
    IPlayer player1 = new Player(strategy1, playerToTest);
    IPlayer player2 = new BreakerPlayer(strategy2, opponent);
    List<IPlayer> players1 = Arrays.asList(player1, player2);
    IReferee referee1 = new Referee(players1, new StandardSantoriniRulesEngine());
    Outcome out = referee1.runAGame();
    assertTrue(out.getReason().contains("is a breaker or into infinite loop"));
    assertEquals(out.getLoser(), opponent);
  }

}