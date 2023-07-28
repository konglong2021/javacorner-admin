package com.javacorner.admin;

import com.javacorner.admin.dao.*;
import com.javacorner.admin.utility.OperationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavacornerAdminApplication implements CommandLineRunner {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private InstructorDao instructorDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private RoleDao roleDao;
    public static void main(String[] args) {
        SpringApplication.run(JavacornerAdminApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        OperationUtility.usersOperations(userDao);
//        OperationUtility.rolesOperations(roleDao);
//        OperationUtility.assignRoleToUser(userDao,roleDao);
//        OperationUtility.instructorOperations(userDao,instructorDao,roleDao);
        OperationUtility.studentsOperations(userDao,studentDao,roleDao);
    }
}
