package data.transfer.object.location;

import java.util.Date;

public class LocationDTO {
    private final int userID;
    private final Date dateTime;
    private final double latitude;
    private final double longitude;

    public LocationDTO(int userID, Date dateTime, double latitude, double longitude) {
        this.userID = userID;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public int getUserID() {
        return userID;
    }
}