package com.weatherapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.util.List;

public class WeatherController {

    @FXML private TextField txtCity;
    @FXML private ListView<WeatherModel.Location> cityList;

    @FXML private Label lblCity;
    @FXML private Label lblTemp;
    @FXML private Label lblDesc;
    @FXML private Label lblHumidity;
    @FXML private Label lblWind;

    @FXML private ImageView weatherIcon;
    @FXML private HBox forecastBox;
    @FXML private VBox mainPane;

    /* =====================
       SEARCH CITIES
       ===================== */
    @FXML
    private void onSearchCities() {
        try {
            List<WeatherModel.Location> cities = WeatherService.searchCities(txtCity.getText());
            cityList.getItems().setAll(cities);

            cityList.setOnMouseClicked(e -> onCitySelected());
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    /* =====================
       LOAD WEATHER
       ===================== */
    private void onCitySelected() {
        var loc = cityList.getSelectionModel().getSelectedItem();
        if (loc == null) return;

        Platform.runLater(() -> {
            try {
                // CURRENT WEATHER
                var current = WeatherService.getCurrent(loc.latitude, loc.longitude);
                lblCity.setText(loc.name);
                lblTemp.setText(String.format("%.1f °C", current.temperature));
                lblDesc.setText(current.description);
                lblHumidity.setText(current.humidity + "%");
                lblWind.setText(current.windSpeed + " km/h");

                // ICON AND BACKGROUND COLORS
                Image icon = new Image(current.iconUrl);
                weatherIcon.setImage(icon);

                // BACKGROUND COLOR BASED ON WEATHER
                String bgColor = switch (current.description.toLowerCase()) {
                    case "rain", "storm" -> "#5C6B73"; // rain/storm = dark gray-blue
                    case "cloudy", "partly cloudy" -> "#A3A3A3"; // clouds
                    case "clear sky", "sunny" -> "#87CEEB"; // blue sky
                    default -> "#ADD8E6"; // default light blue
                };
                mainPane.setStyle("-fx-background-color: " + bgColor);

                // 5-DAY FORECAST
                forecastBox.getChildren().clear();
                List<WeatherModel.DailyForecast> forecast = WeatherService.getForecast(loc.latitude, loc.longitude);

                for (var f : forecast) {
                    VBox dayBox = new VBox(5);
                    dayBox.setStyle("-fx-alignment: center; -fx-padding: 5; -fx-background-color: rgba(255,255,255,0.3); -fx-border-radius: 8; -fx-background-radius: 8;");

                    ImageView iv = new ImageView(new Image(f.iconUrl));
                    iv.setFitWidth(50);
                    iv.setFitHeight(50);

                    Label lDate = new Label(f.date);
                    Label lTemp = new Label(String.format("%.0f° / %.0f°", f.minTemp, f.maxTemp));

                    dayBox.getChildren().addAll(iv, lDate, lTemp);
                    forecastBox.getChildren().add(dayBox);
                }

            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.show();
    }
}
