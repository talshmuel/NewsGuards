package logic.engine.location.history.management;

import java.awt.geom.Point2D;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationHistoryManager {
    double radiusMetersToLookingForUsers = 50;//todo:maybe change

    public void saveUserLocation(int userID, Date dateTime, double lat, double lon) {
        //שמירת הלוקיישן בדאטהבייס
        /*ככה הצאט הציע:
        * public void saveUserLocation(Connection conn, long userId, double lat, double lon, Timestamp timestamp) throws Exception {
            String sql = "INSERT INTO user_locations (user_id, location, timestamp) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);

            PGobject location = new PGobject();
            location.setType("geometry");
            location.setValue(String.format("POINT(%f %f)", lon, lat));
            ps.setObject(2, location);

            ps.setTimestamp(3, timestamp);
            ps.executeUpdate();
            }
            }
        * */
    }

    public ArrayList<Integer> findUsersInRadius(Date dateTime, double lat, double lon){
        /*מציאת רשימת היוזרים ברדיוס כלשהו בזמן נתון
        *ככה הצאט הציע:
        * public List<Long> findUsersInRadius(Connection conn, double lat, double lon, Timestamp timestamp) throws Exception {
    List<Long> userIds = new ArrayList<>();

    String sql = "SELECT user_id FROM user_locations " +
                 "WHERE ST_DWithin(location, ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography, ?) " +
                 "AND timestamp BETWEEN ? - interval '1 day' AND ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setDouble(1, lon);
        ps.setDouble(2, lat);
        ps.setDouble(3, radiusMetersToLookingForUsers);
        ps.setTimestamp(4, timestamp);
        ps.setTimestamp(5, timestamp);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userIds.add(rs.getLong("user_id"));
            }
        }
    }

    return userIds;
}
        * */

        //todo delete this (it is just a try)
        ArrayList<Integer> res = new ArrayList<>();
        res.add(1);
        return res;
    }


}
