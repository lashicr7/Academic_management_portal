package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.BufferedReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class prof {

    private static final String COURSE_DIS_TABLE = "course_dis";
    private static final String COURSE_OFF_TABLE = "course_off";
    private static final String STUDENT_COURSE_REG_TABLE = "student_course_reg";
    private static final String GRADES_TABLE = "grades";
    private static final String COURSE_TAGS= "course_tags";
    public static int addNewCourse( Connection conn, String username) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Display courses from course_dis table
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT course_id, course_name FROM " + COURSE_DIS_TABLE);

            // Print the courses available in course_dis table
            System.out.println("Courses available in course_dis table:");
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                System.out.println(courseId + ". " + courseName);
            }

            // Get the course_id from the user
            System.out.print("Enter course ID: ");
            int courseId = scanner.nextInt();

            // Check if the course exists in course_dis table
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT course_id FROM " + COURSE_DIS_TABLE + " WHERE course_id=" + courseId);

            if (!rs.next()) {
                System.out.println("Course not found in course_dis table.");
                return 0;
            }

//            String courseType = rs.getString("course_type");

//            System.out.print("Enter semester: ");
//            String semester = scanner.next();
//
            int semester = CurriculumManager.getLatestCurriculumsem(conn);

            System.out.print("Enter CGPA constant: ");
            double cgpaConst = scanner.nextDouble();

            // Insert the new course into the course_off table
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO " + COURSE_OFF_TABLE + " (course_id, instructor, semester, cgpa_const) VALUES (?, ?, ?, ?)");
            ps.setInt(1, courseId);
            ps.setString(2, username);
            ps.setInt(3, semester);
            ps.setDouble(4, cgpaConst);
            int courseOffResult = ps.executeUpdate();

            // Check if the course was added successfully
            if (courseOffResult > 0) {
                System.out.println("New course added successfully!");

            } else {
                System.out.println("Failed to add new course.");
                return 2;
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        } finally {
            // Close the database resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
//            scanner.close();
        }
        return 1;
    }
    public static void deleteCourse(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Display courses from course_off table
            ps = conn.prepareStatement("SELECT course_id, semester, cgpa_const FROM " + COURSE_OFF_TABLE);
            rs = ps.executeQuery();

            // Print the courses available in course_off table
            System.out.println("Courses available in course_off table:");
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String semester = rs.getString("semester");
                double cgpaConst = rs.getDouble("cgpa_const");
                System.out.println(courseId + ". Semester: " + semester + ", CGPA Constant: " + cgpaConst);
            }

            // Get the course_id from the user
            System.out.print("Enter course ID: ");
            int courseId = scanner.nextInt();

            // Check if the course exists in course_off table
            ps = conn.prepareStatement("SELECT course_id FROM " + COURSE_OFF_TABLE + " WHERE course_id=?");
            ps.setInt(1, courseId);
            rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Course not found in course_off table.");
                return;
            }

            // Delete the course from the course_off table
            ps = conn.prepareStatement("DELETE FROM " + COURSE_OFF_TABLE + " WHERE course_id=?");
            ps.setInt(1, courseId);
            int result = ps.executeUpdate();

            // Check if the course was deleted successfully
            if (result > 0) {
                System.out.println("Course deleted successfully!");
            } else {
                System.out.println("Failed to delete course.");
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
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

    public void displayCourses(Connection conn, String username) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM course_off WHERE instructor='" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                System.out.println("No courses found for professor " + username);
            } else {
                System.out.println("Courses floated by professor " + username + ":");
                do {
                    int off_id = rs.getInt("off_id");
                    int course_id = rs.getInt("course_id");
                    String semester = rs.getString("semester");
                    double cgpa_const = rs.getDouble("cgpa_const");

                    System.out.println("Off ID: " + off_id);
                    System.out.println("Course ID: " + course_id);
                    System.out.println("Semester: " + semester);
                    System.out.println("CGPA Constant: " + cgpa_const);
                    System.out.println();
                } while (rs.next());
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }

    public static void updateGradesFromCsv(Connection conn, String fileName, int courseId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        BufferedReader br = null;

        try {
            // Get the course_id from the instructor

            // Check if the course exists
            ps = conn.prepareStatement("SELECT COUNT(*) FROM " + COURSE_OFF_TABLE + " WHERE off_id=?");
            ps.setInt(1, courseId);
            rs = ps.executeQuery();
            rs.next();
            int courseExists = rs.getInt(1);

            if (courseExists == 0) {
                System.out.println("Course not found!");
                return;
            }

            // Read grades from CSV file
            br = new BufferedReader(new FileReader(fileName));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int regId = Integer.parseInt(data[0]);
                String grade = data[1];

                // Insert the grade into the grades table
                ps = conn.prepareStatement("INSERT INTO " + GRADES_TABLE + " (reg_id, grade) VALUES (?, ?)");
                ps.setInt(1, regId);
                ps.setString(2, grade);
                int result = ps.executeUpdate();

                // Check if the grade was assigned successfully
                if (result > 0) {
                    System.out.println("Grade assigned successfully for reg_id " + regId);
                } else {
                    System.out.println("Failed to assign grade for reg_id " + regId);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {
            // Close the database resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
            }
        }
    }

}

