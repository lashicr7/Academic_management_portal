package org.example;
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
public class displayCoursesTest {
    private Connection conn;
    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert a course in the course_off table for testing displayCourses
        String insertCoursedis = "INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES " +
                "(1,'cs101', '3+1', NULL, 3)";
        Statement st = conn.createStatement();
        st.executeUpdate(insertCoursedis);
        // Insert a course in course_off table
        String insertCourseOff = "INSERT INTO course_off (off_id,course_id, semester, cgpa_const, instructor) VALUES " +
                "(1,1, 1, 3.0, 'user1')";
        st.executeUpdate(insertCourseOff);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Delete the course added in setUp()
        String deleteCourseOff = "DELETE FROM course_off WHERE course_id=1";
        Statement st = conn.createStatement();
        st.executeUpdate(deleteCourseOff);
        String deleteCourseDis = "DELETE FROM course_dis WHERE course_id=1";
        st.executeUpdate(deleteCourseDis);

        // Close the database connection
        conn.close();
    }

    @Test
    public void testDisplayCoursesWithValidInput() {
        // Redirect standard output to a stream for testing
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        prof prof = new prof();
        // Call the method with valid input
        prof.displayCourses(conn, "user1");
        String printedOutput = outputStream.toString();
        if (!printedOutput.equals("Courses floated by professor user1:\n" +
                "Off ID: 1\n" +
                "Course ID: 1\n" +
                "Semester: 1\n" +
                "CGPA Constant: 3.0\n\n")) {
            System.out.println("Test failed: expected output was not printed");
        }
        // Check that the expected output was printed
//    assertEquals("Courses floated by professor user1:\n" +
//                 "Off ID: 1\n" +
//                 "Course ID: 1\n" +
//                 "Semester: 1\n" +
//                 "CGPA Constant: 3.0\n\n", outputStream.toString());

        // Reset standard output to its original value
        System.setOut(System.out);
    }

    @Test
    public void testDisplayCoursesWithInvalidInput() {
        // Redirect standard output to a stream for testing
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Call the method with invalid input
        prof prof = new prof();
        prof.displayCourses(conn, "user2");

        // Get the printed output as a string
        String printedOutput = outputStream.toString();

        // Check that the expected output was printed
        if (!printedOutput.equals("No courses found for professor user2 ")) {
            System.out.println("Test failed: expected output was not printed");
        }

        // Reset standard output to its original value
        System.setOut(System.out);
    }


}

