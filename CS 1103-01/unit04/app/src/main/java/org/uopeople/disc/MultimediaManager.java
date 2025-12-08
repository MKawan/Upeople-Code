package org.uopeople.disc;

import java.io.*;

public class MultimediaManager {
    public byte[] loadProductImage(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException("Image not found: " + filePath);

        try (var fis = new FileInputStream(file);
             var bis = new BufferedInputStream(fis);
             var baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    public void saveProcessedImage(byte[] imageData, String destinationPath) throws IOException {
        try (var fos = new FileOutputStream(destinationPath);
             var bos = new BufferedOutputStream(fos)) {
            bos.write(imageData);
        }
    }
}