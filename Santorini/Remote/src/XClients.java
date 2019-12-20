import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import player.BreakerPlayer;
import player.IPlayer;
import player.InfinitePlacePlayer;
import player.InfiniteTurnPlayer;
import player.Player;
import strategy.DiagonalPlacementStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

/**
 * this is the class for xclients test harness;
 */
public class XClients {
    private static List<IPlayer> players = new ArrayList<>();
    private static String IP;
    private static int PORT;
    public static void main(String[] args) {
        readConfig();
        System.out.println(IP);
        System.out.println(PORT);
        for (IPlayer player: players) {
            XClient client = new XClient(player, IP, PORT);
            Thread t = new Thread(client);
            t.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readConfig() {
        Scanner scanConfig = new Scanner(System.in);
        StringBuilder config = new StringBuilder();
        while (scanConfig.hasNextLine()) {
            config.append(scanConfig.nextLine());
        }
        applyConfig(config.toString());
    }

    /**apply configuration to this XClients
     * @param configuration string
     */
    public static void applyConfig(String configuration) {
        JSONParser parser = new JSONParser();
        JSONObject config = new JSONObject();

        try {
            config = (JSONObject) parser.parse(configuration);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        IP = (String) config.get("ip");
        PORT =  Math.toIntExact((Long) config.get("port"));
        JSONArray JPlayers = (JSONArray) config.get("players");
        addPlayers(JPlayers);
    }

    /**
     * print the configuration to a string
     * @return string
     */
    public String printConfig() {
        StringBuilder config = new StringBuilder("players: \n");
        for (IPlayer player: players) {
            config.append(player.getPlayerName()).append(" ");
            if (player instanceof BreakerPlayer) {
                config.append("breaker\n");
            }
            else if (player instanceof InfinitePlacePlayer) {
                config.append("infinite at placement\n");
            }
            else if (player instanceof InfiniteTurnPlayer) {
                config.append("infinite at turn\n");
            }
            else {
                config.append("good\n");
            }
        }
        config.append("IP: ").append(IP).append("\n").append("PORT: ").append(PORT);
        return config.toString();
    }

    private static void addPlayers(JSONArray JPlayers) {
        JSONArray currentPlayer = null;
        String playerName = null;
        String pathString = "";
        for (Object Jplayer : JPlayers) {
            try {
                currentPlayer = (JSONArray) Jplayer;
                playerName = (String) currentPlayer.get(1);
                pathString = (String) currentPlayer.get(2);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println("cannot cast players from the configuration");
            }
            Strategy strategy = new Strategy(new DiagonalPlacementStrategy(), new StayAliveStrategy(playerName), 1);
            switch ((String) currentPlayer.get(0)) {
                case "good":
                    players.add(new Player(strategy, playerName));
                    break;
                case "breaker":
                    players.add(new BreakerPlayer(strategy, playerName));
                    break;
                case "infinite":
                    if (pathString.contains("InfinitePlacePlayer")) {
                        players.add(new InfinitePlacePlayer(strategy, playerName));
                    } else if (pathString.contains("InfiniteTurnPlayer")){
                        players.add(new InfiniteTurnPlayer(strategy, playerName));
                    }
                    break;
                default: System.err.println("players can only be \"good\", \"breaker\" or \"infinite\"");
            }
        }
    }
}
