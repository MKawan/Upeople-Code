package org.uopeople.disc;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class ProductImageProcessor {
    private final MultimediaManager manager = new MultimediaManager();

    public void processAndSave(String originalPath, String thumbPath) throws Exception {
        byte[] data = manager.loadProductImage(originalPath);
        try (var bais = new ByteArrayInputStream(data)) {
            BufferedImage img = ImageIO.read(bais);
            Image scaled = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            BufferedImage thumb = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = thumb.createGraphics();
            g2d.drawImage(scaled, 0, 0, null);
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 34));
            g2d.drawString("NEW", 15, 45);
            g2d.dispose();

            try (var baos = new ByteArrayOutputStream()) {
                ImageIO.write(thumb, "jpg", baos);
                manager.saveProcessedImage(baos.toByteArray(), thumbPath);
            }
        }
        System.out.println("Thumbnail criado com sucesso: " + thumbPath);
    }
}