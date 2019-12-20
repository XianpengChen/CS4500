import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
    public static void main(String argv[]) throws Exception {
      StringBuilder fromUser = new StringBuilder("");
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      Socket clientSocket = new Socket("localhost", 8000);
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      String str;
      while ((str = inFromUser.readLine()) != null) {
        fromUser.append(str);
      }
      outToServer.writeBytes(fromUser + "\n");


      System.out.println("FROM SERVER: ");
      String str2;
      while ((str2 = inFromServer.readLine()) != null) {
        System.out.println(str2);
      }
      clientSocket.close();
    }

}
