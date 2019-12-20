import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String argv[]) throws Exception {

      String fromClient;
      StringBuilder toClient = new StringBuilder();
      ServerSocket welcomeSocket = new ServerSocket(8000);

      while (true) {
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient =
                new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        fromClient = inFromClient.readLine();
        System.out.println("Received: " + fromClient);
        String str = "[" +fromClient + "]";
        JSONParser parser = new JSONParser();
        JSONArray jsa = new JSONArray();
        //catch exceptions if the the input JSON objects are not completed.
        try {
          jsa = (JSONArray) parser.parse(str);
        } catch (ParseException e) {
          System.out.println(e);
          System.out.println("JSON objects are not complete");
        }
        for (int i = 0; i < jsa.size(); i++) {
          toClient.append("[" + (jsa.size() - 1 - i) + "," + jsa.get(i) + "]\n");
        }
        outToClient.writeBytes(toClient.toString());
        connectionSocket.close();
        fromClient = new String();
        toClient = new StringBuilder();

      }


    }

}
