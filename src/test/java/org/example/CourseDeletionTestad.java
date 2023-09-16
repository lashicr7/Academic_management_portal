package org.example;

//import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
public class CourseDeletionTestad {
    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert a course into the course_dis table for testing purposes
        PreparedStatement ps = conn.prepareStatement("INSERT INTO course_dis (course_id, course_name, credit_structure, prerequisites, total_credits) VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, 1);
        ps.setString(2, "Test Course");
        ps.setString(3, "3+0");
        ps.setString(4, null);
        ps.setInt(5, 3);
        ps.executeUpdate();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Delete the course inserted in setUp()
        PreparedStatement ps = conn.prepareStatement("DELETE FROM course_dis WHERE course_id = ?");
        ps.setInt(1, 1);
        ps.executeUpdate();

        // Close the database connection
        conn.close();
    }

    @Test
    public void testDeleteCourseSuccess() throws SQLException {
        // Set up input for the scanner
        InputStream input = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(input);

        // Call the deleteCourse() method
        admin.deleteCourse(conn);

        // Check that the course was deleted from the course_dis table
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM course_dis WHERE course_id = ?");
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        assertFalse(rs.next());

        // Reset System.in to its original value
        System.setIn(System.in);
    }

    @Test
    public void testDeleteCourseFailure() throws SQLException {
        // Set up input for the scanner
        InputStream input = new ByteArrayInputStream("2\n".getBytes());
        System.setIn(input);

        // Call the deleteCourse() method
        admin.deleteCourse(conn);

        // Check that the course was not deleted from the course_dis table
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM course_dis WHERE course_id = ?");
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        assertTrue(rs.next());

        // Reset System.in to its original value
        System.setIn(System.in);
    }
}

