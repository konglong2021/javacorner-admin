package com.javacorner.admin.runner;

import com.javacorner.admin.dto.CourseDTO;
import com.javacorner.admin.dto.InstructorDTO;
import com.javacorner.admin.dto.StudentDTO;
import com.javacorner.admin.dto.UserDTO;
import com.javacorner.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Autowired
    private InstructorService instructorService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        createAdmin();
        createInstructors();
        createCourses();
        StudentDTO student = createStudent();
        assignCourseToStudent(student);
    }


    private void createRoles() {
        Arrays.asList("Admin","Instructor","Student").forEach(role -> roleService.createRole(role));
    }
    private void createAdmin() {
        userService.createUser("admin@gmail.com","password");
        userService.assignRoleToUser("admin@gmail.com","Admin");
    }

    private void createInstructors() {
        //create 10 instructors
        for (int i = 0; i<10 ;i++){
            InstructorDTO instructorDTO = new InstructorDTO();
            instructorDTO.setFirstName("instructor"+i+"FN");
            instructorDTO.setLastName("instructor"+i+"LN");
            instructorDTO.setSummary("master"+i);
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail("instructor"+i+"@gmail.com");
            userDTO.setPassword("password");
            instructorDTO.setUser(userDTO);
            instructorService.createInstructor(instructorDTO);
        }
    }

    private void createCourses() {
        //create 20 courses
        for (int i = 0; i < 20 ; i++){
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setCourseName("Java Course"+i);
            courseDTO.setCourseDescription("It About Java Course"+i);
            courseDTO.setCourseDuration(i+"Hours");
            InstructorDTO instructorDTO = new InstructorDTO();
            //Random number between 1 - 9
            Random random = new Random();
            int randomNumber = random.nextInt(9) + 1;
            instructorDTO.setInstructorId(randomNumber);
            courseDTO.setInstructor(instructorDTO);
            courseService.createCourse(courseDTO);
        }
    }

    private StudentDTO createStudent() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("studentFN");
        studentDTO.setLastName("studentLN");
        studentDTO.setLevel("intermediate");
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("student@gmail.com");
        userDTO.setPassword("password");
        studentDTO.setUser(userDTO);
        return studentService.createStudent(studentDTO);
    }


    private void assignCourseToStudent(StudentDTO student) {
        Random random = new Random();
        courseService.assignStudentToCourse(random.nextLong(20) + 1,student.getStudentId());
    }

}
