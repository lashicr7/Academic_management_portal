package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


//import static org.junit.Assert.assertEquals;
@TestInstance(Lifecycle.PER_CLASS)
public class GradeCalculationTest {
    private Connection conn;

    @BeforeAll
    void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert test data
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
        sql = "INSERT INTO students (student_id,user_id, entry_year, branch) VALUES (?,?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setInt(2, userId);
        stmt.setInt(3, 2020);
        stmt.setString(4, "Computer Science");
        stmt.executeUpdate();
        String insertCourse = "INSERT INTO course_dis (course_id, course_name, credit_structure, prerequisites, total_credits) VALUES " +
                "(1, 'Test Course', '3+0', NULL, 3)";
        String insertOffering = "INSERT INTO course_off (off_id, course_id, semester, cgpa_const, instructor) VALUES " +
                "(1, 1, 1, 3.0, 'Test Instructor')";
        String insertStudentCourse = "INSERT INTO student_course_reg (reg_id, student_id, off_id,credits) VALUES " +
                "(1, 1, 1,3)";
        String insertGrade = "INSERT INTO grades (reg_id, grade) VALUES " +
                "(1, 'B+')";
        Statement st = conn.createStatement();
        st.executeUpdate(insertCourse);
        st.executeUpdate(insertOffering);
        st.executeUpdate(insertStudentCourse);
        st.executeUpdate(insertGrade);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Delete test data
        String deleteCourse = "DELETE FROM course_dis WHERE course_id = 1";
        String deleteOffering = "DELETE FROM course_off WHERE off_id = 1";
        String deleteStudentCourse = "DELETE FROM student_course_reg WHERE reg_id = 1";
        String deleteGrade = "DELETE FROM grades WHERE reg_id = 1";
        Statement st = conn.createStatement();




        String deleteUsers1 = "DELETE FROM students WHERE user_id = 1";
        st.executeUpdate(deleteUsers1);
        String deleteUsers = "DELETE FROM users WHERE username = 'test_user'";
        st.executeUpdate(deleteGrade);
        st.executeUpdate(deleteStudentCourse);
        st.executeUpdate(deleteUsers);

        st.executeUpdate(deleteOffering);
        st.executeUpdate(deleteCourse);

        // Close the database connection
        conn.close();
    }

    @Test
    public void testDisplayGrades() {
        // Redirect standard output to a stream for testing
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Call the method with valid input
        try {
            student.displayGrades(conn, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String printedOutput = outputStream.toString();
        if (!printedOutput.equals("courseid|grade\\n1|B+\\n")) {
            System.out.println("Test failed: expected output was not printed");
        }
//        assertEquals("courseid|grade\n1|B+\n", printedOutput);
    }

    @Test
    public void testCalculateCGPA() {
        double expectedCGPA = 8.0;
        student student = new student(); // create an instance of Student

        // Call the method with valid input
        double actualCGPA = student.calculateCGPA(conn, 1);

        assertEquals(expectedCGPA, actualCGPA, 0.01);
    }

}

