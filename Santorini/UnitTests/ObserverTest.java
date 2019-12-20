import Referee.IReferee;
import Referee.Referee;
import observer.IObserver;
import observer.Observer;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import rules.StandardSantoriniRulesEngine;
import strategy.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class ObserverTest {
    private List<IPlayer> initializePlayersB() {
        String playerToTest = "bob";
        String opponent = "cindy";
        ITurnStrategy turnStrategy1 = new StayAliveStrategy(playerToTest, opponent);
        ITurnStrategy turnStrategy2 = new StayAliveStrategy(opponent, playerToTest);
        Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy1, 1);
        Strategy strategy2 = new Strategy(new CartesianDistancePlacementStrategy(playerToTest),
                turnStrategy2, 1);
        IPlayer player1 = new Player(strategy1, playerToTest);
        IPlayer player2 = new Player(strategy2, opponent);
        return Arrays.asList(player1, player2);
    }
    @Test
    public void updateViewBoard() {
        List<IPlayer> players = initializePlayersB();
        List<IObserver> observers = Arrays.asList(new Observer());
        IReferee referee1 = new Referee(players, new StandardSantoriniRulesEngine(), observers);
        referee1.runAGame();
        IObserver ob = observers.get(0);
        assertEquals(ob.printViewBoard(), "[[0,1,2,\"0cindy2\",4,0],[0,0,2,4,4,4],[0,0,0,1,2,\"3bob1\"],[0,0,0,0," +
                "\"1bob2\",\"2cindy1\"],[0,0,0,0,1,4],[0,0,0,0,0,1]]");
        assertNull(ob.getCurrentPlayer());
        assertEquals(ob.getCurrentAction(), "[bob1, [\"EAST\", \"PUT\"]]");
        assertEquals(ob.getFinalMessage(), "\"bob won by a worker reaching 3rd floor\"");
    }

}
