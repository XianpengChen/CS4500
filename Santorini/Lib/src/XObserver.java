import Referee.IReferee;
import Referee.Referee;
import observer.IObserver;
import observer.Observer;
import player.IPlayer;
import player.Player;
import rules.StandardSantoriniRulesEngine;
import strategy.*;

import java.util.Arrays;
import java.util.List;

public class XObserver {
    private static List<IPlayer> initializePlayers() {
        String playerToTest = "Jason";
        String opponent = "Sam";
        ITurnStrategy turnStrategy1 = new StayAliveStrategy(playerToTest, opponent);
        ITurnStrategy turnStrategy2 = new StayAliveStrategy(opponent, playerToTest);
        Strategy strategy1 = new Strategy(new DiagonalPlacementStrategy(), turnStrategy1, 1);
        Strategy strategy2 = new Strategy(new CartesianDistancePlacementStrategy(playerToTest),
                turnStrategy2, 1);
        IPlayer player1 = new Player(strategy1, playerToTest);
        IPlayer player2 = new Player(strategy2, opponent);
        return Arrays.asList(player1, player2);
    }

    public static void main(String[] args) {
        List<IPlayer> players = initializePlayers();
        List<IObserver> observers = Arrays.asList(new Observer());
        IReferee referee1 = new Referee(players, new StandardSantoriniRulesEngine(), observers);
        referee1.runAGame();
    }
}
