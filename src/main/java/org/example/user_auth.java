package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class user_auth {
    private static final String USER_TABLE = "users";
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Get a connection to the database
            conn = DBconn.getConnection();

            // Get the user input for username and password
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Execute the query to authenticate the user
            stmt = conn.createStatement();
            String sql = "SELECT * FROM " + USER_TABLE + " WHERE username='" + username + "' AND password='" + password + "'";
            rs = stmt.executeQuery(sql);

            // Check if the user is authenticated or not
            if (rs.next()) {
                String role = rs.getString("role");
                int userId = rs.getInt("user_id");

                if (role.equals("student")) {
                    student stu = new student();

                    while (true) {
                        System.out.println("\n1. Display available courses");
                        System.out.println("2. Register for a course");
                        System.out.println("3. De-register for a course");
                        System.out.println("4. Display courses registered");
                        System.out.println("5. Display grades assigned");
                        System.out.println("6. compute current CGPA");
                        System.out.println("7. Edit your profile");
                        System.out.println("8. Check graduation eligibility");
                        System.out.println("9. Logout");
                        System.out.print("Enter your choice: ");

                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1:
                                int currStatus = CurriculumManager.getLatestCurriculumstatus(conn);
                                if (currStatus == 2 ) {
                                    stu.displayCourses(conn);
                                } else {
                                    System.out.println("Courses are not available for registration.");
                                }
                                break;
                            case 2:
                                currStatus = CurriculumManager.getLatestCurriculumstatus(conn);
                                if (currStatus == 2) {
                                    stu.registerCourse(conn, userId);
                                } else {
                                    System.out.println("Course registration is not open at this time.");
                                }
                                break;
                            case 3:
                                currStatus = CurriculumManager.getLatestCurriculumstatus(conn);
                                if (currStatus == 2) {
                                    stu.displayRegisteredCourses(conn, userId);
                                    stu.deleteRegistration(conn, userId);
                                } else {
                                    System.out.println("Courses are not available to delete at this moment.");
                                }
                                break;
                            case 4:
                                stu.displayRegisteredCourses(conn, userId);
                                break;
                            case 6:
                                System.out.println("Your current CGPA is"+stu.calculateCGPA(conn,userId));
                                break;
                            case 5:
                                student.displayGrades(conn,userId);
                                break;
                            case 7:
                                System.out.println("\nEnter 1 to edit your phone number");
                                System.out.println("Enter 2 to edit your address");
                                int choice1 = scanner.nextInt();
                                scanner.nextLine();
                                switch(choice1){
                                    case 1:
                                        System.out.println("Enter your new phonenumber:");
                                        int phonenumber = scanner.nextInt();
                                        scanner.nextLine();
                                        stu.updateContactInfo(conn,userId,phonenumber);
                                    case 2:
                                        System.out.println("Enter your new address:");
                                        String address = scanner.nextLine();
                                        stu.updateAddr(conn,userId,address);
                                }

                            case 8:
                                gradcheck.checkGraduationStatus(conn, userId);
                                break;
                            case 9:
                                System.out.println("Logged out successfully.");
                                return;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }

                } else if (role.equals("instructor")) {
                    prof ins = new prof();

                    while (true) {
//                	scanner = new Scanner(System.in);
                        System.out.println("\n1. Add a new offering");
                        System.out.println("2. Delete a offering");
                        System.out.println("3. Assign grades");
                        System.out.println("4. Logout");
                        System.out.print("Enter your choice: ");

                        int choice = scanner.nextInt();
//                    System.out.println(choice);
                        scanner.nextLine();

                        switch (choice) {
                            case 1:
                                int currStatus = CurriculumManager.getLatestCurriculumstatus(conn);
                                if (currStatus == 1) {
                                    prof.addNewCourse(conn, username);
                                } else {
                                    System.out.println("Courses are not available to offer.");
                                }
                                break;
                            case 2:
                                currStatus = CurriculumManager.getLatestCurriculumstatus(conn);
                                if (currStatus == 1) {
                                    prof.deleteCourse(conn);
                                } else {
                                    System.out.println("Courses are not available to delete at this moment.");
                                }
                                break;
                            case 3:
                                currStatus = CurriculumManager.getLatestCurriculumstatus(conn);
                                if (currStatus == 3) {
                                    ins.displayCourses(conn,username);
                                    System.out.print("Enter off_id: ");
//                                        Scanner scanner = new Scanner(System.in);
                                    int courseId = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.println("Enter File path to upload:");
                                    String path =scanner.nextLine();
//                                        scanner.nextLine();
                                    prof.updateGradesFromCsv(conn,path,courseId);
                                } else {
                                    System.out.println("Courses are not available to grade at this moment.");
                                }
                                break;
                            case 4:
                                System.out.println("Logged out successfully.");
                                return;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }
                } else if (role.equals("admin")) {
                    admin adn = new admin();
                    while (true) {
                        System.out.println("\n1. View course catalog");
                        System.out.println("2.Delete course from catalog");
                        System.out.println("3.Add new course");
                        System.out.println("4. Generate transcript");
                        System.out.println("5. Add new curriculum");
                        System.out.println("6. Modify curriculum status");
                        System.out.println("7. Logout");
                        System.out.print("Enter your choice: ");

                        int choice = scanner.nextInt();
//                    scanner.nextLine();

                        switch (choice) {
                            case 1:
                                adn.displayCourses(conn);
                                break;
                            case 2:
                                if (!username.equals("Staff Deans office")) {
                                    System.out.println("Error: Access denied. Only staff from Dean's office can edit the course catalog.");
                                    return;
                                }
                                int currStatus = CurriculumManager.getLatestCurriculumstatus(conn);
                                if (currStatus == 0) {
                                    adn.displayCourses(conn);
                                    admin.deleteCourse(conn);
                                } else {
                                    System.out.println("Courses are not available to delete at this moment.");
                                }
                                break;
                            case 3:
                                if (!username.equals("Staff Deans office")) {
                                    System.out.println("Error: Access denied. Only staff from Dean's office can edit the course catalog.");
                                    return;
                                }
                                currStatus = CurriculumManager.getLatestCurriculumstatus(conn);
                                if (currStatus == 0) {
                                    admin.addNewCourse(conn);
                                } else {
                                    System.out.println("Courses are not available to add at this moment.");
                                }
                                break;
                            case 4:
                                System.out.println("Enter user ID:");
                                int studentId = scanner.nextInt();
//                            scanner.nextLine();
                                TranscriptGenerator transcriptGenerator = new TranscriptGenerator();
                                transcriptGenerator.generateTranscript(conn, studentId);
                                break;
                            case 5:
                                if (!username.equals("Staff Deans office")) {
                                    System.out.println("Error: Access denied. Only staff from Dean's office can edit the course catalog.");
                                    return;
                                }
                                adn.addNewCurriculum(conn);
                                break;
                            case 6:
                                if (!username.equals("Staff Deans office")) {
                                    System.out.println("Error: Access denied. Only staff from Dean's office can edit the course catalog.");
                                    return;
                                }
                                adn.modifyCurriculumStatus(conn);
                                break;

                            case 7:
                                System.out.println("Logged out successfully.");
                                return;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }
                }
            }

            else {
                System.out.println("Invalid username or password.");
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // Close the database resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    DBconn.closeConnection(conn, stmt);
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            scanner.close();
        }
    }
}



