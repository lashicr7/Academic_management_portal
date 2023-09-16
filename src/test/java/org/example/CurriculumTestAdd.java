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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


//import static org.junit.Assert.assertEquals;
@TestInstance(Lifecycle.PER_CLASS)
public class CurriculumTestAdd {

    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert a sample curriculum row
        PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO curriculum (semester, status) VALUES (?, ?)");
        insertStmt.setInt(1,1);
        insertStmt.setInt(2, 2);
        insertStmt.executeUpdate();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Delete the curriculum table
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE from curriculum where semester=1");

        // Close the database connection
        conn.close();
    }

    @Test
    public void testAddNewCurriculumWithValidInput() throws SQLException {
        // Set up input for the scanner
        InputStream input = new ByteArrayInputStream("1\n3\n".getBytes());
        System.setIn(input);

        admin admin = new admin();
        // Call the method with valid input
        admin.addNewCurriculum(conn);

        // Check that the curriculum was added to the curriculum table
        assertTrue(conn.createStatement().executeQuery(
                "SELECT * FROM curriculum WHERE semester=1 AND status=2").next());
    }

//    @Test
//    public void testAddNewCurriculumWithInvalidInput() throws SQLException {
//        // Set up input for the scanner
//        InputStream input = new ByteArrayInputStream("1\nabc\n".getBytes());
//        System.setIn(input);
//
//        admin admin = new admin();
//		// Call the method with invalid input
//        admin.addNewCurriculum(conn);
//
//        // Check that the curriculum was not added to the curriculum table
//        assertTrue(!conn.createStatement().executeQuery(
//                "SELECT * FROM curriculum WHERE semester=1 AND status='abc'").next());
//    }

}


