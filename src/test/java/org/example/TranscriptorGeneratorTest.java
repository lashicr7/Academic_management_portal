package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
public class TranscriptorGeneratorTest {

    private Connection conn;

    @BeforeAll
    public void setUp() throws Exception {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert sample data into the tables needed for the test
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

        // Insert sample course data
        sql = "INSERT INTO course_dis (course_id,course_name,credit_structure,prerequisites, total_credits) VALUES (?,?, '2-2',NULL, ?) RETURNING course_id";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1,1);
        stmt.setString(2, "Test Course 1");
        stmt.setInt(3, 3);
        stmt.executeQuery();
        rs = stmt.getResultSet();
        rs.next();
        int courseId1 = rs.getInt(1);
        stmt.setInt(1,2);
        stmt.setString(2, "Test Course 2");
        stmt.setInt(3, 4);
        stmt.executeQuery();
        rs = stmt.getResultSet();
        rs.next();
        int courseId2 = rs.getInt(1);

        sql = "INSERT INTO course_off (off_id,course_id, instructor,semester, cgpa_const) VALUES (?,?, ?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setInt(4, 1);
        stmt.setDouble(5, 3.5);
        stmt.setInt(2, 1);
        stmt.setString(3, "John Doe");
        stmt.executeUpdate();
        stmt.setInt(1, 2);
        stmt.setInt(4, 2);
        stmt.setDouble(5, 3.0);
        stmt.setInt(2, 2);
        stmt.setString(3, "Jane Doe");
        stmt.executeUpdate();
        sql = "INSERT INTO student_course_reg(reg_id, student_id, off_id,credits) VALUES (?, ?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.setInt(3, 1);
        stmt.setInt(4, 3);
        stmt.executeUpdate();
        stmt.setInt(1, 2);
        stmt.setInt(2, 1);
        stmt.setInt(3, 2);
        stmt.setInt(4, 4);
        stmt.executeUpdate();
        sql = "INSERT INTO grades (grade_id, reg_id, grade) VALUES (?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.setString(3, "A");
        stmt.executeUpdate();

        stmt.setInt(1, 2);
        stmt.setInt(2, 2);
        stmt.setString(3, "B+");
        stmt.executeUpdate();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        Statement st = conn.createStatement();

        // Delete the rows from the grades table
        String deleteGrades = "DELETE FROM grades WHERE reg_id IN (1,2)";
        st.executeUpdate(deleteGrades);
        // Delete the rows from the student_course_reg table
        String deleteStudentCourseReg = "DELETE FROM student_course_reg WHERE student_id = 1 AND off_id IN (1,2)";
        st.executeUpdate(deleteStudentCourseReg);
        // Delete the rows from the course_off table
        String deleteCourseOff = "DELETE FROM course_off WHERE course_id IN (1, 2, 3, 4)";
        st.executeUpdate(deleteCourseOff);
        // Delete the rows from the course_dis table
        String deleteCourseDis = "DELETE FROM course_dis WHERE course_id IN (1, 2, 3, 4)";
        st.executeUpdate(deleteCourseDis);
        // Delete the rows from the users table
        String deleteUsers1 = "DELETE FROM students WHERE user_id = 1";
        st.executeUpdate(deleteUsers1);
        String deleteUsers = "DELETE FROM users WHERE username = 'test_user'";
        st.executeUpdate(deleteUsers);
        // Create a SQL statement to delete data from the curriculum table
//        String deleteCurr = "DELETE FROM curriculum WHERE id = 1";
//        st.executeUpdate(deleteCurr);


        // Close the database connection
        if (st != null) {
            st.close();
        }
        if (conn != null) {
            conn.close();
        }
    }



    @Test
    public void testGenerateTranscript() throws Exception {
        // Call the method to be tested
        TranscriptGenerator generator = new TranscriptGenerator();
        generator.generateTranscript(conn, 1);

        // Check if the output file was created
        String fileName = "C:\\Users\\" + System.getProperty("user.name") + "\\OneDrive\\" +"\\Desktop\\" + "test_user_report.txt";
        File file = new File(fileName);
        assertTrue(file.exists());

        // Check the content of the output file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            assertEquals("Name: test_user", reader.readLine());
            assertEquals("Entry Year: 2020", reader.readLine());
            assertEquals("Branch: Computer Science", reader.readLine());
            assertEquals("Test Course 1", reader.readLine());
            assertEquals("Semester: 1", reader.readLine());
            assertEquals("Instructor: John Doe", reader.readLine());
            assertEquals("Grade: A", reader.readLine());
            assertEquals("CGPA Constant: 3.5", reader.readLine());
            assertEquals("", reader.readLine());
            assertEquals("Test Course 2", reader.readLine());
            assertEquals("Semester: 2", reader.readLine());
            assertEquals("Instructor: Jane Doe", reader.readLine());
            assertEquals("Grade: B+", reader.readLine());
            assertEquals("CGPA Constant: 3.0", reader.readLine());
            assertEquals("", reader.readLine());
            assertEquals("CGPA: 8.857142857142858", reader.readLine());
        }
    }

}





