package com.javacorner.admin.utility;

import com.javacorner.admin.dao.*;
import com.javacorner.admin.entity.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public class OperationUtility {

    public static void  usersOperations(UserDao userDao){
        createUsers(userDao);
//        updateUser(userDao);
//        deleteUser(userDao);
//        fetchUsers(userDao);
    }

    public static void rolesOperations(RoleDao roleDao){
        createRoles(roleDao);
//        updateRole(roleDao);
//        deleteRole(roleDao);
//        fetchRoles(roleDao);
    }

    public static void assignRoleToUser(UserDao userDao,RoleDao roleDao){
        Role role = roleDao.findByName("Admin");
        if(role==null) throw new EntityNotFoundException("Role Not Found");
        List<User> users = userDao.findAll();
        users.forEach(user -> {
            user.assignRoleToUser(role);
            userDao.save(user);
        });
    }



    private static void createUsers(UserDao userDao){
        User user1 = new User("user1@gmail.com","password");
        userDao.save(user1);
        User user2 = new User("user2@gmail.com","password");
        userDao.save(user2);
        User user3 = new User("user3@gmail.com","password");
        userDao.save(user3);
        User user4 = new User("user4@gmail.com","password");
        userDao.save(user4);
    }

    private static void updateUser(UserDao userDao){
        User user = userDao.findById(2L).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        user.setEmail("newEmail@gmail.com");
        userDao.save(user);
    }

    private static void deleteUser(UserDao userDao){
        User user = userDao.findById(3L).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        userDao.delete(user);
    }

    private static void fetchUsers(UserDao userDao){
        userDao.findAll().forEach(user -> System.out.println(user.toString()));
    }

    private static void fetchRoles(RoleDao roleDao) {
        roleDao.findAll().forEach(role -> System.out.println(role.toString()));
    }

    private static void deleteRole(RoleDao roleDao) {
        roleDao.deleteById(2L);
    }

    private static void updateRole(RoleDao roleDao) {
        Role role = roleDao.findById(1L).orElseThrow(()->new EntityNotFoundException("Role Not Found"));
        role.setName("newAdmin");
        roleDao.save(role);
    }

    private static void createRoles(RoleDao roleDao) {
        Role role = new Role("Admin");
        roleDao.save(role);
        Role role2 = new Role("Instructor");
        roleDao.save(role2);
        Role role3 = new Role("Student");
        roleDao.save(role3);
    }

    public static void instructorOperations(UserDao userDao, InstructorDao instructorDao,RoleDao roleDao){
//        createInstructors(userDao,instructorDao,roleDao);
//        updateInstructor(instructorDao);
//        deleteInstructor(instructorDao);
//        fetchInstructor(instructorDao);
    }


    private static void createInstructors(UserDao userDao, InstructorDao instructorDao, RoleDao roleDao) {
        Role role = roleDao.findByName("Instructor");
        if(role==null) throw new EntityNotFoundException("Role Not Found");

        User user1 = new User("instructorUser1@gmail.com","password");
        user1.assignRoleToUser(role);
        userDao.save(user1);

        Instructor instructor = new Instructor("instructoreFN","instructorLN","Experience instructor",user1);
        instructorDao.save(instructor);

        User user2 = new User("instructorUser2@gmail.com","password");
        user2.assignRoleToUser(role);
        userDao.save(user2);

        Instructor instructor2 = new Instructor("instructoreFN2","instructorLN2","Experience instructor2",user2);
        instructorDao.save(instructor2);
    }

    private static void updateInstructor(InstructorDao instructorDao){
        Instructor instructor = instructorDao.findById(1L).orElseThrow(()-> new EntityNotFoundException("Instructor not found"));
        instructor.setSummary("Certified Instructor");
        instructorDao.save(instructor);
    }

    private static void deleteInstructor(InstructorDao instructorDao) {
        instructorDao.deleteById(2L);
    }

    private static void fetchInstructor(InstructorDao instructorDao) {
        instructorDao.findAll().forEach(instructor -> System.out.println(instructor.toString()));
    }

    public static void studentsOperations(UserDao userDao,StudentDao studentDao,RoleDao roleDao){
//        createStudents(userDao,studentDao,roleDao);
//        updateStudent(studentDao);
//        deleteStudent(studentDao);
        fetchStudents(studentDao);
    }

    private static void createStudents(UserDao userDao, StudentDao studentDao, RoleDao roleDao) {
        Role role = roleDao.findByName("Student");
        if(role==null) throw new EntityNotFoundException("Role Not Found");
        User user = new User("stdUser@gmail.com","password");
        user.assignRoleToUser(role);
        userDao.save(user);
        Student student = new Student("stdUserFN","stdUserLn","master",user);
        studentDao.save(student);

        User user2 = new User("stdUser2@gmail.com","password");
        user2.assignRoleToUser(role);
        userDao.save(user2);
        Student student2 = new Student("stdUserFN2","stdUserLn2","Phd",user);
        studentDao.save(student2);
    }

    private static void updateStudent(StudentDao studentDao) {
        Student student = studentDao.findById(1L).orElseThrow(()->new EntityNotFoundException("Student Not Found"));
        student.setLevel("Minion");
        studentDao.save(student);
    }

    private static void deleteStudent(StudentDao studentDao) {
        studentDao.deleteById(1L);
    }

    private static void fetchStudents(StudentDao studentDao) {
        studentDao.findAll().forEach(student -> System.out.println(student.toString()));
    }

    public static void courseOperations(CourseDao courseDao, InstructorDao instructorDao, StudentDao studentDao){
        createCourses(courseDao,instructorDao);
        updateCourse(courseDao);
        deleteCourse(courseDao);
        fetchCourses(courseDao);
        assignStudentToCourse(courseDao,studentDao);
        fetchCoursesForStudent(courseDao);
    }

    private static void createCourses(CourseDao courseDao, InstructorDao instructorDao) {
        Instructor instructor = instructorDao.findById(1L).orElseThrow(()->new EntityNotFoundException("Instructor Not Found"));
        Course course1 = new Course("Hibernete","5 Hours","Instroduction to Hibernete",instructor);
        courseDao.save(course1);

        Course course2 = new Course("Spring Data JPA","10 Hours","Master Spring Data JPA",instructor);
        courseDao.save(course2);
    }

    private static void updateCourse(CourseDao courseDao) {
        Course course = courseDao.findById(1L).orElseThrow(()->new EntityNotFoundException("Course Not Found"));
        course.setCourseDuration("10 Hours");
        courseDao.save(course);
    }

    private static void deleteCourse(CourseDao courseDao) {
        courseDao.deleteById(2L);
    }

    private static void fetchCourses(CourseDao courseDao) {
        courseDao.findAll().forEach(course -> System.out.println(course.toString()));
    }

    private static void assignStudentToCourse(CourseDao courseDao, StudentDao studentDao) {
        Optional<Student> student1 = studentDao.findById(1L);
        Optional<Student> student2 = studentDao.findById(2L);

        Course course = courseDao.findById(1L).orElseThrow(()->new EntityNotFoundException("Course Not Found"));
        student1.ifPresent(course::assignStudentToCourse);
        student2.ifPresent(course::assignStudentToCourse);
        courseDao.save(course);
    }

    private static void fetchCoursesForStudent(CourseDao courseDao) {
        courseDao.getCoursesByStudentId(1L).forEach(course -> System.out.println(course.toString()));
    }

}
