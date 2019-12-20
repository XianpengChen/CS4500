package Referee;



import java.util.List;

import board.IReadonlyBoard;
import data.Outcome;
import player.IPlayer;

/**
 * An Referee.Referee interface represents the knowledge of how to oversee IPlayers in a
 * game of Santorini.
 *
 * Some methods and documentation adopted from provider's assignment 8 design file.
 */
public interface IReferee {
  // Set up the game according to information gathered from the IRulesEngine
  // for Standard Santorini, a starting common.board is a common.board of all 0's with two workers per player
  void setUpGame();

  // Execute a turn in the game. The standard way to use this
  // method would be to call it via a while loop.
  // the game is over. Once this happens, the Referee.Referee.IReferee will be know who won and who
  // lost and update the outcome.
  void executeTurn();


  /**
   * run a game.
   * @return the outcome of a game;
   */
  Outcome runAGame();

  // Plays N number (where N is a positive integer) of Santorini games. return an outcome stating the winners of the series.
  // To play a single game, call this method with N = 1.
  List<Outcome> playNGames(int games);

  //sort the input list in an order such that who goes first, who are in front;
  List<IPlayer> whoIsFirst(List<IPlayer> players);

  /**
   * get the IReadonlyBoard representing the current game state;
   * @return an IReadonlyBoard
   */
  IReadonlyBoard getGameState();

  /**
   * merge the list<Outcome> to an over outcome stating who is the total winner of series;
   * @param outcomes list of outcomes.
   * @return an overall outcome.
   */
  Outcome mergeOutcomes(List<Outcome> outcomes);

}