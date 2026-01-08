package com.weatherapp;

import com.google.gson.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WeatherService {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_KEY = "API_HERE"; // chave gratuita
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEE, dd")
            .withZone(ZoneId.systemDefault());

    /* =======================
       BUSCAR CIDADES
       ======================= */
    public static List<WeatherModel.Location> searchCities(String city) throws Exception {
        String url = "https://geocoding-api.open-meteo.com/v1/search?name="
                + URLEncoder.encode(city, StandardCharsets.UTF_8)
                + "&count=5&language=en";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement root = JsonParser.parseString(response.body());

        if (!root.isJsonObject() || !root.getAsJsonObject().has("results")) {
            return Collections.emptyList();
        }

        JsonArray array = root.getAsJsonObject().getAsJsonArray("results");
        List<WeatherModel.Location> list = new ArrayList<>();

        for (var el : array) {
            JsonObject obj = el.getAsJsonObject();
            WeatherModel.Location loc = new WeatherModel.Location();

            loc.name = obj.get("name").getAsString()
                    + (obj.has("admin1") ? ", " + obj.get("admin1").getAsString() : "")
                    + ", " + obj.get("country").getAsString();
            loc.latitude = obj.get("latitude").getAsDouble();
            loc.longitude = obj.get("longitude").getAsDouble();

            list.add(loc);
        }

        return list;
    }

    /* =======================
       CLIMA ATUAL
       ======================= */
    public static WeatherModel.CurrentWeather getCurrent(double lat, double lon) throws Exception {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&lang=en&appid=%s",
                lat, lon, API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();

        WeatherModel.CurrentWeather cw = new WeatherModel.CurrentWeather();
        cw.temperature = root.getAsJsonObject("main").get("temp").getAsDouble();
        cw.humidity = root.getAsJsonObject("main").get("humidity").getAsDouble();
        cw.windSpeed = root.getAsJsonObject("wind").get("speed").getAsDouble();

        JsonObject weather = root.getAsJsonArray("weather").get(0).getAsJsonObject();
        cw.description = weather.get("description").getAsString();
        cw.iconUrl = "https://openweathermap.org/img/wn/" + weather.get("icon").getAsString() + "@2x.png";
        cw.isDay = weather.get("icon").getAsString().endsWith("d");

        return cw;
    }

    /* =======================
       PREVISÃO 5 DIAS
       ======================= */
    public static List<WeatherModel.DailyForecast> getForecast(double lat, double lon) throws Exception {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&units=metric&lang=en&appid=%s",
                lat, lon, API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray list = root.getAsJsonArray("list");

        List<WeatherModel.DailyForecast> forecast = new ArrayList<>();
        Map<String, Boolean> daysAdded = new HashMap<>();

        for (var el : list) {
            JsonObject obj = el.getAsJsonObject();
            String date = fmt.format(Instant.ofEpochSecond(obj.get("dt").getAsLong()));

            // Pegamos apenas 1 ponto por dia
            if (daysAdded.containsKey(date)) continue;

            WeatherModel.DailyForecast df = new WeatherModel.DailyForecast();
            df.date = date;
            df.minTemp = obj.getAsJsonObject("main").get("temp_min").getAsDouble();
            df.maxTemp = obj.getAsJsonObject("main").get("temp_max").getAsDouble();

            JsonObject w = obj.getAsJsonArray("weather").get(0).getAsJsonObject();
            df.iconUrl = "https://openweathermap.org/img/wn/" + w.get("icon").getAsString() + "@2x.png";

            forecast.add(df);
            daysAdded.put(date, true);

            if (forecast.size() == 5) break;
        }

        return forecast;
    }
}
