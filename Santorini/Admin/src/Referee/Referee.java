package Referee;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import board.Board;
import board.IBoard;
import board.IReadonlyBoard;
import data.Action;
import data.Outcome;
import data.PlaceWorkerAction;
import observer.IObserver;
import player.IPlayer;
import rules.IRulesEngine;

// A concrete implementation of a Santorini Referee, 2 players, 2 workers per player;
public class Referee implements IReferee {
  // the list of IPlayers to know who is in the game
  private List<IPlayer> players;
  private final static int numberOfPlayers = 2;
  private final static int workersPerPlayer = 2;
  //the milliseconds the referee will wait when it is asking for a placement action from a player;
  private final static int waitForPlacement = 3000;
  //the milliseconds the referee will wait when it is asking for a turn action from a player;
  private final static int waitForTurn = 15000;
  private final static String wrongNumber = "player number not equal to 2!";
  private final static String breakerOrInfinite = " is a breaker or into infinite loop";
  private final static String noFeedback = "no feed back from ";
  private final static String illegalAction = " lose, the action is illegal according to rule";
  private final static String wonByBoxing = " won by boxing all opponent's workers";
  private final static String wonByRule = " won by a worker reaching 3rd floor";
  // the set of rules that this Referee should enforce on the board and IPlayer Actions
  private final IRulesEngine rulesEngine;
  // the index of a player in "players" above that indicates whose turn it is
  private  int turn;
  // the IBoard that represents the single source of truth and state of the game
  private  IBoard board;
  // indicate the game or game series running(true); ended (false)
  private  boolean gameRunning;
  //the outcome of game(s);
  private  List<Outcome> outcome;
  private List<IObserver> observers;

  // this constructor takes in information about the IPlayers in our game and our rule
  // set from the Administrator, and allows the first player in the List to begin placing workers.
  // The mechanism for determining first turn will be iterated on later.
  public Referee(List<IPlayer> players, IRulesEngine rulesEngine) {
    if (players.size() == numberOfPlayers) {
      this.players = whoIsFirst(players);
    }
    else {
      System.err.println(wrongNumber);
    }
    this.rulesEngine = rulesEngine;
    this.turn = 0;
    this.board = new Board();
    this.gameRunning = true;
    this.outcome = new ArrayList<>();
    this.observers = new ArrayList<>();
  }

  public Referee(List<IPlayer> players, IRulesEngine rulesEngine, List<IObserver> observers) {
    if (players.size() == numberOfPlayers) {
      this.players = whoIsFirst(players);
    }
    else {
      System.err.println(wrongNumber);
    }
    this.rulesEngine = rulesEngine;
    this.turn = 0;
    this.board = new Board();
    this.gameRunning = true;
    this.outcome = new ArrayList<>();
    this.observers = observers;
  }

  @Override
  public void setUpGame() {
    board = new Board();
    turn = 0;
    int rounds = workersPerPlayer * numberOfPlayers;
    while (rounds > 0 && gameRunning) {
      synchronized (this) {
        for (int i = 0; i < players.size(); i++) {
          turn = i;
          IPlayer current = players.get(turn);
          IPlayer next = players.get((turn + 1) % numberOfPlayers);
          PlaceWorkerAction action;
          //set a timer to wait for player's action
          Timer timer = new Timer(true);
          try {
            InterruptTimerTask interruptTimerTask =
                    new InterruptTimerTask(Thread.currentThread());
            timer.schedule(interruptTimerTask, waitForPlacement);
            action = current.getPlaceWorker(board.toViewModel());
            if (Thread.currentThread().isInterrupted()) {
              throw new InterruptedException();
            }
          } catch (Exception e) {
            handleSetupException(current, next);
            return;
          } finally {
            timer.cancel();
          }
          executePlacementHelper(current, next, action, i);
          rounds -= 1;
        }
      }
    }
  }

  private void handleSetupException(IPlayer current, IPlayer next) {
    outcome.add(new Outcome(next.getNameInTournament(), current.getNameInTournament(), 1,
            1, current.getNameInTournament() + breakerOrInfinite));
    gameRunning = false;
    for (IObserver ob: observers) {
      ob.updateCurrentPlayer(current.getNameInTournament());
      ob.updateViewBoard(board.toViewModel());
      ob.updateFinalMessage(noFeedback + current.getNameInTournament());
    }
  }


  private void executePlacementHelper(IPlayer current, IPlayer next, PlaceWorkerAction action, int i) {
    if (rulesEngine.isPlaceWorkerLegal(board.toViewModel(), action)) {
      board.createWorker(action.getWorkerId(), action.getRow(), action.getColumn());
      turn = (i+1) % numberOfPlayers;
      for (IObserver ob: observers) {
        ob.updateViewBoard(board.toViewModel());
      }
    }
    else {
      outcome.add(new Outcome(next.getNameInTournament(),current.getNameInTournament(), 1,
              1, current.getNameInTournament() + breakerOrInfinite));
      gameRunning = false;
      for (IObserver ob: observers) {
        ob.updateCurrentPlayer(current.getNameInTournament());
        ob.updateViewBoard(board.toViewModel());
        ob.updateFinalMessage(current.getNameInTournament() + illegalAction);
      }
    }
  }

  @Override
  public void executeTurn() {
    if (gameRunning) {
      IPlayer currentPlayer = players.get(turn);
      IPlayer nextPlayer = players.get((turn + 1) % numberOfPlayers);
      boolean playerLose = rulesEngine.didPlayerLose(board.toViewModel(), currentPlayer.getNameInTournament());
      if (playerLose) {
        outcome.add(new Outcome(nextPlayer.getNameInTournament(), currentPlayer.getNameInTournament(), 1,
                1, nextPlayer.getNameInTournament() + wonByBoxing));
        gameRunning = false;
        for (IObserver ob: observers) {
          ob.updateCurrentPlayer(currentPlayer.getNameInTournament());
          ob.updateViewBoard(board.toViewModel());
          ob.updateFinalMessage(nextPlayer.getNameInTournament() + wonByBoxing);
        }
        return;
      }
      List<Action> currentTurn;
      //set a timer to wait for player's turn action
      Timer timer = new Timer(true);
      try {
        InterruptTimerTask interruptTimerTask =
                new InterruptTimerTask(Thread.currentThread());
        timer.schedule(interruptTimerTask, waitForTurn);
        currentTurn = currentPlayer.getTurn(board.toViewModel());
        if (Thread.currentThread().isInterrupted()) {
          throw new InterruptedException();
        }
      } catch (Exception e) {
        handleTurnException(currentPlayer, nextPlayer);
        return;
      } finally {
        timer.cancel();
      }
      executeTurnHelper(currentPlayer, nextPlayer, currentTurn);
    }
  }

  private void handleTurnException(IPlayer currentPlayer, IPlayer nextPlayer) {
    outcome.add(new Outcome(nextPlayer.getNameInTournament(), currentPlayer.getNameInTournament(), 1,
            1, currentPlayer.getNameInTournament() + breakerOrInfinite));
    gameRunning = false;
    for (IObserver ob: observers) {
      ob.updateCurrentPlayer(currentPlayer.getNameInTournament());
      ob.updateViewBoard(board.toViewModel());
      ob.updateFinalMessage(noFeedback + currentPlayer.getNameInTournament());
    }
  }

  /**
   * to execute a turn for a player.
   * @param currentPlayer current player;
   * @param currentTurn current turn action;
   */
  private void executeTurnHelper(IPlayer currentPlayer, IPlayer nextPlayer, List<Action> currentTurn) {
    if (rulesEngine.isTurnLegal(board.toViewModel(), currentTurn, currentPlayer.getNameInTournament())) {
      board.move(currentTurn.get(0).getWorkerId(), currentTurn.get(0).getDirection());
      boolean playerWon = rulesEngine.didPlayerWin(board.toViewModel(), currentPlayer.getNameInTournament());
      if (playerWon) {
        outcome.add(new Outcome(currentPlayer.getNameInTournament(), nextPlayer.getNameInTournament(), 1,
                1, currentPlayer.getNameInTournament() + wonByRule));
        gameRunning = false;
        for (IObserver ob: observers) {
          ob.updateCurrentAction(currentTurn.subList(0, 1));
          ob.updateViewBoard(board.toViewModel());
          ob.updateFinalMessage(currentPlayer.getNameInTournament() + wonByRule);
        }
        return;
      }
      board.build(currentTurn.get(1).getWorkerId(), currentTurn.get(1).getDirection());
      for (IObserver ob: observers) {
        ob.updateCurrentAction(currentTurn);
        ob.updateViewBoard(board.toViewModel());
      }
      turn = (turn + 1) % numberOfPlayers;
    } else {
      gameRunning = false;
      for (IObserver ob: observers) {
        ob.updateCurrentAction(currentTurn.subList(0, 1));
        ob.updateViewBoard(board.toViewModel());
        ob.updateFinalMessage(currentPlayer.getNameInTournament() + illegalAction);
      }
    }
  }

  @Override
  public Outcome runAGame() {
    setPhaseA();
    setUpGame();
    setPhaseB();
    while (gameRunning) {
      executeTurn();
    }
    for (IPlayer player : players) {
      player.receiveOutcome(outcome.get(outcome.size()-1));
    }
    return outcome.get(outcome.size()-1);
  }

  private void setPhaseA() {
    for(IPlayer player: players) {
      player.setPhaseA();
    }
  }
  private void setPhaseB() {
    for(IPlayer player: players) {
      player.setPhaseB();
    }
  }

  @Override
  public List<Outcome> playNGames(int games) {
    while (games > 0) {
      gameRunning = true;
      runAGame();
      gameRunning = true;
      games-=1;
    }
    return outcome;
  }

  @Override
  public List<IPlayer> whoIsFirst(List<IPlayer> players) {
    //sort the list according to the string of playerName;
    return players.stream().sorted(Comparator.comparing(IPlayer::getNameInTournament)).collect(Collectors.toList());
  }

  @Override
  public IReadonlyBoard getGameState() {
    return board.toViewModel();
  }


  @Override
  public Outcome mergeOutcomes(List<Outcome> outcomes) {
    String player1 = players.get(0).getNameInTournament();
    int player1Won = 0;
    String player2 = players.get(1).getNameInTournament();
    int player2Won = 0;
    for (Outcome out : outcomes) {
      if (out.getWinner().equals(player1)) {
        player1Won++;
      }
      else {
        player2Won++;
      }
    }
    if (player1Won >= player2Won) {
      return new Outcome(player1, player2, outcomes.size(), player1Won, " won " + player1Won +
              " games out of a best of " + outcomes.size());
    }
    else {
      return new Outcome(player2, player1, outcomes.size(), player2Won, " won " + player2Won +
              " games out of a best of " + outcomes.size());
    }

  }

  protected class InterruptTimerTask extends TimerTask {

    private Thread theTread;

    InterruptTimerTask(Thread theTread) {
      this.theTread = theTread;
    }
    @Override
    public void run() {
      theTread.interrupt();
    }
  }
}
