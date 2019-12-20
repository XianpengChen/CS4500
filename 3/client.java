import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Scanner;

public class Client {
  public static void main(String args[]) {

    Scanner reader = new Scanner(System.in);
    JSONParser parser = new JSONParser();
    JSONArray jarr = new JSONArray();
    HashMap<String, SpreadSheet> spreadMap = new HashMap<String, SpreadSheet>();

    while(reader.hasNextLine()) {
      String str = reader.nextLine();

      // no checks here as the spec enforces well-formed json. Will crash if invalid
      try {
        jarr = (JSONArray) parser.parse(str);
      } catch (ParseException e) {
        System.out.println("Error: " + e.getMessage());
      }

      String op = jarr.get(0).toString();

      switch(op) {
        case "sheet":
          JSONArray jarrInner = new JSONArray();
          try {
            jarrInner = (JSONArray) parser.parse(jarr.get(2).toString());
          } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
          }

          int b = ((JSONArray)jarrInner.get(0)).size();
          Formula[][] fArr = new Formula[jarrInner.size()][b];
          for (int i = 0; i < jarrInner.size(); i++) {
            for (j = 0; j < b; j++) {
              JSONArray jsa = (JSONArray)jarrInner.get(i);
              f[i][j] = jsa.get(j);
            }
          }
          SpreadSheet spread = new SpreadSheet(fArr);
          String name = jarr.get(1).toString();
          spreadMap.put(name, spread);

          break;
        case "set":
          SpreadSheet current = spreadMap.get(jarr.get(1).toString());
          int x = (int) jarr.get(2);
          int y = (int) jarr.get(3);
          Formula f = (Formula) jarr.get(4);

          current.insertFormula(x, y, f);

          break;
        case "at":
          Spreadsheet curr = spreadMap.get(jarr.get(1).toString());
          int xx = (int) jarr.get(2);
          int yy = (int) jarr.get(3);

          Formula ff = curr.locateCell(xx, yy);
          System.out.println("FOUND: " + ff);
          break;

        default:
          throw new IllegalArgumentException("Bad command");
      }
    }

  }
}