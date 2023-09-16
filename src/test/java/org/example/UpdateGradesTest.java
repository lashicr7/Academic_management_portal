package org.example;

//import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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
public class UpdateGradesTest {
    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Create a test course in the course_off table
        String insertCoursedis = "INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES " +
                "(1,'cs101', '3+1', NULL, 3)";
        Statement st = conn.createStatement();
        st.executeUpdate(insertCoursedis);
        // Insert a course in course_off table
        String insertCourseOff = "INSERT INTO course_off (off_id,course_id, semester, cgpa_const, instructor) VALUES " +
                "(1,1, 1, 3.0, 'Instructor A')";
        st.executeUpdate(insertCourseOff);
        String insertUserData = "INSERT INTO users (user_id,username, password, role) VALUES " +
                "(1,'sai', 'pass123', 'student')";
        st.execute(insertUserData);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Drop the test course and its grades from the database
        PreparedStatement ps = conn.prepareStatement("DELETE FROM grades WHERE reg_id IN (SELECT reg_id FROM student_course_reg WHERE off_id=1)");
        ps.executeUpdate();
        ps.close();

        ps = conn.prepareStatement("DELETE FROM student_course_reg WHERE off_id=1");
        ps.executeUpdate();
        ps.close();
        ps = conn.prepareStatement("DELETE FROM users WHERE user_id=1");
        ps.executeUpdate();
        ps.close();
        ps = conn.prepareStatement("DELETE FROM course_off WHERE off_id=1");
        ps.executeUpdate();
        ps.close();
        ps = conn.prepareStatement("DELETE FROM course_dis WHERE course_id=1");
        ps.executeUpdate();
        ps.close();

        // Close the database connection
        conn.close();
    }

    @Test
    public void testUpdateGradesFromCsv() throws SQLException, IOException {
        // Insert some test registrations for the test course
        PreparedStatement ps = conn.prepareStatement("INSERT INTO student_course_reg (reg_id,student_id, off_id,credits) VALUES (?,1, 1,3)");
        ps.setInt(1, 1);
        ps.executeUpdate();
        ps.setInt(1, 2);
        ps.executeUpdate();
        ps.close();

        // Set up a test CSV file with grades for the test registrations
        String csvData = "1,A\n2,B\n";
        File csvFile = File.createTempFile("test_grades", ".csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
        writer.write(csvData);
        writer.close();

        // Call the method to update the grades
        prof.updateGradesFromCsv(conn, csvFile.getPath(), 1);

        // Check that the grades were updated correctly in the database
        ps = conn.prepareStatement("SELECT grade FROM grades WHERE reg_id=1");
        ResultSet rs = ps.executeQuery();
        assertTrue(rs.next());
        assertEquals("A", rs.getString(1));
        assertFalse(rs.next());
        ps.close();
        rs.close();
        ps = conn.prepareStatement("SELECT grade FROM grades WHERE reg_id=2");
        rs = ps.executeQuery();
        assertTrue(rs.next());
        assertEquals("B", rs.getString(1));
        assertFalse(rs.next());
        ps.close();
        rs.close();

        // Delete the temporary file
        csvFile.delete();
//    Statement st = conn.createStatement();
//    String deleteCoursereg = "DELETE FROM student_course_reg WHERE off_id IN (1)";
//    st.executeUpdate(deleteCoursereg);
    }

}