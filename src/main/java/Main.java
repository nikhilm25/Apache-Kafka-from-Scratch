import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.nio.ByteBuffer;

import java.io.FileInputStream;




public class Main {
  public static void main(String[] args){
    System.err.println("Logs from your program will appear here!");

    System.err.println("Java version: " + System.getProperty("java.version"));
    
     
    ServerSocket serverSocket = null; 
    Socket clientSocket = null;
    int port = 9092;
    try {
      serverSocket = new ServerSocket(port);
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      clientSocket = serverSocket.accept();


      BufferedInputStream input = new BufferedInputStream(clientSocket.getInputStream());
      
      byte[] messageSizeBytes = input.readNbytes(4);
      byte[] requestApiKeyBytes = input.readNbytes(2);
      byte[] requestApiVersionBytes = input.readNbytes(2);
      byte[] correlationIdBytes = input.readNbytes(4);

      //int messageSize = ByteBuffer.wrap(messageSizeBytes).getInt();
      //int requestApiKey = ByteBuffer.wrap(requestApiKeyBytes).getInt();
      //int requestApiVersion= ByteBuffer.wrap(requestApiVersionBytes).getInt();
      int correlationId= ByteBuffer.wrap(correlationIdBytes).getInt();




      clientSocket.getOutputStream().write(messageSizeBytes);
      
      clientSocket.getOutputStream().write(ByteBuffer.allocate(4).putInt(correlationId).array());
      




    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } finally {
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }
}
