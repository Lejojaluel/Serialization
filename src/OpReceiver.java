import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class receives a serialized BinaryOp and deserialize, performs calculations, and serializes the data and sends
 * back to the sender.
 *
 * @Author Leroy Valencia
 */
public class OpReceiver {
  private static InetAddress clientAddr = null;
  private static boolean verbose = true;

  /**
   * This method sets up the ServerSocket with a given port to listen on.
   * @param port the port to listen on.
   * @return the ServerSocket when its created.
   */
  private static ServerSocket setupServer(int port) {
    ServerSocket serverSocket = null; //Sets to null

    try { // Try catch for IOexception when creating ServerSocket
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.err.println("[ERROR] In setupServer. IOException cannot connect on port " + port);
      System.exit(1);
    }
    return serverSocket;
  }

  /**
   * Gets the next client in line to connect to.
   * @param server The serversocket that is created on the port.
   * @return a Socket that is connected to the client.
   */
  private static Socket getNextClient(ServerSocket server) {
    Socket client = null; // Sets to null
    try { // Try catch for Socket creation
      client = server.accept(); //Accept the incoming request.
    } catch (IOException e) {
      System.err.println("[ERROR] Cannot accept the client in getNextClient()");
      System.exit(1);
    }

    clientAddr = client.getInetAddress(); // Allows for information to be fetched from client
    System.out.println(
        "Incoming connection from "
            + clientAddr.getHostName()
            + " from "
            + clientAddr.getHostAddress()
            + ":"
            + client.getPort());
    return client; // Returns the client when connected.
  }

  /**
   * This method will send over the BinaryOp data back to the client using serializtion.
   * @param data The data being sent over, A BinaryOP
   * @param server the Socket in which to communicate with
   */
  private static void send(BinaryOp data,Socket server) {
    System.out.println("Sending "+data.toString()); //print what is sending
    ObjectOutputStream serverObj = null; //Create to null
    try { //These are necessary tries but need to be seperate to avoid pokemon exception handling.
      verbose("initiating object output stream...");
      serverObj = new ObjectOutputStream(server.getOutputStream());
    } catch (IOException e) {
      System.err.println("[ERROR] In creating the objectOutPutStream");
      System.exit(1);
    }

    try {
      verbose("writing object");
      serverObj.writeObject(data);
    } catch (IOException e) {
      System.err.println("[ERROR] In writing the Object to client");
      System.exit(1);
    }

    try {
      verbose("flushing stream");
      serverObj.flush();
    } catch (IOException e) {
      System.err.println("[ERROR] In flushing the stream out to the client");
      System.exit(1);
    }
    System.out.println("Sent to server..."); //End of sending
  }

  /**
   * This method will calculate the given equation based on the given binary operation
   * @param data the BinaryOp to use
   * @return the result in a double form
   */
  public static double calculate(BinaryOp data) {
    System.out.println("Calculating...");
    double left = data.getLeft(); //set the left to the binaryop left
    double right = data.getRight(); // set the right to the binaryop right
    switch (data.getOperator()) { // Switch case to perform calculations
      case ADD:
        return left + right; //Addition
      case SUBTRACT:
        return left - right; //Subtraction
      case DIVIDE:
        return right != 0 ? left / right : 0; //Division catches the 0 if not caught before
      case MULTIPLY:
        return left * right; //Multiplication
      default:
        System.err.println("IMPOSSIBLE IN SWITCH CASE ");
        System.exit(1);
    }
    System.err.println("Impossible reach in switch statement");
    return 0;
  }

  /**
   * The main method because it needs to always be waiting for input.
   * @param args
   */
  public static void main(String[] args) {
    final int port = 4096; // only listen on this port
    ServerSocket server = setupServer(port); // setup server for use
    //Set to null outside the while loop
    Socket client = null;
    BinaryOp data = null;
    ObjectInputStream in = null;

    System.out.println("\nSetting up listener on port " + port + "...");

    while (true) { //While true because it always needs to respond.
      client = getNextClient(server); //Get the next client in line

      try { //These are all in seperate trys to prevent pokemon exception handling
        in = new ObjectInputStream(client.getInputStream());
      } catch (IOException e) {
        System.err.println("[ERROR] in Getting the new ObjectInputStream");
        System.exit(1);
      }
      verbose("Obtained input stream!");

      try {
        data = (BinaryOp) in.readObject(); //Read the object from client and cast to BinaryOp
        System.out.println("Recieved object is: "+ data.toString());
      } catch (IOException ioe) {
        ioe.printStackTrace();
      } catch (ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
      }
      verbose("Object Received!");
      //Perform Calculation
      data.setResult(calculate(data));
      //Send data back to client
      send(data,client);

      //Close the connection once finished.
      try {
        client.close();
        System.out.println("Connection Closed");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This just allows for more information to be outputted or not
   * @param s The string to print.
   */
  private static void verbose(String s) {
    if (verbose) {
      System.out.println(s);
    }
  }
}
