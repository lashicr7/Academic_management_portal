package org.example;

import java.sql.*;

public class registercheck {
    private static final String COURSE_DIS_TABLE = "course_dis";
    private static final String STUDENT_COURSE_REG_TABLE = "student_course_reg";
    private static final String GRADES_TABLE = "grades";
    private static final String COURSE_OFF_TABLE = "course_off";

    public static boolean checkPrerequisites(Connection conn, int studentId, int courseId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get the prerequisites for the course
            ps = conn.prepareStatement("SELECT prerequisites FROM " + COURSE_DIS_TABLE + " WHERE course_id=?");
            ps.setInt(1, courseId);
            rs = ps.executeQuery();
            rs.next();
            String prerequisites = rs.getString("prerequisites");

            if (prerequisites == null || prerequisites.isEmpty()) {
                // No prerequisites for the course
                return true;
            }

            // Check if the prerequisites have been completed by the student
            String[] prereqList = prerequisites.split("\\s*,\\s*");
            for (String prereq : prereqList) {
                int prereqId = getCourseIdByName(conn, prereq);
                if (prereqId == -1) {
                    // Prerequisite course not found
                    return false;
                }
                // Get the registration id for the prerequisite course
                ps = conn.prepareStatement(
                        "SELECT reg_id FROM student_course_reg scr " +
                                "JOIN course_off co ON scr.off_id = co.off_id " +
                                "WHERE scr.student_id = ? AND co.course_id = ?");
                ps.setInt(1, studentId);
                ps.setInt(2, prereqId);
                rs = ps.executeQuery();

                if (!rs.next()) {
                    // Prerequisite course not found
                    return false;
                }

                int regId = rs.getInt("reg_id");

                // Check if a grade has been assigned for the prerequisite course
                ps = conn.prepareStatement("SELECT COUNT(*) FROM " + GRADES_TABLE + " WHERE reg_id=?");
                ps.setInt(1, regId);
                rs = ps.executeQuery();
                rs.next();
                int gradeAssigned = rs.getInt(1);

                if (gradeAssigned == 0) {
                    // Grade not assigned for prerequisite course
                    return false;
                }
            }

            // All prerequisites completed
            return true;

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        } finally {
            // Close the database resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }
    }
    public static int getCourseIdByName(Connection conn, String courseName) throws SQLException {
        int courseId = -1;
        String sql = "SELECT course_id FROM course_dis WHERE course_name = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, courseName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            courseId = rs.getInt("course_id");
        }
        return courseId;
    }
    public static boolean creditsCheck(Connection conn, int studentId, int courseId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get the semester for the given course
//             = conn.prepareStatement(
//                "SELECT semester FROM " + COURSE_OFF_TABLE + " WHERE off_id=?");
//            ps.setInt(1, courseId);
//            rs = ps.executeQuery();
//            if (rs.next()) { // add this line to move the cursor to the first row
//                String semester = rs.getString("semester");
            int semester = CurriculumManager.getLatestCurriculumsem(conn);

            // Get the total number of credits registered for the semester
            ps = conn.prepareStatement(
                    "SELECT total_credits FROM " + COURSE_DIS_TABLE + " WHERE course_id=?");
            ps.setInt(1, courseId);
            rs = ps.executeQuery();
            int courseCredits=0;
            if (rs.next()) {
                courseCredits = rs.getInt("total_credits");
            }
            ps = conn.prepareStatement(
                    "SELECT SUM(credits) FROM " + STUDENT_COURSE_REG_TABLE + " WHERE student_id=? AND off_id IN (SELECT off_id FROM " + COURSE_OFF_TABLE + " WHERE semester=?)");
            ps.setInt(1, studentId);
            ps.setInt(2, semester);
            rs = ps.executeQuery();
            if (rs.next()) { // add this line to move the cursor to the first row
                int totalCredits = rs.getInt(1);
                totalCredits+=courseCredits;
//                    System.out.println(totalCredits);
//                    System.out.println(totalCredits+courseCredits);
                if (totalCredits > 25) {
                    System.out.println("Total credits for " + semester + " exceed 25!");
                    return false;
                }

                // Get the number of courses completed for the last two semesters
                if(semester>2) {
                    ps = conn.prepareStatement(
                            "SELECT SUM(student_course_reg.credits) FROM " + GRADES_TABLE + " JOIN student_course_reg ON grades.reg_id=student_course_reg.reg_id JOIN " + COURSE_OFF_TABLE + " ON student_course_reg.off_id=" + COURSE_OFF_TABLE + ".off_id WHERE grades.grade IS NOT NULL AND grades.grade <= 'D' AND student_course_reg.student_id=? AND " + COURSE_OFF_TABLE + ".semester IN (SELECT DISTINCT semester FROM " + COURSE_OFF_TABLE + " WHERE semester < ? ORDER BY semester DESC LIMIT 2)");
                    ps.setInt(1, studentId);
                    ps.setInt(2, semester);
                    rs = ps.executeQuery();
                    if (rs.next()) { // add this line to move the cursor to the first row
                        int coursesCompleted = rs.getInt(1);

                        // Get the new credit limit based on the number of courses completed
                        int creditLimit = (int) Math.round(coursesCompleted * 1.25);

                        if (totalCredits > creditLimit) {
                            System.out.println("Total credits for " + semester + " exceed the credit limit of " + creditLimit + " based on completed courses!");
                            return false;
                        }
                    }
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        } finally {
            // Close the database resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }
    }
}

