package logic.engine.location.history.management;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.geom.Point2D;
import java.io.IOException;

public class OpenCageGeocodingService {

    private static final String GEOCODING_API_URL = "https://api.opencagedata.com/geocode/v1/json";
    private static final String API_KEY = "fa9ddb5e1b4844e3bbbfcf04e230f4d8"; // Replace with your actual API key

    private OkHttpClient client;
    private ObjectMapper objectMapper;

    public OpenCageGeocodingService() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String getCountry(Point2D.Double location) throws IOException {
        String url = String.format("%s?q=%s,%s&key=%s",
                GEOCODING_API_URL, location.getY(), location.getX(), API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            System.out.println("Response Body: " + responseBody);



            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode results = root.path("results");

            for (JsonNode result : results) {
                JsonNode addressComponents = result.path("address_components");
                for (JsonNode component : addressComponents) {
                    JsonNode types = component.path("types");
                    for (JsonNode type : types) {
                        if (type.asText().equals("country")) {
                            return component.path("long_name").asText();
                        }
                    }
                }
            }
        }

        return null;
    }

    public String getCoordinates(String address) throws IOException {
        String url = String.format("%s?q=%s&key=%s",
                GEOCODING_API_URL, address, API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JsonNode root = objectMapper.readTree(response.body().string());
            JsonNode results = root.path("results");

            if (results.size() > 0) {
                JsonNode geometry = results.get(0).path("geometry");
                double lat = geometry.get("lat").asDouble();
                double lng = geometry.get("lng").asDouble();
                return String.format("Latitude: %f, Longitude: %f", lat, lng);
            }
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        OpenCageGeocodingService geocodingService = new OpenCageGeocodingService();

        // Example usage: Reverse geocoding
        Point2D.Double location = new Point2D.Double(40.730610, -73.935242);
        String country = geocodingService.getCountry(location);
        System.out.println("Country: " + country);

        // Example usage: Forward geocoding
        String coordinates = geocodingService.getCoordinates("Empire State Building, New York, USA");
        System.out.println("Coordinates: " + coordinates);
    }
}

