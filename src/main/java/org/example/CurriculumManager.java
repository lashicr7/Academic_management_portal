package org.example;

import java.sql.*;

public class CurriculumManager {
    public static int getLatestCurriculumsem(Connection conn) throws SQLException {
        int latestSemester = 0;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT semester FROM curriculum ORDER BY id DESC LIMIT 1");
        if (rs.next()) {
            latestSemester = rs.getInt("semester");
        }
        return latestSemester;
    }

    public static int getLatestCurriculumstatus(Connection conn) throws SQLException {
        int latestStatus = -1;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT status FROM curriculum ORDER BY id DESC LIMIT 1");
        if (rs.next()) {
            latestStatus = rs.getInt("status");
        }
        return latestStatus;
    }
}
