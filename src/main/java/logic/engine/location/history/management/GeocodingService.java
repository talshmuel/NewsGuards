package logic.engine.location.history.management;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.awt.geom.Point2D;

public class GeocodingService {

    private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "AIzaSyA6cK5KSpW5U_Z3JdmXRBURXC5Cxj8jJBM"; // Replace with your actual API key

    private OkHttpClient client;
    private ObjectMapper objectMapper;

    public GeocodingService() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String getCountry(Point2D.Double location) throws IOException {
        String url = String.format("%s?latlng=%s,%s&key=%s",
                GEOCODING_API_URL, location.getY(), location.getX(), API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JsonNode root = objectMapper.readTree(response.body().string());
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
}
