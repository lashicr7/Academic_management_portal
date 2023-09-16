package org.example;

//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
public class CourseDeletionTest {
    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");
        String insertCoursedis = "INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES " +
                "(1,'cs101', '3+1', NULL, 3)";
        Statement st = conn.createStatement();
        st.executeUpdate(insertCoursedis);
        // Insert a course in course_off table
        String insertCourseOff = "INSERT INTO course_off (course_id, semester, cgpa_const, instructor) VALUES " +
                "(1, 1, 3.0, 'Instructor A')";
        st.executeUpdate(insertCourseOff);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        Statement st = conn.createStatement();

        // Delete the course from the course_off table
        String deleteCourseOff = "DELETE FROM course_off WHERE course_id IN (1)";
        st.executeUpdate(deleteCourseOff);
        String deleteCourseDis = "DELETE FROM course_dis WHERE course_name IN ('cs101')";
        st.executeUpdate(deleteCourseDis);

        // Close the database connection
        conn.close();
    }

    @Test
    public void testDeleteCourseWithValidInput() throws SQLException {
        // Set up input for the scanner
        InputStream input = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(input);

        // Call the method with valid input
        prof.deleteCourse(conn);

        // Check that the course was deleted from the course_off table
        assertFalse(conn.createStatement().executeQuery("SELECT * FROM course_off WHERE course_id=1").next());

        // Reset System.in to its original value
        System.setIn(System.in);
    }

    @Test
    public void testDeleteCourseWithInvalidInput() throws SQLException {
        // Set up input for the scanner
        InputStream input = new ByteArrayInputStream("2\n".getBytes());
        System.setIn(input);

        // Call the method with invalid input
        prof.deleteCourse(conn);

        // Check that the course was not deleted from the course_off table
        assertTrue(conn.createStatement().executeQuery("SELECT * FROM course_off WHERE course_id=1").next());

        // Reset System.in to its original value
        System.setIn(System.in);
    }
}

