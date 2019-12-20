
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

import board.Board;
import board.IReadonlyBoard;
import data.Action;
import data.PlaceWorkerAction;
import player.IPlayer;
import utils.JSONUtils;

/**
 * This component exist on the client side and proxies the interaction between the player on one side and the tournament
 * manager and referee on the other. By linking this proxy with the player implementation, it becomes straightforward to
 * establish a TCP-based communication link between the client side and the server side
 */
public class XClient implements Runnable {
    private final IPlayer player;
    //playing-as name in the tournament
    private String PA;
    private final String IP;
    private final int PORT;
    private String opponent;
    private JSONUtils JTool;
    private volatile boolean tournamentRunning;
    public XClient(IPlayer player, String ip, int port) {
        this.player = player;
        PA = null;
        IP = ip;
        PORT = port;
        JTool = new JSONUtils();
        tournamentRunning = true;
    }

    @Override
    public void run() {
        Socket s = null;
        BufferedReader sc1 = null;
        PrintStream p = null;
        try {
            s = new Socket(IP, PORT);
            sc1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
            p = new PrintStream(s.getOutputStream(), true);
            p.println(player.getPlayerName());
            p.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (tournamentRunning) {
            try {
                StringBuilder messageFrom = new StringBuilder();
                String line = sc1.readLine();
                if (line != null) {
                    messageFrom.append(line);
                }
                if (!messageFrom.toString().equals("")) {
                    System.out.println("Server to " + player.getPlayerName() + ": " + messageFrom);
                    interpretMessageAndRespond(messageFrom.toString(), p, s);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("exception at trying to receive, interpret messages from the server and responding");
                System.exit(1);
            }
        }
    }

    /**
     * using the printStream to print out the response;
     * @param p PrintStream
     * @param response string
     */
    private void printToServer(PrintStream p, String response) {
        p.println(response);
    }

    /**
     * interpret the meesage from the tournament server and respond;
     * @param message string from server;
     * @param p printer to server;
     * @throws Exception player may be into infinite loop while preparing response;
     */
    private void interpretMessageAndRespond(String message, PrintStream p, Socket s) throws Exception {
        if (!message.contains("[")) {
            opponent = message;
        }else {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(message);
            if (array.size() == 2 && PA == null && array.get(0).equals("playing-as")) {
                PA = (String) array.get(1);
                player.receiveNameInTournament(PA);
            }
            //add "encounters results" tag to disambiguate the messages.
            else if (array.size() == 2 && array.get(0).equals("encounters results")) {
                tournamentRunning = false;
                s.close();
                Thread.currentThread().interrupt();
            }
            else if (array.size() <= 3) {
                String response = createWorkerAndReturnPlaceAction(array);
                printToServer(p, response);
            } else if (array.size() == 6) {
                String response = getCurrentBoardAndReturnAction(message);
                printToServer(p, response);
            } else {
                System.err.println("unable to interpret this message: " + message);
            }
        }
    }

    /**
     * interpret the input JSONArray as a list of WorkerPlacement and return a string representing a worker Placement
     * @param array JSONArray
     * @return string
     * @throws InterruptedException player may be into an infinite loop while getting a place worker action;
     */
    private String createWorkerAndReturnPlaceAction(JSONArray array) throws InterruptedException {
        Board board = new Board();
        for (Object anArray : array) {
            String worker = "";
            int column = -1;
            int row = -1;
            try {
                JSONArray WorkerPlace = (JSONArray) anArray;
                worker = (String) WorkerPlace.get(0);
                column = Math.toIntExact((Long) WorkerPlace.get(1));
                row = Math.toIntExact((Long) WorkerPlace.get(2));
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("cannot parse this JSONArray to a workerPlacement");
            }
            board.createWorker(worker, row, column);
        }
        PlaceWorkerAction PWA = player.getPlaceWorker(board.toViewModel());
        return JTool.PlaceWorkerActionToStringB(PWA);
    }

    /**
     * interpret a string as current game board, and return string representing a turn actions;
     * @param message string representation of currentn game board;
     * @return a string representing actions
     * @throws InterruptedException the player may be into a infinite loop;
     */
    private String getCurrentBoardAndReturnAction(String message) throws InterruptedException {
        IReadonlyBoard board = JTool.StringToBoard(message);
        List<Action> actions = player.getTurn(board);
        return JTool.ActionsToStringB(actions);
    }
}
