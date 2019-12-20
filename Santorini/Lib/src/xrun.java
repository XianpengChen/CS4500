import TManager.ITManager;
import TManager.TManager;
import observer.IObserver;
import observer.Observer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import player.*;
import strategy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class xrun {
    public static void main(String[] args) {
        ITManager manager = new TManager();
        manager.roundRobin();
        String results = manager.printOutResult();
        System.out.println(results);
    }
}
