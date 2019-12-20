/**
 * this is the interface for a tournament manager, which deals with a fixed but arbitrary number of players and runs a
 * round-robin tournament of all players. Clearly, the essence of the manager is basically a function from the players
 * to the result of the tournament.
 *
 * Fields: a TManager.TManager class should have a list of players to represent the arbitrary number of players participating in
 * this tournament. should have a scores hashMap from player name (String) to score(int). score means how many games the
 * player won in the tournament.
 *
 * For broken player: in a game, if there is no feedback(such as placement action and turn action) from a player in a
 * certain amount of time, or it throws exception when referee asks for action from the player, the referee will decide
 * that the player lose the game and the opponent player wins the game.
 *
 * how to fairly schedule all the (n-1)n/2 games if there are n players?
 *
 */
public interface ITManager {

    /**
     * this method will use the list of players in the fields to run a single round-robin schedule between them. the
     * result will be updated to the scores hashMap.
     */
    void roundRobin();

    /**
     * this will send out a summary to every player participating in this tournament. it may contain how many games won
     * by this player, the rank of this player in the tournament in the tournament, and overall tournament rank list.
     */
    void resultToPlayers();
}
