package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TranscriptGenerator {
    public void generateTranscript(Connection conn, int studentId) throws IOException {
        try {
            // Get student info
            String sql = "SELECT u.username, s.entry_year, s.branch FROM users u " +
                    "JOIN students s ON u.user_id=s.user_id " +
                    "WHERE u.user_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                int entryYear = rs.getInt("entry_year");
                String branch = rs.getString("branch");
                String fileName = "C:\\Users\\" + System.getProperty("user.name") + "\\OneDrive\\" +"\\Desktop\\" + username + "_report.txt";
                File file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }

                try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                    writer.println("Name: " + username);
                    writer.println("Entry Year: " + entryYear);
                    writer.println("Branch: " + branch);

                    // Get course registrations and grades
                    sql = "SELECT cd.course_name, cr.semester, cr.cgpa_const, cr.off_id, cr.instructor, g.grade " +
                            "FROM student_course_reg scr " +
                            "JOIN course_off cr ON scr.off_id=cr.off_id " +
                            "JOIN course_dis cd ON cr.course_id=cd.course_id " +
                            "LEFT JOIN grades g ON scr.reg_id=g.reg_id " +
                            "WHERE scr.student_id=?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, studentId);
                    rs = stmt.executeQuery();
                    double totalCredits = 0;
                    double totalGradePoints = 0;
                    while (rs.next()) {
                        String courseName = rs.getString("course_name");
                        int semester = rs.getInt("semester");
                        double cgpaConst = rs.getDouble("cgpa_const");
                        int offId = rs.getInt("off_id");
                        String instructor = rs.getString("instructor");
                        String grade = rs.getString("grade");

                        writer.println(courseName);
                        writer.println("Semester: " + semester);
                        writer.println("Instructor: " + instructor);
                        if (grade == null) {
                            writer.println("Grade: N/A");
                        } else {
                            writer.println("Grade: " + grade);
                            totalCredits += getCourseCredits(conn, offId);
                            totalGradePoints += calculateGradePoints(grade) * getCourseCredits(conn, offId);
                        }
                        writer.println("CGPA Constant: " + cgpaConst);
                        writer.println();
                    }
                    rs.close();
                    stmt.close();

                    // Calculate and print CGPA
                    if (totalCredits > 0) {
                        double cgpa = totalGradePoints / totalCredits;
                        writer.println("CGPA: " + cgpa);
                    } else {
                        writer.println("CGPA: N/A");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int getCourseCredits(Connection conn, int offId) throws SQLException {
        String sql = "SELECT cd.total_credits " +
                "FROM course_off cr " +
                "JOIN course_dis cd ON cr.course_id=cd.course_id " +
                "WHERE cr.off_id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, offId);
        ResultSet rs = stmt.executeQuery();
        int credits = 0;
        if (rs.next()) {
            credits = rs.getInt("total_credits");
        }
        rs.close();
        stmt.close();
        return credits;
    }

    private int calculateGradePoints(String grade) {
        switch (grade) {
            case "A":
                return 10;
            case "A-":
                return 9;
            case "B+":
                return 8;
            case "B":
                return 7;
            case "B-":
                return 6;
            case "C":
                return 5;
            case "D":
                return 4;
            case "F":
                return 0;
            default:
                return 0;
        }}
}

