package uopeople.com.disc;
/**
 *
 * @author mk
 */
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 5000)) {
            System.out.println("Connected to server.");
            
            FileInputStream fis = new FileInputStream("send_file.txt");
            BufferedInputStream bis = new BufferedInputStream(fis);
            OutputStream out = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
            System.out.println("File sent.");
            
            bos.close();
            out.close();
            bis.close();
            fis.close();
        }
    }
}