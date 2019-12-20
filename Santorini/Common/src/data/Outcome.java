package data;

// A class to represent the outcome of a Santorini game, where winner is the name
// of the player that won the game or series, gamesToPlay is the number of games in the series,
// and gamesWon is the number of games won in the series.
public class Outcome {
  private final String winner;
  private final String loser;
  private int gamesToPlay;
  private int gamesWon;
  private final String reason;// "winner won by rule" or "loser is a breaker or into infinite loop"

  // A constructor for an outcome. The arguments are explained in detail at the top of
  // this file.
  public Outcome(String winner, String loser, int gamesToPlay, int gamesWon, String reason) {
    this.winner = winner;
    this.loser = loser;
    this.gamesToPlay = gamesToPlay;
    this.gamesWon = gamesWon;
    this.reason = reason;
  }

  // Convenience method to print the outcome of a game.
  @Override
  public String toString() {
    return String.format("Player %s won %d of %d games! because %s", this.winner, this.gamesWon, this.gamesToPlay, this.reason);
  }

  /**
   * get the winner string of this outcome;
   * @return a string
   */
  public String getWinner() {
    return this.winner;
  }

  /**
   * update games played.
   */
  public void updateGamesToPlay() {
    gamesToPlay+=1;
  }

  /**
   * update games won;
   */
  public void updateGamesWon() {
    gamesWon+=1;
  }

  /**
   * get games won;
   * @return an int.
   */
  public int getGamesWon() {
    return gamesWon;
  }

  /**get the reason why the game ended;
   * @return a string;
   */
  public String getReason() {
    return reason;
  }

  /**get the loser of this game;
   * @return a string
   */
  public String getLoser() {
    return loser;
  }
}
