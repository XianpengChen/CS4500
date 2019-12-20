import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import TManager.ITManager;
import TManager.TManager;
import observer.IObserver;
import player.IPlayer;

/**
 * this is the implementation of tournament server. it runs a TCP server and wait for clients connection.
 */
public class XServer {
    private static int minPlayer = -1;
    private static int PORT = -1;
    private static int waitingFor = -1;
    private static int repeat = -1;
    private static int counter = 0;
    public static void main(String args[]) throws IOException {
        readConfig();
        ServerSocket server = new ServerSocket(PORT);
        server.setSoTimeout(waitingFor * 1000);
        List<IPlayer> clients = new ArrayList<>();
        boolean reachMin =false;
        Socket socket;
        //wait for players to sign up
        while (!reachMin) {
            try {
                socket = server.accept();
                ClientHandler t = new ClientHandler(socket, counter);
                counter++;
                t.start();
                clients.add(t);
            } catch (IOException e) {
                System.err.println("I/O error: " + e);
                continue;
            }
            if (clients.size() == minPlayer) {
                reachMin = true;
            }
        }
        List<IObserver> observers = new ArrayList<>();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ITManager tournamentManager = new TManager(clients, observers);
        tournamentManager.roundRobin();
        tournamentManager.printOutEncounters();
        System.exit(0);
    }
    /**
     * read in configuration from STDIN, then apply the configuration;
     */
    private static void readConfig() {
        Scanner scanConfig = new Scanner(System.in);
        StringBuilder config = new StringBuilder();
        while (scanConfig.hasNextLine()) {
            config.append(scanConfig.nextLine());
        }
        applyConfig(config.toString());
    }
    /**
     * apply the configuration to this server
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
        minPlayer = Math.toIntExact((Long) config.get("min players"));
        PORT = Math.toIntExact((Long) config.get("port"));
        waitingFor = Math.toIntExact((Long) config.get("waiting for"));
        repeat = Math.toIntExact((Long) config.get("repeat"));
        if (minPlayer < 0 || (PORT < 50000 || PORT > 60000) || waitingFor <= 0 || (repeat != 0 && repeat != 1)) {
            System.err.println("the configuration is not in the right format");
            System.exit(1);
        }
    }

    /**print out the config to a string
     * @return string
     */
    public String printConfig() {
        return "min players: " + minPlayer + "\n" + "port: " + PORT + "\n" + "waiting for: " + waitingFor + "\n" +
                "repeat: " + repeat;
    }
}
