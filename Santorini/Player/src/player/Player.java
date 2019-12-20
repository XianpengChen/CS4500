package player;


import board.IReadonlyBoard;
import data.Action;
import data.Outcome;
import data.PlaceWorkerAction;
import data.Worker;
import strategy.Strategy;

import java.util.ArrayList;
import java.util.List;

// a concrete representation of a Player in a game of Santorini
public class Player implements IPlayer {

    static final String WORKER_ID = "%s%d";

    final String playerName;
    final Strategy strategy;
    List<Outcome> results;
    String nameInTournament;
    int phase;
    String opponent;

    // Constructs a new Player
    public Player(Strategy strategy, String playerName) {
        this.playerName = playerName;
        this.strategy = strategy;
        this.results = new ArrayList<>();
        this.phase = 0;
        this.nameInTournament = playerName;
    }


    @Override
    public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) throws InterruptedException {
        List<Worker> workersOnBoard = b.getPlayerWorkers(getPlayerName());
        int nextWorkerNumber = workersOnBoard.size() + 1;
        String workerToPlace = String.format(WORKER_ID, this.playerName, nextWorkerNumber);

        return strategy.getPlaceWorker(workerToPlace, b);
    }

    @Override
    public List<Action> getTurn(IReadonlyBoard b) throws InterruptedException {
        if (nameInTournament != null) {
            return strategy.getTurn(nameInTournament, b);
        } else {
            return strategy.getTurn(this.playerName, b);
        }
    }

    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    @Override
    public void receiveOutcome(Outcome outcome) {
        results.add(outcome);
    }

    @Override
    public Strategy getStrategy() {
        return strategy;
    }

    @Override
    public List<Outcome> getResults() {
        return results;
    }

    @Override
    public void receiveNameInTournament(String nameInTournament) {
        this.nameInTournament = nameInTournament;
    }

    @Override
    public String getNameInTournament() {
        return nameInTournament;
    }

    @Override
    public void setPhaseA() {
        phase = 0;
    }

    @Override
    public void setPhaseB() {
        phase = 1;
    }

    @Override
    public void assignOpponent(String playAgainst) {
        opponent = playAgainst;
    }

    @Override
    public void receiveTournamentEncounter(String results) {

    }

}
