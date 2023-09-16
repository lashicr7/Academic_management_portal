package org.example;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
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
public class RegisterCheckTest {
    private Connection conn;

    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");
        String insertCoursedis = "INSERT INTO course_dis (course_id,course_name, credit_structure, prerequisites, total_credits) VALUES " +
                "(1,'cs101', '3+1', NULL, 3)," +
                "(2,'cs202', '3+2',NULL, 3)," +
                "(3,'cs301', '3+2', 'cs202', 3)," +
                "(4,'cs401', '3+1', 'cs101', 20)";
        Statement st = conn.createStatement();
        st.executeUpdate(insertCoursedis);
        String insertCourseOffData = "INSERT INTO course_off (off_id,course_id, instructor, semester, cgpa_const) VALUES " +
                "(1,1, 'A', 1, 0)," +
                "(2,2, 'B', '1', 0)," +
                "(3,3, 'C', '1', 0)," +
                "(4,4, 'D', '3', 0)";
        st.execute(insertCourseOffData);
        String insertUserData = "INSERT INTO users (user_id,username, password, role) VALUES " +
                "(1,'sai', 'pass123', 'student')";
        st.execute(insertUserData);
        String insertStudentCourseRegData = "INSERT INTO student_course_reg (reg_id,student_id, off_id, credits) VALUES " +
                "(1,1, 1, 4)";
        st.execute(insertStudentCourseRegData);
        String insertGrades = "INSERT INTO grades (grade_id,reg_id, grade) VALUES " +
                "(1,1, 'A')";
        st.execute(insertGrades);
        String insertcurr = "INSERT INTO curriculum (semester, status) VALUES" +
                "(3,2)";
        st.execute(insertcurr);
    }



    @AfterAll
    public void tearDown() throws SQLException {
        Statement st = conn.createStatement();

        // Delete the rows from the grades table
        String deleteGrades = "DELETE FROM grades WHERE reg_id = 1";
        st.executeUpdate(deleteGrades);
        // Delete the rows from the student_course_reg table
        String deleteStudentCourseReg = "DELETE FROM student_course_reg WHERE student_id = 1 AND off_id = 1";
        st.executeUpdate(deleteStudentCourseReg);
        // Delete the rows from the course_off table
        String deleteCourseOff = "DELETE FROM course_off WHERE course_id IN (1, 2, 3, 4)";
        st.executeUpdate(deleteCourseOff);
        // Delete the rows from the course_dis table
        String deleteCourseDis = "DELETE FROM course_dis WHERE course_name IN ('cs101', 'cs202', 'cs301', 'cs401')";
        st.executeUpdate(deleteCourseDis);
        // Delete the rows from the users table
        String deleteUsers = "DELETE FROM users WHERE username = 'sai'";
        st.executeUpdate(deleteUsers);
        // Create a SQL statement to delete data from the curriculum table
        String deleteCurr = "DELETE FROM curriculum WHERE semester=3";
        st.executeUpdate(deleteCurr);


        // Close the database connection
        if (st != null) {
            st.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void testCheckPrerequisitesWithNullPrerequisites() throws SQLException {

        // Test the checkPrerequisites() method with null prerequisites
        boolean result = registercheck.checkPrerequisites(conn, 1, 2);
        assertTrue(result);
    }

    @Test
    public void testCheckPrerequisitesWithUnmetPrerequisites() throws SQLException {
        // Test the checkPrerequisites() method with unmet prerequisites
        boolean result = registercheck.checkPrerequisites(conn, 1, 3);
        assertFalse(result);
    }

    @Test
    public void testCheckPrerequisitesWithMetPrerequisites() throws SQLException {
        // Test the checkPrerequisites() method with met prerequisites
        boolean result = registercheck.checkPrerequisites(conn, 1, 4);
        assertTrue(result);
    }

    @Test
    public void testGetCourseIdByNameWithExistingCourse() throws SQLException {
        // Test the getCourseIdByName() method with an existing course
        int result = registercheck.getCourseIdByName(conn, "cs101");
        assertEquals(1, result);
    }

    @Test
    public void testGetCourseIdByNameWithNonExistingCourse() throws SQLException {
        // Test the getCourseIdByName() method with a non-existing course
        int result = registercheck.getCourseIdByName(conn, "NonExistingCourse");
//        System.out.println(result);
        assertEquals(-1, result);
    }

    @Test
    public void testCreditsCheckWithExceedingCredits() throws SQLException {
        // Test the creditsCheck() method with exceeding credits
        boolean result = registercheck.creditsCheck(conn, 1, 4);
        assertFalse(result);
    }

    @Test
    public void testCreditsCheckWithNonExceedingCredits() throws SQLException {
        // Test the creditsCheck() method with non-exceeding credits
        boolean result = registercheck.creditsCheck(conn, 1, 2);
        assertTrue(result);
    }

//    @Test
//    public void testCreditsCheckWithTwoSemCredits() throws SQLException {
//        // Test the creditsCheck() method with non-exceeding credits
//        boolean result = registercheck.creditsCheck(conn, 5, 13);
//        assertFalse(result);
//    }
}

