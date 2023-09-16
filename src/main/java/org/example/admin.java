package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class admin {
    //    private final String username = "Staff Dean's office";
//    private final Connection conn;
//
//    public admin(Connection conn) {
//        this.connection = conn;
//    }
    public static int addNewCourse(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get the course details from the user
            System.out.print("Enter course name: ");
            String courseName = scanner.nextLine();

            System.out.print("Enter credit structure: ");
            String creditStructure = scanner.nextLine();

            System.out.print("Enter prerequisites: ");
            String prerequisites = scanner.nextLine();

            System.out.print("Enter total credits: ");
            int totalCredits = scanner.nextInt();
//	        scanner.nextLine();
            PreparedStatement stmt = conn.prepareStatement("SELECT course_id FROM course_dis WHERE course_name = ?");
            stmt.setString(1, courseName);
            rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Course already exists in catalogue");
                return 0;
            }

            // Insert the new course into the course_dis table
            ps = conn.prepareStatement(
                    "INSERT INTO course_dis (course_name, credit_structure, prerequisites, total_credits) " +
                            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, courseName);
            ps.setString(2, creditStructure);
            ps.setString(3, prerequisites);
            ps.setInt(4, totalCredits);


            int result = ps.executeUpdate();

            // Check if the course was added successfully
            if (result > 0) {
                System.out.println("New course added successfully!");
//	            return 1;
            } else {
                System.out.println("Failed to add new course.");
                return 2;
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return 3;
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
                return 3;
            }
//	        scanner.close();
        }
        return 1;
    }
    public static void deleteCourse(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get the course name from the user
            System.out.print("Enter course Id to delete: ");
            int courseId = scanner.nextInt();

            // Delete the course from the course_dis table
            ps = conn.prepareStatement("DELETE FROM course_dis WHERE course_id = ?");
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
//	      scanner.close();
        }
    }

    public void displayCourses(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM course_dis";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Course ID\tCourse Name\tCredit Structure\tPrerequisites");
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                String creditStructure = rs.getString("credit_structure");
                String prerequisites = rs.getString("prerequisites");
                System.out.printf("%d\t%s\t%s\t%s%n", courseId, courseName, creditStructure, prerequisites);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }}
    public void editCourseCatalog(Connection conn,int course_id, String course_name, String credit_structure,String prerequisites) throws SQLException {
//        if (!username.equals("Staff Dean's office")) {
//            System.out.println("Error: Access denied. Only staff from Dean's office can edit the course catalog.");
//            return;
//        }

        String sql =  "UPDATE course_dis SET course_name=?, credit_structure=?, prerequisites=? WHERE course_id=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, course_name);
        statement.setString(2, credit_structure);
        statement.setString(3, prerequisites);
        statement.setInt(4, course_id);

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Course catalog updated successfully.");
        } else {
            System.out.println("Error: Failed to update course catalog.");
        }
    }
    public void addNewCurriculum(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the semester for the new curriculum: ");
        int semester = scanner.nextInt();
        System.out.print("Enter the status for the new curriculum (0-4): ");
        int status = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO curriculum (semester, status) VALUES (?, ?)");
        stmt.setInt(1, semester);
        stmt.setInt(2, status);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("New curriculum added successfully.");
        } else {
            System.out.println("Failed to add new curriculum.");
        }
    }

    public void modifyCurriculumStatus(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the curriculum to modify: ");
        int curriculumId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new status for the curriculum (0-4): ");
        int newStatus = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE curriculum SET status = ? WHERE id = ?");
        stmt.setInt(1, newStatus);
        stmt.setInt(2, curriculumId);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Curriculum status updated successfully.");
        } else {
            System.out.println("Failed to update curriculum status.");
        }
    }
}


