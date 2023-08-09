package com.javacorner.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class JavacornerAdminApplication{
    public static void main(String[] args) {
        SpringApplication.run(JavacornerAdminApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Override
//    public void run(String... args) throws Exception {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        boolean passwordMatches = passwordEncoder.matches("password", "$2y$10$MGlzqKw3MMEW8E3YiQahw.utZ2ArFofg7V7atz70zinb2ZbY8VPCC");
//
//        if (passwordMatches) {
//            System.out.println("Success password matched");
//        } else {
//            System.out.println("password not matched");
//        }
//    }
}
