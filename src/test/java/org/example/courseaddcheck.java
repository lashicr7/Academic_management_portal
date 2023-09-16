package org.example;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


//import static org.junit.Assert.assertEquals;
@TestInstance(Lifecycle.PER_CLASS)
public class courseaddcheck {
    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");


        String insertCoursedis = "INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES " +
                "(1,'cs101', '3+1', NULL, 3)";
        Statement st = conn.createStatement();
        st.executeUpdate(insertCoursedis);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        System.setIn(System.in);
        Statement st = conn.createStatement();
        // Drop the tables created in setUp()
        String deleteCourseOff = "DELETE FROM course_off WHERE course_id IN (1)";
        st.executeUpdate(deleteCourseOff);
        String deleteCourseDis = "DELETE FROM course_dis WHERE course_name IN ('cs101')";
        st.executeUpdate(deleteCourseDis);

//        conn.createStatement().executeUpdate("DROP TABLE student_courses");
//        conn.createStatement().executeUpdate("DROP TABLE course_dis");

        // Close the database connection
        conn.close();
    }

    @Test
    public void testAddNewCourseWithValidInput() throws SQLException {
        // Set up input for the scanner
        InputStream input = new ByteArrayInputStream("1\n1\n\0.00".getBytes());
        System.setIn(input);

        // Create a new database connection
//        Connection conn = DriverManager.getConnection(DB_URL);

        // Call the method with valid input
        int result = prof.addNewCourse(conn, "user1");

        // Check that the course was added to the course_off table
        assertTrue(conn.createStatement().executeQuery("SELECT * FROM course_off WHERE course_id=1 AND instructor='user1'").next());

        // Check that the method returns 1
        assertEquals(1, result);

        // Reset System.in to its original value
        System.setIn(System.in);
    }

    @Test
    public void testAddNewCourseWithInvalidInput() throws SQLException {
        // Set up input for the scanner
        InputStream input = new ByteArrayInputStream("2\n".getBytes());
        System.setIn(input);

        // Create a new database connection
//        Connection conn = DriverManager.getConnection(DB_URL);
//        System.out.println("hi");
        // Call the method with invalid input
        int result = prof.addNewCourse(conn, "user1");

        // Check that the course was not added to the course_off table
        assertFalse(conn.createStatement().executeQuery("SELECT * FROM course_off WHERE course_id=2 AND instructor='user1'").next());

        // Check that the method returns 0
        assertEquals(0, result);

        // Reset System.in to its original value
        System.setIn(System.in);
    }


}
