package logic.engine.location.history.management;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NominatimExample {

    public static String getCountry(Point2D.Double location) throws Exception {
        double latitude = location.getY();
        double longitude = location.getX();
        String urlString = String.format(
                "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=10&addressdetails=1",
                latitude, longitude);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "MyGeocodingApp/1.0 (myemail@example.com)"); // Replace with your application name and contact info

        int responseCode = conn.getResponseCode();
        if (responseCode == 403) {
            throw new IOException("Server returned HTTP response code: 403 for URL: " + urlString);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(content.toString());
        JsonNode addressNode = rootNode.path("address");
        if (addressNode.has("country")) {
            return addressNode.path("country").asText();
        }

        return null;
    }
}
