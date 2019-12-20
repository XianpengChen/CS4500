package observer;

import java.util.List;

import board.IReadonlyBoard;
import data.Action;

/**
 * an observer designed to be plugged into the referee component;
 */
public interface IObserver {

     /**update the view board of the observer;
      */
     void updateViewBoard(IReadonlyBoard view);

     /**update the current player taking action on the view board;
      * @param player player name;
      */
     void updateCurrentPlayer(String player);

     /**update the current action the current player is taking;
      * @param action a list of action(s). move or move and build;
      */
     void updateCurrentAction(List<Action> action);

     /**a final or error message from the referee when the game is end;
      * @param message a string;
      */
     void updateFinalMessage(String message);

     /**print the current view board to a string representation;
      * @return a string representation;
      */
     String printViewBoard();

     /**
      * @return a string, the current player;
      */
     String getCurrentPlayer();

     /**
      * @return a string, the current action;
      */
     String getCurrentAction();

     /**
      * @return a string, the final or error message;
      */
     String getFinalMessage();



     /**
      * to visualize the view board; the visualization should be updated every time the view board is updated;
      */
     void visualization();
}
