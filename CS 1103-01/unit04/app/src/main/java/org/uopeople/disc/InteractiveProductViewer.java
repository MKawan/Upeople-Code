package org.uopeople.disc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.InputStream;

public class InteractiveProductViewer extends Application {

    private static final int TOTAL_FRAMES = 18;     // 18 imagens = 360° perfeito
    private final Image[] frames = new Image[TOTAL_FRAMES];
    private int currentFrame = 0;
    private double startX = 0;

    @Override
    public void start(Stage stage) {
        loadImages();

        Canvas canvas = new Canvas(800, 600);  // Tela maior fica mais bonito com 18 frames
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(e -> startX = e.getX());
        canvas.setOnMouseDragged(e -> {
            double delta = e.getX() - startX;
            if (Math.abs(delta) >= 10) {  // evita micro-movimentos
                int steps = (int) (delta / 30);  // sensibilidade ajustada
                currentFrame = (currentFrame + steps % TOTAL_FRAMES + TOTAL_FRAMES) % TOTAL_FRAMES;
                startX = e.getX();
                draw(gc);
            }
        });

        canvas.setOnMouseClicked(e -> {
            currentFrame = 0;
            draw(gc);
        });

        draw(gc);

        stage.setTitle("E-Commerce 360° Viewer - 18 Frames (Java 21 + Gradle)");
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.setResizable(false);
        stage.show();
    }

    private void loadImages() {
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String path = "/images/" + i + ".jpg";
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    frames[i] = new Image(is, 600, 600, true, true); // redimensiona suavemente
                } else {
                    frames[i] = createPlaceholder(i);
                    System.err.println("Imagem não encontrada: " + path);
                }
            } catch (Exception e) {
                frames[i] = createPlaceholder(i);
            }
        }
    }

    private void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 600);

        Image img = frames[currentFrame];
        if (img != null) {
            gc.drawImage(img, 100, 50, 600, 500);  // imagem maior e centralizada
        }

        gc.setFill(Color.CYAN);
        gc.setFont(new Font("Arial Bold", 18));
        gc.fillText("360° Product Viewer - Arraste para girar", 180, 580);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 16));
        gc.fillText("Frame: " + currentFrame + " / " + (TOTAL_FRAMES - 1), 20, 30);
    }

    private Image createPlaceholder(int index) {
        Canvas c = new Canvas(600, 500);
        GraphicsContext g = c.getGraphicsContext2D();
        g.setFill(Color.DARKRED);
        g.fillRect(0, 0, 600, 500);
        g.setFill(Color.WHITE);
        g.setFont(new Font("Arial", 36));
        g.fillText("Imagem " + index + ".jpg", 120, 240);
        g.fillText("não encontrada", 140, 300);
        return c.snapshot(null, null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}