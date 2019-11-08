import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * This class will send a BinaryOp to our receiver using serialization
 * and print out the value it receives back.
 *
 * @Author Leroy Valencia
 */
public class OpSender {
  private static boolean verbose = true;
  private static String host = null;
  private static Integer port = 0;

  /**
   * This method connects to our server and returns the connection.
   * @param host The host to connect to.
   * @param port The port to connect to.
   * @return the Socket once the connection has been made.
   */
  private static Socket Connect(String host, int port) {
    Socket server = null;
    try { // Try catch for socket creation
      if(host.equals("null") || port == 0){
        System.err.println("Invalid Host or Port "+host+":"+port);
        System.exit(1);
      }else{
        server = new Socket(host, port);
      }
    } catch (UnknownHostException e) {
      System.err.println("[ERROR] Unknown Host connection refused to "+host+":"+port);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("[ERROR] IOexception connection cannot be made with "+host+":"+port);
      System.exit(1);
    }
    return server; //return the connection
  }

  /**
   * This method sends the data from param to the server that has been conencted to.
   * @param data the BinaryOp to send to the server
   */
  public static void run(BinaryOp data) {
    Socket server = Connect(host, port); //setup connection
    ObjectOutputStream serverObj = null; //Out to null
    ObjectInputStream receievedObj = null; //In to null
    //Initiate the outputstream
    try { //Seperate Try catches to prevent pokemon exception handling.
      verbose("initiating object output stream...");
      serverObj = new ObjectOutputStream(server.getOutputStream()); // create output stream
    } catch (IOException e) {
      System.err.println("[ERROR] In creating the ObjectOutput.");
      System.exit(1);
    }
    //Write the object to server
    try {
      verbose("writing object");
      serverObj.writeObject(data);
    } catch (IOException e) {
      System.err.println("[ERROR] Error in writing the object.");
      System.exit(1);
    }
    //Flush the output.
    try {
      verbose("flushing stream");
      serverObj.flush();
    } catch (IOException e) {
      System.err.println("[ERROR] In flushing the object to server.");
      System.exit(1);
    }

    System.out.println("Sent to server "+data.toString()+"...");
    //Receive what is put on the server outputstream.
    try{
      verbose("Listening back");
      receievedObj = new ObjectInputStream(server.getInputStream());
    } catch (IOException e) {
      System.err.println("[ERROR] In creating the objectInPutStream for Sender");
      System.exit(1);
    }
    //Read the object
    try{
      verbose("reading object");
      data = (BinaryOp) receievedObj.readObject();
    } catch (IOException e) {
      System.err.println("[ERROR] In reading the object IOException");
      System.exit(1);
    }catch( ClassNotFoundException cnfe){
      System.err.println("[ERROR] In reading the object ClassNotFoundException");
      System.exit(1);
    }
    //Output the data to stdout
    System.out.println("Server replied " + data.toString());

  }

  /**
   * Valide the ip using regex in java.
   *
   * @param ip the ip as a string coming from args.
   * @return the ip if it passes test, returns "null" if not passed.
   */
  private static String validateIp(String ip) {
    String ipregex =
        "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])"
            + "\\."
            + "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])"
            + "\\."
            + "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])"
            + "\\."
            + "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
    Pattern ipPattern = Pattern.compile(ipregex);
    if (ipPattern.matcher(ip).matches() == true) {
      verbose("Port passes test");
      return ip;
    } else {
      System.err.println("Invalid ip number " + ip + ", setting to 0.");
      return "null";
    }
    }

  /**
   * Validate port number using regex.
   *
   * @param sPort given as a string from args.
   * @return the int port number if passes, else return 0.
   */
  private static int validatePort(String sPort){
    int port = Integer.parseInt(sPort);
    String portregex = "^(6553[0-5]|655[0-2]\\d|65[0-4]\\d\\d|6[0-4]\\d{3}|[1-5]\\d{4}|[2-9]\\d{3}|1[1-9]\\d{2}|10[3-9]\\d|102[4-9])$";
    Pattern portPattern = Pattern.compile(portregex);
    if(portPattern.matcher(sPort).matches() == true){
      verbose("Port passes test");
      return port;
    }else{
      System.err.println("Invalid port number "+sPort+", setting to 0.");
      return 0;
    }
  }

  /**
   * This method will perform the main sending to the server
   * @param args
   */
  public static void main(String[] args) {

    if(args.length == 0){
      System.out.println("No parameters. OpSender {host} {port} {left} {operator} {right}");
      host = validateIp("127.0.0.1");
      port = validatePort("4096");
      System.out.println("Setting to default "+host+":"+port);

    }else{
      host = validateIp(args[0]);
      port = validatePort(args[1]);
      System.out.println("Setting to host:port to "+host+":"+port);
    }
    //Args Method
    BinaryOp dataArgs = new BinaryOp(Double.parseDouble(args[2]),args[3].charAt(0),Double.parseDouble(args[4]));
    run(dataArgs);
//    System.out.println("\nStarting Tests");
    // ADDITION TEST
//    BinaryOp data = new BinaryOp(1.2, '+', 3.4);
//    verbose("Binary Op created with " + data.toString());
//    run(data);
//    // SUBTRACTION TEST
//    BinaryOp data1 = new BinaryOp(5, '-', 2);
//    verbose("Binary Op created with " + data.toString());
//    run(data1);
//    // MULTIPLY TEST
//    BinaryOp data2 = new BinaryOp(2, '*', 30);
//    verbose("Binary Op created with " + data.toString());
//    run(data2);
//    // DIVIDE TEST
//    BinaryOp data3 = new BinaryOp(100, '/', 0);
//    verbose("Binary Op created with " + data.toString());
//    run(data3);BinaryOp data4 = new BinaryOp(100, '/', 5);
//    verbose("Binary Op created with " + data.toString());
//    run(data4);

  }

  public static void verbose(String s) {
    if (verbose) {
      System.out.println(s);
    }
  }
}
