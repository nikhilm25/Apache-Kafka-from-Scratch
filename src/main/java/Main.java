
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.nio.ByteBuffer;

public class Main {
  public static void main(String[] args){
    System.err.println("Logs from your program will appear here!");

    ServerSocket serverSocket = null; 
    Socket clientSocket = null;
    int port = 9092;
    try {
      serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept();

      BufferedInputStream input = new BufferedInputStream(clientSocket.getInputStream());

      byte[] messageSizeBytes = input.readNbytes(4);
      byte[] requestApiKeyBytes = input.readNbytes(2);
      byte[] requestApiVersionBytes = input.readNbytes(2);
      byte[] correlationIdBytes = input.readNbytes(4);

      int correlationId = ByteBuffer.wrap(correlationIdBytes).getInt();

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

