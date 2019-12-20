import common.board.IReadonlyBoard;

/**
 * an observer designed to be plugged into the referee component;
 */
public interface IObserver {

     /**
      * Initially connect an observer to a game of santorini
      * @param board the immutable board given by the referee
      */
     void initialize(IReadnlyBoard board);

     /**update the view board of the observer;
      */
     void updateViewBoard();

     /**print the current view board to a string representation;
      * @return a string representation;
      */
     String printViewBoard();

     /**
      * to visualize the view board; the visualization should be updated every time the view board is updated;
      */
     void visualization();
}
