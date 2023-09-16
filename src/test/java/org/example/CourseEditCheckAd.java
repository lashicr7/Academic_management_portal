package org.example;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class CourseEditCheckAd {
    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert a course into the course_dis table
        String insertCoursedis = "INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES " +
                "(1,'cs101', '3+1', NULL, 3)";
        Statement st = conn.createStatement();
        st.executeUpdate(insertCoursedis);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Delete the course from the course_dis table
        String deleteCourseDis = "DELETE FROM course_dis WHERE course_name IN ('cs101','cs102')";
        Statement st = conn.createStatement();
        st.executeUpdate(deleteCourseDis);

        // Close the database connection
        conn.close();
    }

    @Test
    public void testEditCourseCatalogWithValidInput() throws SQLException {
        admin admin = new admin();
        // Call the method with valid input
        admin.editCourseCatalog(conn, 1, "cs102", "3+1", "cs101");

        // Check that the course was updated in the course_dis table
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM course_dis WHERE course_id = ?");
        statement.setInt(1, 1);
        ResultSet resultSet = statement.executeQuery();
        assertTrue(resultSet.next());
        assertEquals("cs102", resultSet.getString("course_name"));
        assertEquals("3+1", resultSet.getString("credit_structure"));
        assertEquals("cs101", resultSet.getString("prerequisites"));
    }

    @Test
    public void testEditCourseCatalogWithInvalidCourseID() throws SQLException {
        admin admin = new admin();
        // Call the method with invalid course_id
        admin.editCourseCatalog(conn, 10, "cs102", "3+1", "cs101");

        // Check that no rows were updated in the course_dis table
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM course_dis WHERE course_id = ?");
        statement.setInt(1, 1);
        ResultSet resultSet = statement.executeQuery();
        assertTrue(resultSet.next());
        assertEquals("cs101", resultSet.getString("course_name"));
        assertEquals("3+1", resultSet.getString("credit_structure"));
        assertEquals(null, resultSet.getString("prerequisites"));
    }
}

