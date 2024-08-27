package data.transfer.object.location;

import java.util.Date;

public class LocationDTO {
    private final int userID;
    private final double latitude;
    private final double longitude;

    public LocationDTO(int userID, double latitude, double longitude) {
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getUserID() {
        return userID;
    }
}