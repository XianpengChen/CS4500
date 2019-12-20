import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  public Socket clientSocket;
  public PrintWriter out;
  public BufferedReader in;
  public static Scanner sc = new Scanner(System.in);
  public static StringBuilder commands = new StringBuilder();
  public static JSONParser parser = new JSONParser();

  public void connect(String ip, int port) throws Exception {
    clientSocket = new Socket(ip, port);
    out = new PrintWriter(clientSocket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }

  public String validateUser() throws Exception {
    // generate random string
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890abcdefghijklmnopqrstuvwxyz";
    StringBuilder id = new StringBuilder();
    int len = 20;

    for (int i = 0; i < len; i++) {
      id.append(chars.charAt((int) (Math.random() * chars.length())));
    }

    out.println(id);

    // receive response of internal id
    String internalId = in.readLine();
    return internalId;
  }

  public void readInput() {
//    JSONArray jsa = new JSONArray();
//    try {
//      jsa = (JSONArray) parser.parse(sc.nextLine());
//      commands.append(jsa);
//    } catch (ParseException e) {
//      System.out.println(e);
//    }
//
//    if (jsa.get(0) != "at") {
//      readInput();
//    }
//    else return;

    while (sc.hasNextLine()) {
      commands.append(sc.nextLine());
    }
  }

  public String sendReceive() throws IOException {
    out.println(commands);
    commands = new StringBuilder();
    StringBuilder receive = new StringBuilder();
    String str;
    while ((str = in.readLine()) != null) {
      receive.append(str);
    }
    return receive.toString();
  }

  public void closeConnection() throws Exception {
    in.close();
    out.close();
    clientSocket.close();
  }

  public static void main(String args[]) throws Exception {
    Client client = new Client();

    client.connect("antarctica.ccs.neu.edu", 8000);
    client.validateUser();
      client.readInput();
      String rec = client.sendReceive();
      System.out.println(rec);

    client.closeConnection();
  }
}