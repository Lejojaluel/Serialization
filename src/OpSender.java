import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;

public class OpSender {
  private static Socket Connect(String host, int port) {
    Socket server = null;
    try {
      server = new Socket(host, port);
    } catch (UnknownHostException e) {
      // TODO Change error messages
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return server;
  }

  public static void main(String[] args) {
    // String host = args[0];
    String host = "localhost";
    //  Integer port = Integer.parseInt(args[1]);
    Integer port = 4096;
    BinaryOp data = new BinaryOp(1.2, '+', 3.4);

    Socket server = Connect(host, port);
      ObjectOutputStream serverObj = null;
    try{
        serverObj = new ObjectOutputStream(server.getOutputStream());
    } catch (IOException e) {
        //TODO change error message
        e.printStackTrace();
    }

    try{
        serverObj.writeObject(data);
    } catch (IOException e) {
        //TODO change error message
        e.printStackTrace();
    }

    try{
        serverObj.flush();
    } catch (IOException e) {
        //TODO change error message
        e.printStackTrace();
    }

    System.out.println("Sent " + data.toString() + " to server...");
  }
}
