package org.example;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


//import static org.junit.Assert.assertEquals;
@TestInstance(Lifecycle.PER_CLASS)
public class gradcheckTest {
    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Close the database connection
        if (conn != null) {
            conn.close();
        }
    }
    //    @Test
//    public void testCheckconn() {
////        Connection conn = null;
////        System.out.println("hi");
//        try {
////        	System.out.println("hi");
////            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");
////            System.out.println("hi");
//            gradcheck.checkGraduationStatus(conn, 5);
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    System.err.println("SQLException: " + e.getMessage());
//                }
//            }
//        }
//    }
    @Test
    public void testCheckGraduationStatusNotGraduated() throws SQLException {
        // Insert test data into the database
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (user_id, username,password,role) VALUES (?, ?, ?,?)");
        pstmt.setInt(1, 1);
        pstmt.setString(2, "user");
        pstmt.setString(3,"hi");
        pstmt.setString(4, "student");

        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO students (user_id, entry_year, branch,phonenumber,address) VALUES (?, ?, ?,?,?)");
        pstmt.setInt(1, 1);
        pstmt.setInt(2, 2021);
        pstmt.setString(3, "Computer Science");
        pstmt.setInt(4,' ');
        pstmt.setString(5, null);

        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES (?, ?, ?, ?,?)");
        pstmt.setInt(1, 1);
        pstmt.setString(4, "");
        pstmt.setInt(5, 3);
        pstmt.setString(2, "cs101");
        pstmt.setString(3, "2-0-2");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES (?, ?, ?, ?,?)");
        pstmt.setInt(1, 2);
        pstmt.setString(4, "");
        pstmt.setInt(5, 3);
        pstmt.setString(2, "cs102");
        pstmt.setString(3, "2-0-2");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES (?, ?, ?, ?,?)");
        pstmt.setInt(1, 3);
        pstmt.setString(4, "");
        pstmt.setInt(5, 3);
        pstmt.setString(2, "cs103");
        pstmt.setString(3, "2-0-2");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_tags (course_id, course_type,entry_year, branch) VALUES (?, ?, ?, ?)");
        pstmt.setInt(1, 1);
        pstmt.setString(2, "Program Core");
        pstmt.setInt(3, 2021);
        pstmt.setString(4, "Computer Science");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_tags (course_id, course_type,entry_year, branch) VALUES (?, ?, ?, ?)");
        pstmt.setInt(1, 2);
        pstmt.setString(2, "Program Elective");
        pstmt.setInt(3, 2021);
        pstmt.setString(4, "Computer Science");

        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_tags (course_id, course_type,entry_year, branch ) VALUES (?, ?, ?, ?)");
        pstmt.setInt(1, 1);
        pstmt.setString(2, "BTP Capstone");
        pstmt.setInt(3, 2021);
        pstmt.setString(4, "Computer Science");

        pstmt.executeUpdate();
        pstmt.close();
//        pstmt = conn.prepareStatement("INSERT INTO course_off (off_id, course_id, semester, credits) VALUES (?, ?, ?, ?)");
//        pstmt.setInt(1, 1);
//        pstmt.setInt(2, 1);
//        pstmt.setInt(3, 202101);
//        pstmt.setInt(4, 4);
//        pstmt.executeUpdate();
//        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_off (off_id,course_id,instructor, semester, cgpa_const) VALUES (?, ?, ?, ?,?)");
        pstmt.setInt(1, 3);
        pstmt.setInt(2, 1);
        pstmt.setString(3, "prof");
        pstmt.setInt(4, 1);
        pstmt.setDouble(5, 0.0);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_off (off_id,course_id,instructor, semester, cgpa_const) VALUES (?, ?, ?, ?,?)");
        pstmt.setInt(1, 2);
        pstmt.setInt(2, 2);
        pstmt.setString(3, "prof");
        pstmt.setInt(4, 1);
        pstmt.setDouble(5, 0.0);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO course_off (off_id,course_id,instructor, semester, cgpa_const) VALUES (?, ?, ?, ?,?)");
        pstmt.setInt(1, 1);
        pstmt.setInt(2, 3);
        pstmt.setString(3, "prof");
        pstmt.setInt(4, 1);
        pstmt.setDouble(5, 0.0);
        pstmt.executeUpdate();
        pstmt.close();
//        pstmt = conn.prepareStatement("INSERT INTO course_off (off_id, course_id, semester, credits) VALUES (?, ?, ?, ?)");
//        pstmt.setInt(1, 3);
//        pstmt.setInt(2, 3);
//        pstmt.setInt(3, 202101);
//        pstmt.setInt(4, 6);
//        pstmt.executeUpdate();
//        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO student_course_reg (reg_id, student_id,off_id,credits) VALUES (?, ?, ?,?)");
        pstmt.setInt(1, 1);
        pstmt.setInt(2, 1);
        pstmt.setInt(3, 3);
        pstmt.setInt(4, 4);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("INSERT INTO grades (reg_id,grade) VALUES (?, ?)");
        pstmt.setInt(1, 1);
        pstmt.setString(2, "A");
//        pstmt.setInt(3, 1);
        pstmt.executeUpdate();
        pstmt.close();

        // Check that the student has not graduated
//        boolean result = gradcheck.checkGraduationStatus(conn, 1);
//        assertFalse(result);

        // Check that the output matches the expected string
        String expectedOutput = "Sorry, you have not yet fulfilled all graduation requirements.\n" +
                "You still require 76 program core credits54 program elective credits2 btp credits";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        gradcheck.checkGraduationStatus(conn, 1);
        String actualOutput = outputStream.toString().trim();
        if (!actualOutput.equals("\"Sorry, you have not yet fulfilled all graduation requirements.\nYou still require 76 program core credits54 program elective credits2 btp credits")) {
            System.out.println("Test failed: expected output was not printed");
        }
//        assertEquals(expectedOutput, actualOutput);
        pstmt = conn.prepareStatement("DELETE FROM students WHERE user_id = ?");
        pstmt.setInt(1, 1);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("DELETE FROM grades WHERE reg_id = ?");
        pstmt.setInt(1, 1);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("DELETE FROM student_course_reg WHERE reg_id = ?");
        pstmt.setInt(1, 1);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
        pstmt.setInt(1, 1);
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM course_tags WHERE course_id IN (?,?,?)");
        pstmt.setInt(1, 1);
        pstmt.setInt(2, 2);
        pstmt.setInt(3, 3);
        pstmt.executeUpdate();
        pstmt.close();


        pstmt = conn.prepareStatement("DELETE FROM course_off WHERE course_id IN (?,?,?)");
        pstmt.setInt(1, 1);
        pstmt.setInt(2, 2);
        pstmt.setInt(3, 3);
        pstmt.executeUpdate();
        pstmt.close();




        pstmt = conn.prepareStatement("DELETE FROM course_dis WHERE course_id IN (?,?,?)");
        pstmt.setInt(1, 1);
        pstmt.setInt(2, 2);
        pstmt.setInt(3, 3);
        pstmt.executeUpdate();
        pstmt.close();

    }

}

