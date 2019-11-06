import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class OpReceiver {
  private static ServerSocket setupServer(int port){
    ServerSocket serverSocket = null;

    try{
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return serverSocket;
  }

  private static Socket getNextClient(ServerSocket server){
    Socket client = null;
    try{
      client = server.accept();
    } catch (IOException e) {
      e.printStackTrace();
    }

    InetAddress clientAddr = client.getInetAddress();
    System.out.println(
        "Incoming connection from " + clientAddr.getHostName() +
        " from " + clientAddr.getHostAddress() +
        ":" + client.getPort());
    return client;
  }

  public static void main(String[] args) {
    final int port = 4096;
    ServerSocket server = null;
    Socket client = null;
    BinaryOp data = null;
    ObjectInputStream in = null;

    server = setupServer(port);

    System.out.println("Setting up listener on port " + port+ "...");
    while(true){
      client = getNextClient(server);

      try{
        in = new ObjectInputStream(client.getInputStream());
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println("Obtained input stream!");

      try{
        data = (BinaryOp) in.readObject();
        System.out.println(data.toStrings());
      } catch (IOException ioe) {
        ioe.printStackTrace();
      } catch (ClassNotFoundException cnfe){
        cnfe.printStackTrace();
      }

      System.out.println("Object Received!");

      System.out.println("Attempting to print toString from Object");



      try{
        client.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println("Connection closed.");


    }
  }
}
