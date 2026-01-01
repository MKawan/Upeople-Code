package uopeople.com.disc;
/**
 *
 * @author mk
 */
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server waiting for client...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");
            
            InputStream in = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            FileOutputStream fos = new FileOutputStream("received_file.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
            System.out.println("File received.");
            
            bos.close();
            fos.close();
            bis.close();
            in.close();
            socket.close();
        }
    }
}
