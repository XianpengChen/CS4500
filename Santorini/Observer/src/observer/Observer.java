package observer;

import java.util.List;

import board.ICell;
import board.IReadonlyBoard;
import board.ViewModelBoard;
import data.Action;

/**
 * an observer designed to be plugged into referee so that the CEO can see what happened on the game board;
 */
public class Observer implements IObserver {

  //represent current player;
  private String currentPlayer;
  //represent the current turn action the current player took;
  private String currentAction;
  //represent the current game board after the current player took the current turn action;
  private IReadonlyBoard currentBoard;
  //a final or error message received when game is end;
  private String finalMessage;

  public Observer() {}

  @Override
  public void updateViewBoard(IReadonlyBoard view) {
    currentBoard = view;
    System.out.println(printViewBoard());
  }

  @Override
  public void updateCurrentPlayer(String player) {
    currentPlayer = "\"" + player + "\"";
    System.out.println(currentPlayer);

  }

  @Override
  public void updateCurrentAction(List<Action> actions) {
    if (actions.size() == 1) {
      currentAction = "[" + actions.get(0).getWorkerId() + ", " +
              actions.get(0).getDirection().printDirection() +  "]";
    }
    else {
      currentAction = "[" + actions.get(0).getWorkerId() + ", " +
              actions.get(0).getDirection().printDirection() + ", "+
              actions.get(1).getDirection().printDirection() +  "]";

    }
    System.out.println();
    System.out.println(currentAction);

  }

  @Override
  public void updateFinalMessage(String message) {
    finalMessage = "\"" + message + "\"";
    System.out.println(finalMessage);
  }

  @Override
  public String printViewBoard() {
    return currentBoard.printBoard();
  }

  @Override
  public String getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public String getCurrentAction() {
    return currentAction;
  }

  @Override
  public String getFinalMessage() {
    return finalMessage;
  }

  @Override
  public void visualization() {

  }
}
