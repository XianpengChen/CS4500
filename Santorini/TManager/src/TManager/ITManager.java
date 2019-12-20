package TManager;

/**
 * this is the interface for a tournament manager, which deals with a fixed but arbitrary number of players and runs a
 * round-robin tournament of all players. Clearly, the essence of the manager is basically a function from the players
 * to the result of the tournament.
 */
public interface ITManager {

    /**
     * this method will use the list of players in the fields to run a  round-robin schedule between them.
     */
    void roundRobin();

    /**
     * print out the result of this tournament to a string; JSON array of cheaters' name and JSON array of games;
     * @return a string
     */
    String printOutResult();

    /**
     * print out the results of encounter in this tournament to a string;
     * add "encounters results" tag to disambiguate the messages.
     * @return a string
     */
    void printOutEncounters();
}

