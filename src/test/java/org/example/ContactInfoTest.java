package org.example;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import static org.junit.Assert.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


//import static org.junit.Assert.assertEquals;
@TestInstance(Lifecycle.PER_CLASS)
public class ContactInfoTest {private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert a sample row in the students table
        String sql = "INSERT INTO users (user_id,username,password,role) VALUES (?,?,?,?) RETURNING user_id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setString(2, "test_user");
        stmt.setString(3, "sql");
        stmt.setString(4, "student");
        stmt.executeQuery();
        ResultSet rs = stmt.getResultSet();
        rs.next();
        int userId = rs.getInt(1);

        // Insert a sample student
        sql = "INSERT INTO students (student_id,user_id, phonenumber, address) VALUES (?,?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setInt(2, userId);
        stmt.setInt(3, 2020);
        stmt.setString(4, "Computer Science");
        stmt.executeUpdate();
//    PreparedStatement insertStmt = conn.prepareStatement(
//            "INSERT INTO students (user_id, entry_year,branch,phonenumber, address) VALUES (?, ?,?,?, ?)");
//    insertStmt.setInt(1,1);
//    insertStmt.setInt(2, 555555);
//    insertStmt.setString(3, "123 Main St");
//    insertStmt.setInt(4, 555555);
//    insertStmt.setString(5, "123 Main St");
//    insertStmt.executeUpdate();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Delete the students table
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE from students where user_id=1");
        String deleteUsers = "DELETE FROM users WHERE username = 'test_user'";
        stmt.executeUpdate(deleteUsers);

        // Close the database connection
        conn.close();
    }

    @Test
    public void testUpdateContactInfo() throws SQLException {
        student student = new student();
        // Call method under test
//    ContactInfo contactInfo = new ContactInfo();
        student.updateContactInfo(conn, 1, 123456);

        // Check that the phone number was updated in the database
        assertTrue(conn.createStatement().executeQuery(
                "SELECT * FROM students WHERE user_id=1 AND phonenumber=123456").next());
    }

    @Test
    public void testUpdateAddr() throws SQLException {
        // Call method under test
//    ContactInfo contactInfo = new ContactInfo();
        student student = new student();
        student.updateAddr(conn, 1, "456 Elm St");

        // Check that the address was updated in the database
        assertTrue(conn.createStatement().executeQuery(
                "SELECT * FROM students WHERE user_id=1 AND address='456 Elm St'").next());
    }}

