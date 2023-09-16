package org.example;

import java.sql.*;

public class gradcheck {

    // method to check graduation status of a student
    public static void checkGraduationStatus(Connection conn, int student_id) {

        try {
            // prepare statement to get student details
            String sql = "SELECT entry_year, branch FROM students WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, student_id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int entry_year = rs.getInt("entry_year");
            String branch = rs.getString("branch");

            // prepare statement to get list of all courses for the program
            sql = "SELECT course_id, course_type FROM course_tags WHERE entry_year = ? AND branch = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,entry_year);
            pstmt.setString(2, branch);
            rs = pstmt.executeQuery();

            // initialize counters for program core, program elective, and btp capstone
            int program_core_count = 0;
            int program_elective_count = 0;
            int btp_capstone_count = 0;

            // loop through all courses and check student's grades
            while (rs.next()) {
                int course_id = rs.getInt("course_id");
                String course_type = rs.getString("course_type");

                sql = "SELECT credits FROM student_course_reg " +
                        "JOIN course_off ON student_course_reg.off_id = course_off.off_id " +
                        "WHERE student_course_reg.student_id = ? " +
                        "AND course_off.course_id = ? ";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, student_id);
                pstmt.setInt(2, course_id);
                ResultSet rs2 = pstmt.executeQuery();

                // calculate credits for the course
                int credits = 0;
                while (rs2.next()) {
                    credits += rs2.getInt("credits");
                }
                // prepare statement to get grades for the course
                sql = "SELECT grade FROM grades " +
                        "JOIN student_course_reg ON grades.reg_id = student_course_reg.reg_id " +
                        "JOIN course_off ON student_course_reg.off_id = course_off.off_id " +
                        "WHERE student_course_reg.student_id = ? " +
                        "AND course_off.course_id = ? " +
//                      "AND course_off.semester_year <= ? " +
                        "AND grades.grade IS NOT NULL " +
                        "AND grades.grade <= 'D'";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, student_id);
                pstmt.setInt(2, course_id);
//                pstmt.setInt(3, entry_year + 4); // only count courses taken in last 8 semesters
                rs2 = pstmt.executeQuery();

                // check if course is a program core course
                if (course_type.equals("Program Core")) {
                    if (rs2.next()) {
                        program_core_count+= credits;
                    }
                }

                // check if course is a program elective course
                if (course_type.equals("Program Elective")) {
                    if (rs2.next()) {
                        program_elective_count+= credits;
                    }
                }

                // check if course is the BTP Capstone course
                if (course_type.equals("BTP Capstone")) {
                    if (rs2.next()) {
                        btp_capstone_count+= credits;
                    }
                }
            }

            // check if student has completed all program core courses
            boolean has_completed_program_core = (program_core_count >= 80);

            // check if student has completed minimum number of program elective courses
            boolean has_completed_program_elective = (program_elective_count >= 60);

            // check if student has passed BTP Capstone course
            boolean has_passed_btp_capstone = (btp_capstone_count >=6);

            // print graduation status
            if (has_completed_program_core && has_completed_program_elective && has_passed_btp_capstone) {
                System.out.println("Congratulations! You have graduated from the program.");
            } else {
                System.out.println("Sorry, you have not yet fulfilled all graduation requirements.");
                System.out.println("You still require "+(80-program_core_count)+" program core credits"+(60-program_elective_count)+" program elective credits"+(6-btp_capstone_count)+" btp credits");
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());}
//            return false;
//        } finally {
//            // Close the database resources
//            try {
//                if (rs2 != null) {
//                    rs2.close();
//                }
//                if (rs != null) {
//                    rs.close();
//                }
//                if (ps != null) {
//                    ps.close();
//                }
////            } catch (SQLException e) {
////                System.err.println("SQLException: " + e.getMessage());
////            }
    }
}

