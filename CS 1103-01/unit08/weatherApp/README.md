# Weather Information App

## How to Run
1. Set up JavaFX project (use IntelliJ/Eclipse with JavaFX SDK).
2. Add Gson library.
3. Run Main.java.

## Features
- Enter city name and click "Get Weather".
- Toggle units with the button.
- Displays current weather with icon, 5-day forecast, history.
- Dynamic background (day/night).
- Error handling for invalid cities/network.

## Implementation Details
- API: Open-Meteo (free, no key).
- Geocoding + weather/forecast in separate calls.
- Icons mapped to OpenWeatherMap style for visuals.
- Threading for async API calls.
- Well-commented code.