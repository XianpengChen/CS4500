import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import board.IReadonlyBoard;
import data.Action;
import data.ActionType;
import data.Direction;
import data.Outcome;
import data.PlaceWorkerAction;
import data.Worker;
import player.IPlayer;
import strategy.Strategy;
import utils.JSONUtils;

/**
 * this is a remote proxy player implementing the player interface; it plays a player role on the server side;
 */
public class ClientHandler extends Thread implements IPlayer {
    private final Socket socket;
    private String playerName;
    //playing as in this tournament
    private String PA;
    private String opponent;
    private String currentPlacement;
    private String currentTurn;
    private final JSONUtils JTool;

    private volatile Boolean respondedYet;
    private InputStream input;
    private PrintStream output;
    private List<Outcome> outcomes;
    private final int index;

    //this int is to indicate which phase this player is in; if phase = 0,
    // it means it's in placement phase; if phase=1,
    //it's in turn phase;
    private int phase;

    ClientHandler(Socket socket, int index) {
        this.index = index;
        this.socket = socket;
        try {
            this.input = socket.getInputStream();
            this.output = new PrintStream(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.JTool = new JSONUtils();
        this.outcomes = new ArrayList<>();
        this.respondedYet = false;
    }

    @Override
    public void run() {
        BufferedReader sc1 = new BufferedReader(new InputStreamReader(input));
        while (true) {
            StringBuilder message = new StringBuilder();
            try {
                String line = sc1.readLine();
                if (line != null) {
                    message.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!message.toString().equals("")) {
                synchronized (this) {
                    if (playerName == null) {
                        playerName = message.toString();
                    } else if (phase == 0) {
                        currentPlacement = message.toString();
                        respondedYet = true;
                    } else if (phase == 1) {
                        currentTurn = message.toString();
                        respondedYet = true;
                    }
                }

            }
        }
    }

    /**
     * write this message to client
     * @param message a string
     */
    private void sendToClient(String message) {
        output.println(message);
    }

    @Override
    public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {

        List<Worker> WorkersOnBoard;
        if (PA != null) {
            WorkersOnBoard = b.getPlayerWorkers(PA);
        } else {
            WorkersOnBoard = b.getPlayerWorkers(playerName);
        }
        WorkersOnBoard.addAll(b.getPlayerWorkers(opponent));
        int i = WorkersOnBoard.size();
        try {
            sendToClient(JTool.WorkersToString(WorkersOnBoard));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("cannot send workers placement to client");
        }
        while (!respondedYet) {
        }
        String workerId = PA;
        if (i == 0 || i == 1) {
            workerId = workerId + "1";
        } else {
            workerId = workerId + "2";
        }
        workerId = "[\"" + workerId + "\", ";
        PlaceWorkerAction PWA = JTool.StringToPlaceWorkerAction(workerId + currentPlacement.substring(1));
        respondedYet = false;
        currentPlacement = null;
        return PWA;
    }

    @Override
    public List<Action> getTurn(IReadonlyBoard b) {
        String board = JTool.BoardToString(b);
        try {
            sendToClient(board);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (!respondedYet) {
        }
        //the player is giving up by sending his playerName;
        if (currentTurn.equals(playerName)) {
            Action move = new Action(ActionType.MOVE, playerName + "3",
                    new Direction("east", "put"));
            Action build = new Action(ActionType.BUILD, playerName + "3",
                    new Direction("east", "put"));
            return new ArrayList<>(Arrays.asList(move, build));
        }
        List<Action> actions = JTool.StringToActionsB(currentTurn);
        respondedYet = false;
        return actions;
    }

    /**
     * @return the player name of this client
     */
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void receiveOutcome(Outcome outcome) {
        outcomes.add(outcome);
    }

    @Override
    public Strategy getStrategy() {
        return null;
    }

    @Override
    public List<Outcome> getResults() {
        return outcomes;
    }

    @Override
    public void receiveNameInTournament(String nameInTournament) {
        PA = nameInTournament;
        output.println("[\"playing-as\" \"" + PA + "\"]");
    }

    @Override
    public String getNameInTournament() {
        return PA;
    }

    @Override
    public void setPhaseA() {
        phase = 0;
    }

    @Override
    public void setPhaseB() {
        phase = 1;
    }

    /**
     * assign an opponent to this player
     *
     * @param playAgainst name of the opponent
     */
    public void assignOpponent(String playAgainst) {
        opponent = playAgainst;
        sendToClient(playAgainst);
    }

    @Override
    public void receiveTournamentEncounter(String results) {
        sendToClient(results);
    }
}
