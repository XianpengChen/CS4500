import com.fasterxml.jackson.databind.JsonNode;
import data.Action;
import rules.IRulesEngine;
import rules.StandardSantoriniRulesEngine;
import testdata.Sequence;
import utils.InputParser;
import utils.SequenceParser;

import java.io.IOException;
import java.util.List;

public class XRules {

    public static void main(String[] args) throws IOException {

        IRulesEngine rules = new StandardSantoriniRulesEngine();

        List<JsonNode> testInputs = InputParser.parse(System.in);

        List<Sequence> sequences = SequenceParser.parseSequences(testInputs);

        for (Sequence sequence : sequences) {
            List<Action> actions = sequence.getActions();
            Action firstAction = actions.get(0);
            String workerId = firstAction.getWorkerId();
            String playerName = workerId.substring(0, workerId.length() - 1);
            boolean result = rules.isTurnLegal(sequence.getBoard().toViewModel(), actions, playerName);

            if (result) {
                System.out.println("\"yes\"");
            } else {
                System.out.println("\"no\"");
            }
        }
    }
}
