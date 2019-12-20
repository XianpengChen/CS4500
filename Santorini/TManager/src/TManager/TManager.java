package TManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import Referee.IReferee;
import Referee.Referee;
import data.Outcome;
import observer.IObserver;
import observer.Observer;
import player.BreakerPlayer;
import player.IPlayer;
import player.InfinitePlacePlayer;
import player.InfiniteTurnPlayer;
import player.Player;
import rules.StandardSantoriniRulesEngine;
import strategy.DiagonalPlacementStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;


/**
 * this TManager manages a tournament running between
 * Policy on Names If the tournament manager discovers that several players with the same name are to
 * participate in a tournament, it picks a unique name for all but one of the players and informs
 * these players of the chosen names.
 * Policy on Broken Players When a tournament manager is informed that a player misbehaved, it acts
 * as follows:
 * <p>
 * 1.The player is removed from all future encounters; there are no second chances.
 * 2. All past meet-ups involving the player are counted as won by the opponent.
 * <p>
 * If a player breaks after winning past games due to its opponents' breakage, the game is
 * eliminated from the tournament evaluation.
 */

public class TManager implements ITManager {

    private final static String reason = " is a breaker or into infinite loop";
    private final static int gamesOfSeries = 3;
    //the original list of players
    private final List<IPlayer> players;
    //modified the players to let them have unique names;
    private List<IPlayer> playersWithUniqueNames;
    //list of observers
    private final List<IObserver> obs;
    //list of index(of playersWithUniqueNames) of players who misbehaved;
    private  List<Integer> indexOfMisBehavedPlayers;
    //list of games played in every series;
    private  List<Outcome> outcomes;
    //list of meetUps between players;
    private  List<Outcome> meetUps;

    public TManager(List<IPlayer> players, List<IObserver> observers) {
        this.players = players;
        this.playersWithUniqueNames = nameToPlayers(players);
        this.obs = observers;
        this.indexOfMisBehavedPlayers = new ArrayList<>();

        this.outcomes = new ArrayList<>();
        this.meetUps = new ArrayList<>();
    }

    //build a TManager from STDIN
    public TManager() {
        String configuration = scanInConfiguration();
        List<IPlayer> players1 = getPlayersFromConfig(configuration);
        List<IObserver> observers1 = getObserversFromConfig(configuration);
        this.players = players1;
        this.playersWithUniqueNames = Collections.synchronizedList(nameToPlayers(players1));
        this.obs = observers1;
        this.indexOfMisBehavedPlayers = Collections.synchronizedList(new ArrayList<>());
        this.outcomes = new ArrayList<>();
        this.meetUps = new ArrayList<>();
    }


    @Override
    public void roundRobin() {
        int size = playersWithUniqueNames.size();
        synchronized (this) {
        for (int i = 0; i < size; i++) {
            i = increaseUntilOut(i);
            if (i < size) {
                for (int j = i + 1; j < size; j++) {
                    j = increaseUntilOut(j);
                    if (j < size) {
                        IPlayer player1 = playersWithUniqueNames.get(i);
                        IPlayer player2 = playersWithUniqueNames.get(j);
                        player1.assignOpponent(player2.getNameInTournament());
                        player2.assignOpponent(player1.getNameInTournament());
                        IReferee ref = new Referee(Arrays.asList(player1, player2),
                                new StandardSantoriniRulesEngine(), obs);
                        List<Outcome> outs = ref.playNGames(gamesOfSeries);
                        meetUps.add(ref.mergeOutcomes(outs));
                        outcomes.addAll(outs);
                        eliminateCheater(outs, i, j);
                        if (indexOfMisBehavedPlayers.contains(i)) {
                            break;
                        }
                    }
                }
            }
        }

        }
    }

    /**
     * add the index of cheaters in players to indexOfMisBehavedPlayers
     *
     * @param outs given a list of Outcome(size = gamesOfSeries)
     * @param i    represent an index of a player in players;
     * @param j    represent an index of a player in players;
     */
    private void eliminateCheater(List<Outcome> outs, int i, int j) {
        for (Outcome out : outs) {
            if (out.getReason().contains(reason)) {
                String loser = out.getLoser();
                if (playersWithUniqueNames.get(i).getNameInTournament().equals(loser)) {
                    if (!indexOfMisBehavedPlayers.contains(i)) {
                        indexOfMisBehavedPlayers.add(i);
                    }
                } else {
                    if (!indexOfMisBehavedPlayers.contains(j)) {
                        indexOfMisBehavedPlayers.add(j);
                    }
                }
            }

        }
    }

    /**
     * input a, keep increasing it until it's not contained in the list of indexOfMisBehavedPlayers.
     *
     * @param a an int
     * @return an int
     */
    private int increaseUntilOut(int a) {
        int i = a;
        while (indexOfMisBehavedPlayers.contains(i)) {
            i++;
        }
        return i;
    }


    @Override
    public String printOutResult() {
        StringBuilder cheaters = new StringBuilder("[");
        for (int i = 0; i < indexOfMisBehavedPlayers.size(); i++) {
            String name = "\"" + players.get(indexOfMisBehavedPlayers.get(i)).getNameInTournament() + "\"";
            if (i != indexOfMisBehavedPlayers.size() - 1) {
                name = name + ", ";
            }
            cheaters.append(name);
        }
        cheaters.append("]");
        StringBuilder games = new StringBuilder("[");
        List<Outcome> legalOuts = filterOutCheatersGames();
        for (int i = 0; i < legalOuts.size(); i++) {
            String game = "[\"winner\": \"" + legalOuts.get(i).getWinner() + "\", \"loser\": \"" +
                    legalOuts.get(i).getLoser() + "\"" + "]";
            if (i != legalOuts.size() - 1) {
                game = game + ",\n";
            }
            games.append(game);
        }
        games.append("]");
        return cheaters + "\n" + games;
    }

    @Override
    public void printOutEncounters() {
        System.out.println(encountersToString());
        ////add "encounters results" tag to disambiguate the messages.
        String results = "[\"encounters results\", " + encountersToString() + "]";
        for (IPlayer player : players) {
            player.receiveTournamentEncounter(results);
        }
    }

    /**
     * translate encounters results to string in JSON format for server-client communication;
     * @return string
     */
    private String encountersToString() {
        List<Outcome> encounters = adjustCheatersMeetUp();
        StringBuilder prints = new StringBuilder("[");
        for (int i = 0; i < encounters.size(); i++) {
            String curr = "[";
            curr = curr + "\""+encounters.get(i).getWinner() + "\", \"" + encounters.get(i).getLoser() + "\"";
            if (isHeMisBehaved(encounters.get(i).getLoser())) {
                curr = curr + ", \"irregular\"]";
            }
            else {
                curr = curr + "]";
            }
            if (i!= encounters.size()-1) {
                curr = curr + ",";
            }
            prints.append(curr);
        }
        return prints + "]";
    }

    /**
     * If a player breaks after winning past games due to its opponents' breakage, the game is
     * eliminated from the tournament evaluation. we are doing it here.
     *
     * @return a list of outcome after filtering as above
     */
    private List<Outcome> filterOutCheatersGames() {
        return outcomes.stream()
                .filter(out -> !(isHeMisBehaved(out.getWinner()) && out.getReason().contains(reason)))
                .collect(Collectors.toList());
    }

    /**
     * if a player misbehaved after a meetup, then that meetup is counted won by the opponent; if both
     * player misbehaved after a meetup, then that meetup is eliminated;
     *
     * @return a list of outcome;
     */
    private List<Outcome> adjustCheatersMeetUp() {
        List<Outcome> finalMeetUps = new ArrayList<>();
        for (Outcome out : meetUps) {
            if (isHeMisBehaved(out.getWinner()) && !isHeMisBehaved(out.getLoser())) {
                finalMeetUps.add(new Outcome(out.getLoser(), out.getWinner(), out.getGamesWon(),
                        gamesOfSeries, out.getWinner() + reason));
            } else if (isHeMisBehaved(out.getWinner()) && isHeMisBehaved(out.getLoser())) {
                //do not add this outcome;
            } else {
                finalMeetUps.add(out);
            }
        }
        return finalMeetUps;
    }

    /**
     * to determine if a player was misbehaved
     *
     * @param UPlayerName the player name
     * @return boolean, true means he misbehaved; false means not;
     */
    private boolean isHeMisBehaved(String UPlayerName) {
        for (Integer in : indexOfMisBehavedPlayers) {
            if (playersWithUniqueNames.get(in).getPlayerName().equals(UPlayerName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * for the name policy, to make sure players are with unique names
     *
     * @param players list of original players
     * @return a list of players with uniques names, but others(Strategy and methods) are the same.
     */
    private List<IPlayer> nameToPlayers(List<IPlayer> players) {
        List<String> playerNamesUsed = new ArrayList<>();
        List<IPlayer> playersWithUniqueNames = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            IPlayer player = players.get(i);
            String name = player.getPlayerName().toLowerCase();
            while (playerNamesUsed.contains(name)) {
                name = name + i;
            }
            player.receiveNameInTournament(name);
            playersWithUniqueNames.add(player);
            playerNamesUsed.add(name);
        }
        return playersWithUniqueNames;
    }

    /**
     * scan in configuration from STDIN as a string
     *
     * @return a String
     */
    private String scanInConfiguration() {
        Scanner sc = new Scanner(System.in);
        StringBuilder line = new StringBuilder();
        while (sc.hasNextLine()) {
            line.append(sc.nextLine());
        }
        return line.toString();
    }

    /**
     * get the list of players from configuration string;
     *
     * @param configuration a string to represent a configuration
     * @return a list of IPlayer
     */
    private List<IPlayer> getPlayersFromConfig(String configuration) {
        JSONParser parser = new JSONParser();
        JSONArray players = new JSONArray();
        try {
            JSONObject config = (JSONObject) parser.parse(configuration);
            players = (JSONArray) config.get("players");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<IPlayer> players1 = new ArrayList<>();
        for (Object player : players) {
            JSONArray currentPlayer = null;
            String playerName = null;
            String pathString = "";
            try {
                currentPlayer = (JSONArray) player;
                playerName = (String) currentPlayer.get(1);
                pathString = (String) currentPlayer.get(2);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("cannot cast to players");
            }
            Strategy strategy = new Strategy(new DiagonalPlacementStrategy(), new StayAliveStrategy(playerName), 1);
            switch ((String) currentPlayer.get(0)) {
                case "good":
                    players1.add(new Player(strategy, playerName));
                    break;
                case "breaker":
                    players1.add(new BreakerPlayer(strategy, playerName));
                    break;
                case "infinite":
                    if (pathString.contains("InfinitePlacePlayer")) {
                        players1.add(new InfinitePlacePlayer(strategy, playerName));
                    } else if (pathString.contains("InfiniteTurnPlayer")) {
                        players1.add(new InfiniteTurnPlayer(strategy, playerName));
                    }
                    break;
                default:
                    System.err.println("players can only be \"good\", \"breaker\" or \"infinite\"");
            }
        }
        return players1;
    }

    /**
     * get the list of observers from the configuration string
     *
     * @param configuration a string to represent a configuration
     * @return a list of observers;
     */
    private List<IObserver> getObserversFromConfig(String configuration) {
        JSONParser parser = new JSONParser();
        JSONArray observers = new JSONArray();
        try {
            JSONObject config = (JSONObject) parser.parse(configuration);
            observers = (JSONArray) config.get("observers");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<IObserver> observers1 = new ArrayList<>();
        for (int i = 0; i < observers.size(); i++) {
            observers1.add(new Observer());
        }
        return observers1;
    }
}
