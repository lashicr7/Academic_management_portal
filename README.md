# Academic_management_portal

This project consist of 3 logins:student,instructor,admin
when we enter the username,password we'll be opened with authentication and then role extraction.
if role==student,
we will have following functionalities:
1. Display available courses:
    Here,all the courses available to register will be displayed(course_off table)
2. Register for a course:
     We can enter the id of course which we weant to register for
3. De-register for a course:
     We can enter the id of the course which we want to deregister for
4. Display courses registered:
     Here we can see all the courses that we have registered
5. Display grades assigned:
     Using this,we can see all the courses for which grades are assigned
6. compute current CGPA:
     With the help of this,we can compute the current cgpa
7. Edit your profile:
     After entering this,we will be provided by two options:edit phonenumber,edit address
8. Check graduation eligibility:
     This will the course completion in different types and says eligible/not with providing difference
9. Logout:
     With this,we can comeout of the loop which asks repetitively for choice.

if role==instructor,
we will have following functionalities:
1. Add a new offering:
    Here,all course available in course catalogue will be displayed and we can enter course_id to add that in offerings
2. Delete a offering:
    With the help of this,we can delete the offerings which we have provided.
3. Assign grades:
   Using this,we can assign grades to students using csv file,and id of the course
4. Logout:
    Using this,we can comeout of the loop which asks repetitively for choice.

if role==admin,
we will have following functionalities:
1. View course catalog:
    This will display the current available course catalogue
2.Delete course from catalog:
    This helps in removing the course from catalogue.
    Note this can only be done by username:Staff Deans office
3.Add new course:
    Using this,we can add a new course in catalog.
    Note this can only be done by username:Staff Deans office
4. Generate transcript:
    This helps in generating transcript for a student with id.
5. Add new curriculum:
     With the help of this we can add new curriculum.
     Note this can only be done by username:Staff Deans office
6. Modify curriculum status:
     Using this,we can modify the status of current running semester.
     Note this can only be done by username:Staff Deans office.
7. Logout:
     With the help of this,We can comeout of the loop which asks repetitively for choice.

Curriculum:
To avoid parallel updates,deletes I have used curriculum table which decides whether course can be added/offered/graded..
It has parameters:id,semester,status(0-4)
0: Beginning of new semester. Courses can be floated in the course_dis table, but not able to be offeref by proffesors.
1: Course float period. Courses can be floated in the course_off table, but not registered for in the student_course_reg table.
2: Registration period. Courses can be registered/deregistered for in the student_course_reg table, but not floated in the course_off table.
3: Locked period. No changes can be made to courses. Only grades can be assigned.

File descriptions:
test_reports have jacoco reports,
seproj has code of project,junit
tables.txt is the code to run in postgreSQL
