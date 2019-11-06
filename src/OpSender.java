import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;

public class OpSender {
  private static boolean verbose = true;
  private static InetAddress clientAddr = null;
  private static String host = null;
  private static Integer port = 0;
  private static BinaryOp rdata;
  private static boolean listen = true;

  public OpSender(String host, Integer port) {
    // TODO validate the host and ports
    this.host = host;
    this.port = port;
  }

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

  private static void setupListener(int port) {
    ServerSocket server = null;
    Socket client = null;
    BinaryOp data = null;
    ObjectInputStream in = null;

    server = setupServer(port);
    System.out.println("Setting up listener on port " + port + "...");
    while (listen) {
      client = getNextClient(server);

      try {
        in = new ObjectInputStream(client.getInputStream());
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println("Obtained input stream!");

      try {
        data = (BinaryOp) in.readObject();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      } catch (ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
      }
      System.out.println("Object Received!");
      // TODO print results recieved
      System.out.println(data.toString());
      try {
        client.close();
        System.out.println("Connection Closed");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } // end of while true
  }

  private static ServerSocket setupServer(int port) {
    ServerSocket serverSocket = null;

    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return serverSocket;
  }

  private static Socket getNextClient(ServerSocket server) {
    Socket client = null;
    try {
      client = server.accept();
    } catch (IOException e) {
      e.printStackTrace();
    }

    clientAddr = client.getInetAddress();
    System.out.println(
        "Incoming connection from "
            + clientAddr.getHostName()
            + " from "
            + clientAddr.getHostAddress()
            + ":"
            + client.getPort());
    return client;
  }

  public static void send(BinaryOp data) {
    Socket server = Connect(host, port);
    ObjectOutputStream serverObj = null;
    try {
      verbose("initiating object output stream...");
      serverObj = new ObjectOutputStream(server.getOutputStream());
    } catch (IOException e) {
      // TODO change error message
      e.printStackTrace();
    }

    try {
      verbose("writing object");
      serverObj.writeObject(data);
    } catch (IOException e) {
      // TODO change error message
      e.printStackTrace();
    }

    try {
      verbose("flushing stream");
      serverObj.flush();
    } catch (IOException e) {
      // TODO change error message
      e.printStackTrace();
    }

    System.out.println("Sent to server...");
    data.toString();
  }

  public static void main(String[] args) {
    host = "localhost";
    port = 4096;

    // ADDITION TEST
    BinaryOp data = new BinaryOp(1.2, '+', 3.4);
    verbose("Binary Op created with " + data.getLeft());
    send(data);

    // SUBTRACTION TEST
    BinaryOp data1 = new BinaryOp(5, '-', 2);
    verbose("Binary Op created with " + data.getLeft());
    send(data1);
    // MULTIPLY TEST
    BinaryOp data2 = new BinaryOp(2, '*', 30);
    verbose("Binary Op created with " + data.getLeft());
    send(data2);
    // DIVIDE TEST
    BinaryOp data3 = new BinaryOp(100, '/', 5);
    verbose("Binary Op created with " + data.getLeft());
    send(data3);

    setupListener(4444);
  }

  public static void verbose(String s) {
    if (verbose) {
      System.out.println(s);
    }
  }
}
