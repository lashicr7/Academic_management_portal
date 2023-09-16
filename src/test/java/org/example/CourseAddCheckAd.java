package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
public class CourseAddCheckAd {

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
        String deleteCourseDis = "DELETE FROM course_dis WHERE course_name IN ('cs101','Test Course')";
        Statement st = conn.createStatement();
        st.executeUpdate(deleteCourseDis);

        // Close the database connection
        conn.close();
    }

    @Test
    public void testAddNewCourseWithValidInput() throws SQLException {
        // Set up input for the scanner
        InputStream input = new ByteArrayInputStream("Test Course\n3+1\nNone\n4\n".getBytes());
        System.setIn(input);

        // Call the method with valid input
        admin.addNewCourse(conn);

        // Check that the course was added to the course_dis table
        assertTrue(conn.createStatement().executeQuery("SELECT * FROM course_dis WHERE course_name='Test Course'").next());
    }

    @Test
    public void testAddNewCourseWithInvalidInput() throws SQLException {
        // Set up input for the scanner
//    	System.out.println("hgc");
        InputStream input = new ByteArrayInputStream("Test Course\n3+1\nNone\n4\n".getBytes());
        System.setIn(input);

        // Call the method with valid input
        admin.addNewCourse(conn);
        input = new ByteArrayInputStream("Test Course\n3+1\nNone\n4\n".getBytes());
        System.setIn(input);

        // Call the method with invalid input
        int result=admin.addNewCourse(conn);

        // Check that the course was not added to the course_dis table
//        assertFalse(conn.createStatement().executeQuery("SELECT * FROM course_dis WHERE course_name='Test Course'").next());
        // Check that the method returns 3
        System.out.println(result);
        assertEquals(0, result);

        // Reset System.in to its original value
        String deleteCourseDis = "DELETE FROM course_dis WHERE course_name ='Test Course'";
        Statement st = conn.createStatement();
        st.executeUpdate(deleteCourseDis);
        System.setIn(System.in);

    }

}
