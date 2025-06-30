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
      
      byte[] messageSizeBytes = input.readNBytes(4);
      byte[] requestApiKeyBytes = input.readNBytes(2);
      byte[] requestApiVersionBytes = input.readNBytes(2);
      byte[] correlationIdBytes = input.readNBytes(4);

      byte[] errorCode = {0,0x23};

      int messageSize = ByteBuffer.wrap(messageSizeBytes).getInt();
      int correlationId= ByteBuffer.wrap(correlationIdBytes).getInt();


      clientSocket.getOutputStream().write(messageSizeBytes);

      var result= ByteBuffer.allocate(4).putInt(correlationId).array();
      
      clientSocket.getOutputStream().write(result);


      clientSocket.getOutputStream().write(errorCode);


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
