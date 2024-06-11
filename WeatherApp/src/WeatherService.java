import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import org.json.simple.JSONObject;

public class WeatherApp {
    private static final String API_KEY = "592e2be8c44b42342a1effd4b7b31664";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a location (city, state, country): ");
        String location = scanner.nextLine();

        String weatherData = getWeatherData(location);

        if (weatherData != null) {
            displayWeatherData(weatherData);
        } else {
            System.out.println("Error: Unable to retrieve weather data.");
        }
    }

    public static String getWeatherData(String location) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?q=" + location + "&appid=" + API_KEY + "&units=imperial"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public static void displayWeatherData(String weatherData) {
        JSONObject jsonObject = new JSONObject(weatherData);
        String city = jsonObject.getString("name");
        double temperature = jsonObject.getJSONObject("main").getDouble("temp");
        double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
        String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

        System.out.println("Weather in " + city + ":");
        System.out.println("Temperature: " + temperature + "°F");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Description: " + description);
    }
}