package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class student {


    public void displayCourses(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM course_off";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Offering ID\tCourse ID\tInstructor\tSemester\tCGPA Const.");
            while (rs.next()) {
                int offeringId = rs.getInt("off_id");
                int courseId = rs.getInt("course_id");
                String instructor = rs.getString("instructor");
                String semester = rs.getString("semester");
                double cgpaConst = rs.getDouble("cgpa_const");
                System.out.printf("%d\t\t%d\t\t%s\t\t%s\t\t%f%n", offeringId, courseId, instructor, semester, cgpaConst);

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }


    public void registerCourse(Connection conn,int userId) {
        Scanner scanner = new Scanner(System.in);
        try {
            Statement stmt = conn.createStatement();
            System.out.print("Enter the course ID you want to register for: ");
            int courseId = scanner.nextInt();
            scanner.nextLine();
//            System.out.print("Enter the number of credits you want to take for this course: ");
//            double credits = scanner.nextDouble();
//            scanner.nextLine();
            System.out.print("Confirm registration (Y/N): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("Y")) {
                // check if the course exists
                String sql = "SELECT * FROM course_off WHERE course_id=" + courseId;
                ResultSet rs = stmt.executeQuery(sql);
                if (!rs.next()) {
                    System.out.println("Invalid course ID.");
                    rs.close();
                    stmt.close();
                    return;
                }
//			        System.out.println("hi");
                rs.close();
//			        System.out.println("hi");
                // Check if the prerequisites for the course have been completed
                if (!registercheck.checkPrerequisites(conn, userId, courseId)) {
                    System.out.println("You have not completed the prerequisites for this course.");
                    return;
                }
                if (!registercheck.creditsCheck(conn, userId, courseId)) {
                    System.out.println("You don't satisfy credit limit");
                    return;
                }
                sql = "SELECT total_credits FROM course_dis WHERE course_id=" + courseId;
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String credits = rs.getString("total_credits");
//			        System.out.println("hi");
                    rs.close();
//			        System.out.println("hi");
//                 check if the student is already registered for the course
                    sql = "SELECT * FROM student_course_reg WHERE student_id=" + userId + " AND off_id IN " +
                            "(SELECT off_id FROM course_off WHERE course_id=" + courseId + ")";
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        System.out.println("You are already registered for this course.");
                        rs.close();
                        stmt.close();
                        return;
                    }
                    rs.close();
                    // register the student for the course
                    sql = "INSERT INTO student_course_reg (student_id, off_id, credits) " +
                            "VALUES (" + userId + ", (SELECT off_id FROM course_off WHERE course_id=" + courseId + "), " +
                            credits + ")";
                    int count = stmt.executeUpdate(sql);
                    if (count > 0) {
                        System.out.println("Registration successful.");
                    } else {
                        System.out.println("Registration failed.");
                    }
                }else {
                    System.out.println("Invalid course ID.");
                }
                rs.close();
            }  else {
                System.out.println("Registration cancelled.");
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }

    public void deleteRegistration(Connection conn, int userId) {
        Scanner scanner = new Scanner(System.in);
        try {
            Statement stmt = conn.createStatement();
            System.out.print("Enter the course ID you want to unregister from: ");
            int courseId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Confirm unregistration (Y/N): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("Y")) {
                // check if the course exists
                String sql = "SELECT * FROM course_off WHERE course_id=" + courseId;
                ResultSet rs = stmt.executeQuery(sql);
                if (!rs.next()) {
                    System.out.println("Invalid course ID.");
                    rs.close();
                    stmt.close();
                    return;
                }
                rs.close();
                // check if the student is registered for the course
                sql = "SELECT * FROM student_course_reg WHERE student_id=" + userId + " AND off_id IN " +
                        "(SELECT off_id FROM course_off WHERE course_id=" + courseId + ")";
                rs = stmt.executeQuery(sql);
                if (!rs.next()) {
                    System.out.println("You are not registered for this course.");
                    rs.close();
                    stmt.close();
                    return;
                }
                rs.close();
                // unregister the student from the course
                sql = "DELETE FROM student_course_reg WHERE student_id=" + userId + " AND off_id IN " +
                        "(SELECT off_id FROM course_off WHERE course_id=" + courseId + ")";
                int count = stmt.executeUpdate(sql);
                if (count > 0) {
                    System.out.println("Unregistration successful.");
                } else {
                    System.out.println("Unregistration failed.");
                }
            } else {
                System.out.println("Unregistration cancelled.");
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }

    public void displayRegisteredCourses(Connection conn,int userId) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT course_dis.course_id, course_dis.course_name, student_course_reg.credits FROM student_course_reg " +
                    "INNER JOIN course_off ON student_course_reg.off_id = course_off.off_id " +
                    "INNER JOIN course_dis ON course_off.course_id = course_dis.course_id " +
                    "WHERE student_id = " + userId;
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Course ID\tCourse Name\tCredits Allowed");
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                double creditsAllowed = rs.getDouble("credits");
                System.out.printf("%d\t%s\t%.2f%n", courseId, courseName, creditsAllowed);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }
    public static void displayGrades(Connection conn, int studentId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get all the grades obtained by the student
            ps = conn.prepareStatement("SELECT s.off_id, g.grade FROM  student_course_reg  s " +
                    "JOIN grades  g ON s.reg_id = g.reg_id " +
                    "WHERE s.student_id=?");
            ps.setInt(1, studentId);
            rs = ps.executeQuery();

            // Print courseId and grade for each record
            while (rs.next()) {
                int courseId = rs.getInt("off_id");
                String grade = rs.getString("grade");
                System.out.println("courseid|grade");
                System.out.println(courseId + "|" + grade);
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

    public double calculateCGPA(Connection conn, int userId) {
        double totalCredits = 0;
        double totalPoints = 0;
        try {
            String sql = "SELECT course_dis.total_credits, grades.grade FROM student_course_reg " +
                    "INNER JOIN course_off ON student_course_reg.off_id = course_off.off_id " +
                    "INNER JOIN course_dis ON course_off.course_id = course_dis.course_id " +
                    "INNER JOIN grades ON student_course_reg.reg_id = grades.reg_id " +
                    "WHERE student_course_reg.student_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                double credits = rs.getDouble("total_credits");
                String grade = rs.getString("grade");
                double points = convertGradeToPoint(grade);
                totalCredits += credits;
                totalPoints += (credits * points);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        if (totalCredits == 0) {
            return 0.0;
        }
        return totalPoints / totalCredits;
    }

    private double convertGradeToPoint(String grade) {
        switch (grade) {
            case "A":
                return 10.0;
            case "A-":
                return 9.0;
            case "B+":
                return 8.0;
            case "B":
                return 7.0;
            case "B-":
                return 6.0;
            case "C+":
                return 5.0;
            case "C":
                return 4.0;
            case "C-":
                return 3.0;
            case "D+":
                return 2.0;
            case "D":
                return 1.0;
            default:
                return 0.0;
        }
    }

    public void updateContactInfo(Connection conn,int userId, int phoneNumber) {
        // Update the phone number and address for the given user ID
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                    "UPDATE students SET phonenumber=? WHERE user_id=?");

            ps.setInt(1, phoneNumber);
            ps.setInt(2, userId);
            int numRowsUpdated = ps.executeUpdate();

            // Check if the update was successful
            if (numRowsUpdated > 0) {
                System.out.println("Mobile number updated " + userId);
            } else {
                System.out.println("Update error " + userId);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updateAddr(Connection conn,int userId, String address) {
        // Update the phone number and address for the given user ID
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                    "UPDATE students SET address=? WHERE user_id=?");

            ps.setString(1, address);
            ps.setInt(2, userId);
            int numRowsUpdated = ps.executeUpdate();

            // Check if the update was successful
            if (numRowsUpdated > 0) {
                System.out.println("Address updated " + userId);
            } else {
                System.out.println("Update error " + userId);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}

