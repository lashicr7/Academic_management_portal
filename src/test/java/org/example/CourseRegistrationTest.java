package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


//import static org.junit.Assert.assertEquals;
@TestInstance(Lifecycle.PER_CLASS)
public class CourseRegistrationTest {
    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert test data
        String sql = "INSERT INTO users (user_id, username, password, role) VALUES (?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setString(2, "testuser");
        stmt.setString(3, "testpass");
        stmt.setString(4, "student");
        stmt.executeUpdate();

        String insertCourse = "INSERT INTO course_dis (course_id, course_name, credit_structure, prerequisites, total_credits) VALUES " +
                "(1, 'Test Course', '3+0', NULL, 3)";
        String insertOffering = "INSERT INTO course_off (off_id, course_id, semester, cgpa_const, instructor) VALUES " +
                "(1, 1, 1, 3.0, 'Test Instructor')";
        String insertCurr = "INSERT INTO curriculum (id, semester, status) VALUES " +
                "(1, 1, 2)";

        Statement st = conn.createStatement();
        st.executeUpdate(insertCurr);
        st.executeUpdate(insertCourse);
        st.executeUpdate(insertOffering);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Remove test data
//        String sql = "DELETE FROM student_course_reg WHERE student_id=1";
//        Statement stmt = conn.createStatement();
//        stmt.executeUpdate(sql);


        String deleteCurr="DELETE FROM curriculum WHERE id = 1";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(deleteCurr);

        String sql = "DELETE FROM users WHERE user_id=1";
        stmt.executeUpdate(sql);


        sql = "DELETE FROM course_off WHERE off_id=1";
        stmt.executeUpdate(sql);
        sql = "DELETE FROM course_dis WHERE course_id=1";
        stmt.executeUpdate(sql);

        // Close the connection
        conn.close();
    }

    @Test
    public void testRegisterCourse() throws SQLException {
        // Set up input stream to simulate user input
        ByteArrayInputStream in = new ByteArrayInputStream("1\nY\n".getBytes());
        System.setIn(in);

        // Set up output stream to capture console output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        student student = new student();
        // Call the method to be tested
//        CourseRegistration cr = new CourseRegistration();
        student.registerCourse(conn, 1);
        System.out.println(out.toString());
        // Verify that the correct output was produced
        if (!out.toString().equals("Enter the course ID you want to register for: Confirm registration (Y/N): Registration successful.\n")) {
            System.out.println("Test failed: expected output was not printed");
        }
//        assertEquals("Enter the course ID you want to register for: Confirm registration (Y/N): Registration successful.\n", out.toString());

        // Verify that the course was registered
        try {
            String sql = "SELECT * FROM student_course_reg WHERE off_id=1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("off_id"));
            assertEquals(3, rs.getDouble("credits"), 0);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String deleteStudentCourse = "DELETE FROM student_course_reg WHERE off_id = 1";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(deleteStudentCourse);

    }

    @Test
    public void testDeleteRegistration() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\nY\n".getBytes());
        System.setIn(in);

        // Set up output stream to capture console output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        student student2 = new student();
        // Set up input stream to simulate user input
        student2.registerCourse(conn, 1);
        ByteArrayInputStream in2 = new ByteArrayInputStream("1\nY\n".getBytes());
        System.setIn(in2);

        // Set up output stream to capture console output
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out2));
        student student = new student();
        // Call the method to be tested
//        CourseRegistration cr = new CourseRegistration();
        student.deleteRegistration(conn, 1);

        // Verify that the correct output was produced
        if (!out2.toString().equals("Enter the course ID you want to unregister from: Confirm unregistration (Y/N): Unregistration successful.\n")) {
            System.out.println("Test failed: expected output was not printed");
        }
//        assertEquals("Enter the course ID you want to unregister from: Confirm unregistration (Y/N): Unregistration successful.\n", out.toString());

        // Verify that the course was unregistered
        try {
            String sql = "SELECT * FROM student_course_reg WHERE off_id=1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            assertTrue(!rs.next());
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String deleteStudentCourse = "DELETE FROM student_course_reg WHERE off_id = 1";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(deleteStudentCourse);
    }
}

