package com.javacorner.admin.controller;

import com.javacorner.admin.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public boolean checkIfEmailExists(@RequestParam(name = "email",defaultValue = "") String email){
        return userService.loadUserByEmail(email) != null;
    }
}
