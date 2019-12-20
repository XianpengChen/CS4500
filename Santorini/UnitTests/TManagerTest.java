import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import TManager.ITManager;
import TManager.TManager;
import observer.IObserver;
import player.IPlayer;
import player.InfinitePlacePlayer;
import player.Player;
import strategy.DiagonalPlacementStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

import static org.junit.Assert.assertEquals;

public class TManagerTest {
    private List<IPlayer> initializePlayers() {
        String player1Name = "dog";
        String player2Name = "Cat";
        String player3Name = "fish";
        String player4Name = "fish";
        IPlayer player1 = new Player(new Strategy(new DiagonalPlacementStrategy(), new StayAliveStrategy(player1Name),
                1), player1Name);
        IPlayer player2 = new InfinitePlacePlayer(new Strategy(new DiagonalPlacementStrategy(), new StayAliveStrategy(player2Name),
                1), player2Name);
        IPlayer player3 = new Player(new Strategy(new DiagonalPlacementStrategy(), new StayAliveStrategy(player3Name),
                1), player3Name);
        IPlayer player4 = new Player(new Strategy(new DiagonalPlacementStrategy(), new StayAliveStrategy(player4Name),
                1), player4Name);

        return Arrays.asList(player1, player2, player3, player4);
    }

    @Test
    public void RoundRobinAndPrintOutResult() {
        List<IPlayer> players = initializePlayers();
        List<IObserver> observers = new ArrayList<>();
        ITManager manager = new TManager(players, observers);
        manager.roundRobin();
        assertEquals(players.get(3).getNameInTournament(), "fish3");
        assertEquals(players.get(1).getNameInTournament(), "cat");
        assertEquals(manager.printOutResult(), "[\"cat\", \"fish\"]\n" +
                "[[\"winner\": \"dog\", \"loser\": \"cat\"],\n" +
                "[\"winner\": \"dog\", \"loser\": \"cat\"],\n" +
                "[\"winner\": \"dog\", \"loser\": \"cat\"],\n" +
                "[\"winner\": \"dog\", \"loser\": \"fish\"],\n" +
                "[\"winner\": \"dog\", \"loser\": \"fish\"],\n" +
                "[\"winner\": \"dog\", \"loser\": \"fish\"],\n" +
                "[\"winner\": \"dog\", \"loser\": \"fish3\"],\n" +
                "[\"winner\": \"dog\", \"loser\": \"fish3\"],\n" +
                "[\"winner\": \"dog\", \"loser\": \"fish3\"],\n" +
                "[\"winner\": \"fish3\", \"loser\": \"fish\"],\n" +
                "[\"winner\": \"fish3\", \"loser\": \"fish\"],\n" +
                "[\"winner\": \"fish3\", \"loser\": \"fish\"]]");

    }
}