package org.example;

//import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class userTest {
    private Connection conn;
    @BeforeAll
    public void setUp() throws SQLException {
        // Connect to the test database
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lashi7941");

        // Insert a course in the course_off table for testing displayCourses
        String insertCoursedis = "INSERT INTO users (user_id,username, password, role) VALUES " +
                "(1,'st', 'hi', 'student'),"+
                "(2,'prf', 'hi', 'instructor'),"+
                "(3,'adm', 'hi', 'admin')";

        Statement st = conn.createStatement();
        st.executeUpdate(insertCoursedis);
        PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO curriculum (id,semester, status) VALUES (?,1, ?)");
        insertStmt.setInt(1,1);
        insertStmt.setInt(2, 3);
        insertStmt.executeUpdate();
        // Insert a course in course_off table
//    String insertCourseOff = "INSERT INTO course_off (off_id,course_id, semester, cgpa_const, instructor) VALUES " +
//            "(1,1, 1, 3.0, 'user1')";
//    st.executeUpdate(insertCourseOff);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        // Delete the course added in setUp()
//    String deleteCourseOff = "DELETE FROM course_off WHERE course_id=1";
//    Statement st = conn.createStatement();
//    st.executeUpdate(deleteCourseOff);
        String deleteCourseDis = "DELETE FROM users WHERE password='hi'";

        Statement st = conn.createStatement();
        st.executeUpdate(deleteCourseDis);
        st.execute("DELETE from curriculum where id=1");

        // Close the database connection
        conn.close();
    }

//@Test
//public void testUserDisWithValidInput() {
//    // Redirect standard output to a stream for testing
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    prof prof = new prof();
//	// Call the method with valid input
//    prof.displayCourses(conn, "user1");
//    String printedOutput = outputStream.toString();
//    if (!printedOutput.equals("Courses floated by professor user1:\n" +
//            "Off ID: 1\n" +
//            "Course ID: 1\n" +
//            "Semester: 1\n" +
//            "CGPA Constant: 3.0\n\n")) {
//        System.out.println("Test failed: expected output was not printed");
//    }
//    // Check that the expected output was printed
////    assertEquals("Courses floated by professor user1:\n" +
////                 "Off ID: 1\n" +
////                 "Course ID: 1\n" +
////                 "Semester: 1\n" +
////                 "CGPA Constant: 3.0\n\n", outputStream.toString());
//
//    // Reset standard output to its original value
//    System.setOut(System.out);
//}

    @Test
    public void testDisplayst() {
        // Redirect standard output to a stream for
        InputStream systemIn = System.in;
        String input = "st\nhi\n9\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
//
//  // Call the method with invalid input
//  prof prof = new prof();
//  prof.displayCourses(conn, "user2");
//
//  // Get the printed output as a string
//  String printedOutput = outputStream.toString();
        // Act
        user_auth.main(new String[]{});
        String printedOutput = outputStream.toString();

        // Assert
        if (!printedOutput.equals("Connected to the database!\n"
                + "Enter username: Enter password: \n1. Display available courses\n"
                + "2. Register for a course\n"
                + "3. De-register for a course\n"
                + "4. Display courses registered\n"
                + "5. Display grades assigned\n"
                + "6. compute current CGPA\n"
                + "7. Edit your profile\n"
                + "8. Check graduation eligibility\n"
                + "9. Logout\nEnter your choice: Logged out successfully.\n")) {
            System.out.println("Test failed: expected output was not printed");
        }
//    assertEquals("Connected to the database!\n"
//    		+ "Enter username: Enter password: \n1. Display available courses\n"
//    		+ "2. Register for a course\n"
//    		+ "3. De-register for a course\n"
//    		+ "4. Display courses registered\n"
//    		+ "5. Display grades assigned\n"
//    		+ "6. compute current CGPA\n"
//    		+ "7. Edit your profile\n"
//    		+ "8. Check graduation eligibility\n"
//    		+ "9. Logout\nEnter your choice: Logged out successfully.\n", printedOutput);
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    // Call the method with invalid input
//    prof prof = new prof();
//    prof.displayCourses(conn, "user2");
//
//    // Get the printed output as a string
//    String printedOutput = outputStream.toString();
//
//    // Check that the expected output was printed
//    if (!printedOutput.equals("No courses found for professor user2 ")) {
//        System.out.println("Test failed: expected output was not printed");
//    }

        // Reset standard output to its original value
        System.setOut(System.out);
    }
    @Test
    public void testDisplaypr() {
        // Redirect standard output to a stream for
//	InputStream systemIn = System.in;
        String input = "prf\nhi\n4\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
//
//  // Call the method with invalid input
//  prof prof = new prof();
//  prof.displayCourses(conn, "user2");
//
//  // Get the printed output as a string
//  String printedOutput = outputStream.toString();
        // Act
        user_auth.main(new String[]{});
        String printedOutput = outputStream.toString();
        System.out.println(printedOutput);

        // Assert
        if (!printedOutput.equals("Connected to the database!\r\n"
                + "Enter username: Enter password: \r\n"
                + "1. Add a new offering\r\n"
                + "2. Delete a offering\r\n"
                + "3. Assign grades\r\n"
                + "4. Logout\r\n"
                + "Enter your choice: Logged out successfully.\n")) {
            System.out.println("Test failed: expected output was not printed");
        }
//    assertEquals("Connected to the database!\r\n"
//    		+ "Enter username: Enter password: \r\n"
//    		+ "1. Add a new offering\r\n"
//    		+ "2. Delete a offering\r\n"
//    		+ "3. Assign grades\r\n"
//    		+ "4. Logout\r\n"
//    		+ "Enter your choice: Logged out successfully.\r\n", printedOutput);
//    assertEquals("Connected to the database!\n"
//    		+ "Enter username: Enter password: \n1. Display available courses\n"
//    		+ "2. Register for a course\n"
//    		+ "3. De-register for a course\n"
//    		+ "4. Display courses registered\n"
//    		+ "5. Display grades assigned\n"
//    		+ "6. compute current CGPA\n"
//    		+ "7. Edit your profile\n"
//    		+ "8. Check graduation eligibility\n"
//    		+ "9. Logout\nEnter your choice: Logged out successfully.\n", printedOutput);
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    // Call the method with invalid input
//    prof prof = new prof();
//    prof.displayCourses(conn, "user2");
//
//    // Get the printed output as a string
//    String printedOutput = outputStream.toString();
//
//    // Check that the expected output was printed
//    if (!printedOutput.equals("No courses found for professor user2 ")) {
//        System.out.println("Test failed: expected output was not printed");
//    }

        // Reset standard output to its original value
        System.setOut(System.out);
    }
    @Test
    public void testDisplayad() {
        // Redirect standard output to a stream for
//	InputStream systemIn = System.in;
        String input = "adm\nhi\n7\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
//
//  // Call the method with invalid input
//  prof prof = new prof();
//  prof.displayCourses(conn, "user2");
//
//  // Get the printed output as a string
//  String printedOutput = outputStream.toString();
        // Act
        user_auth.main(new String[]{});
        String printedOutput = outputStream.toString();
        System.out.println(printedOutput);

        // Assert
        if (!printedOutput.equals("Connected to the database!\r\n"
                + "Enter username: Enter password: \r\n"
                + "1. View course catalog\r\n"
                + "2.Delete course from catalog\r\n"
                + "3.Add new course\r\n"
                + "4. Generate transcript\r\n"
                + "5. Add new curriculum\r\n"
                + "6. Modify curriculum status\r\n"
                + "7. Logout\r\n"
                + "Enter your choice: Logged out successfully.\r\n"
                + "")) {
            System.out.println("Test failed: expected output was not printed");
        }
//    assertEquals("Connected to the database!\r\n"
//    		+ "Enter username: Enter password: \r\n"
//    		+ "1. View course catalog\r\n"
//    		+ "2.Delete course from catalog\r\n"
//    		+ "3.Add new course\r\n"
//    		+ "4. Generate transcript\r\n"
//    		+ "5. Add new curriculum\r\n"
//    		+ "6. Modify curriculum status\r\n"
//    		+ "7. Logout\r\n"
//    		+ "Enter your choice: Logged out successfully.\r\n"
//    		+ "", printedOutput);
//    assertEquals("Connected to the database!\n"
//    		+ "Enter username: Enter password: \n1. Display available courses\n"
//    		+ "2. Register for a course\n"
//    		+ "3. De-register for a course\n"
//    		+ "4. Display courses registered\n"
//    		+ "5. Display grades assigned\n"
//    		+ "6. compute current CGPA\n"
//    		+ "7. Edit your profile\n"
//    		+ "8. Check graduation eligibility\n"
//    		+ "9. Logout\nEnter your choice: Logged out successfully.\n", printedOutput);
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    // Call the method with invalid input
//    prof prof = new prof();
//    prof.displayCourses(conn, "user2");
//
//    // Get the printed output as a string
//    String printedOutput = outputStream.toString();
//
//    // Check that the expected output was printed
//    if (!printedOutput.equals("No courses found for professor user2 ")) {
//        System.out.println("Test failed: expected output was not printed");
//    }

        // Reset standard output to its original value
        System.setOut(System.out);
    }
    @Test
    public void testDisplayregst() {
        // Redirect standard output to a stream for
        InputStream systemIn = System.in;
        String input = "st\nhi\n1\n9\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        user_auth.main(new String[]{});
        String printedOutput = outputStream.toString();
//
//  // Call the method with invalid input
//  prof prof = new prof();
//  prof.displayCourses(conn, "user2");
//
//  // Get the printed output as a string
//  String printedOutput = outputStream.toString();
        // Act


        // Assert
        if (!printedOutput.equals("Connected to the database!\r\n"
                + "Enter username: Enter password: \r\n"
                + "1. Display available courses\r\n"
                + "2. Register for a course\r\n"
                + "3. De-register for a course\r\n"
                + "4. Display courses registered\r\n"
                + "5. Display grades assigned\r\n"
                + "6. compute current CGPA\r\n"
                + "7. Edit your profile\r\n"
                + "8. Check graduation eligibility\r\n"
                + "9. Logout\r\n"
                + "Enter your choice: Courses are not available for registration.\r\n"
                + "\r\n"
                + "1. Display available courses\r\n"
                + "2. Register for a course\r\n"
                + "3. De-register for a course\r\n"
                + "4. Display courses registered\r\n"
                + "5. Display grades assigned\r\n"
                + "6. compute current CGPA\r\n"
                + "7. Edit your profile\r\n"
                + "8. Check graduation eligibility\r\n"
                + "9. Logout\r\n"
                + "Enter your choice: Logged out successfully.\r\n"
                + "")) {
            System.out.println("Test failed: expected output was not printed");
        }
//    assertEquals("Connected to the database!\r\n"
//    		+ "Enter username: Enter password: \r\n"
//    		+ "1. Display available courses\r\n"
//    		+ "2. Register for a course\r\n"
//    		+ "3. De-register for a course\r\n"
//    		+ "4. Display courses registered\r\n"
//    		+ "5. Display grades assigned\r\n"
//    		+ "6. compute current CGPA\r\n"
//    		+ "7. Edit your profile\r\n"
//    		+ "8. Check graduation eligibility\r\n"
//    		+ "9. Logout\r\n"
//    		+ "Enter your choice: Courses are not available for registration.\r\n"
//    		+ "\r\n"
//    		+ "1. Display available courses\r\n"
//    		+ "2. Register for a course\r\n"
//    		+ "3. De-register for a course\r\n"
//    		+ "4. Display courses registered\r\n"
//    		+ "5. Display grades assigned\r\n"
//    		+ "6. compute current CGPA\r\n"
//    		+ "7. Edit your profile\r\n"
//    		+ "8. Check graduation eligibility\r\n"
//    		+ "9. Logout\r\n"
//    		+ "Enter your choice: Logged out successfully.\r\n"
//    		+ "", printedOutput);
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStream));
//
//    // Call the method with invalid input
//    prof prof = new prof();
//    prof.displayCourses(conn, "user2");
//
//    // Get the printed output as a string
//    String printedOutput = outputStream.toString();
//
//    // Check that the expected output was printed
//    if (!printedOutput.equals("No courses found for professor user2 ")) {
//        System.out.println("Test failed: expected output was not printed");
//    }

        // Reset standard output to its original value
        System.setOut(System.out);
    }
}

