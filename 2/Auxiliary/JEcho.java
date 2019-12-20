import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Scanner;

// use this class to echo the JSON objects input from command line, the program prints all the JSON values,
// each on a separated line, embedded in a JSON array that indicates its reverse position ,
// in the stream (counting down to 0).
public class JEcho {
  //this the main method to fulfill the function we stated above.
  public static void main(String[] args) throws IOException, ParseException {
    //a stringbuilder to hold the input from command line.
    StringBuilder input = new StringBuilder();
    Scanner scanner = new Scanner(System.in);
    while(scanner.hasNextLine())
    {
      input.append(scanner.nextLine());
    }
    String str = "[" +input.toString() + "]";
    JSONParser parser = new JSONParser();
    JSONArray jsa = new JSONArray();
    //catch exceptions if the the input JSON objects are not completed.
    try {
      jsa = (JSONArray) parser.parse(str);
    } catch (ParseException e) {
      System.out.println(e);
      System.out.println("JSON objects are not complete");
    }
    //print out every JSON object in the jsa
    for (int i = 0; i < jsa.size(); i++) {
      System.out.println("[" + (jsa.size() - 1 - i) + "," + jsa.get(i) + "]");
    }
  }
}

