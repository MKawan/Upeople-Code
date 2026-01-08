package com.weatherapp;

import java.util.List;

public class WeatherModel {

    public static class Location {
        public String name;
        public double latitude;
        public double longitude;

        @Override
        public String toString() {
            return name;
        }
    }

    public static class CurrentWeather {
        public double temperature;
        public double humidity;
        public double windSpeed;
        public String description;
        public String iconUrl;
        public boolean isDay;
    }

    public static class DailyForecast {
        public String date;
        public double minTemp;
        public double maxTemp;
        public String iconUrl;
		public String description;
    }
}
