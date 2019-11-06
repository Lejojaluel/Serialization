import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class OpReceiver {
  private static InetAddress clientAddr = null;
  private static String returnHost = "";
  private static int returnPort = 0;
  private static boolean verbose = true;

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
    returnHost = clientAddr.getHostAddress();
    returnPort = client.getPort();
    System.out.println(
        "Incoming connection from "
            + clientAddr.getHostName()
            + " from "
            + clientAddr.getHostAddress()
            + ":"
            + client.getPort());
    return client;
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

  private static void send(BinaryOp data, String host, int port) {
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

  public static double calculate(double left, double right, Op operator) {
    System.out.println("Calculating...");
    switch (operator) {
      case ADD:
        return left + right;
      case SUBTRACT:
        return left - right;
      case DIVIDE:
        return right != 0 ? left / right : 0;
      case MULTIPLY:
        return left * right;
      default:
        System.err.println("IMPOSSIBLE IN SWITCH CASE ");
        System.exit(1);
    }
    System.err.println("Impossible reach in switch statement");
    return 0;
  }

  public static void main(String[] args) {
    final int port = 4096;
    ServerSocket server = null;
    Socket client = null;
    BinaryOp data = null;
    ObjectInputStream in = null;

    server = setupServer(port);

    System.out.println("Setting up listener on port " + port + "...");
    while (true) {
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

      // TODO perform calculation
      data.setResult(calculate(data.getLeft(), data.getRight(), data.getOperator()));
      // System.out.println(data.toStrings());
      // TODO set result and send back
      send(data, returnHost, 4444);
      // System.out.println(data.toString());

      try {
        client.close();
        System.out.println("Connection Closed");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } // end of while true
  }

  private static void verbose(String s) {
    if (verbose) {
      System.out.println(s);
    }
  }
}
