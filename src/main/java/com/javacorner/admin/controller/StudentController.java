package com.javacorner.admin.controller;

import com.javacorner.admin.dto.CourseDTO;
import com.javacorner.admin.dto.StudentDTO;
import com.javacorner.admin.entity.User;
import com.javacorner.admin.service.CourseService;
import com.javacorner.admin.service.StudentService;
import com.javacorner.admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;
    private UserService userService;
    private CourseService courseService;

    public StudentController(StudentService studentService, UserService userService, CourseService courseService) {
        this.studentService = studentService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping
   Page<StudentDTO> searchStudents(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                           @RequestParam(name = "page",defaultValue = "0") int page,
                                           @RequestParam(name = "size",defaultValue = "5") int size){
        return studentService.loadStudentByName(keyword, page, size);
    }

    @DeleteMapping("/{studentId}")
    public void deleteStudent(@PathVariable Long studentId){
        studentService.removeStudent(studentId);
    }

    @PostMapping
    public StudentDTO createStudent(@RequestBody StudentDTO studentDTO){
        User user = userService.loadUserByEmail(studentDTO.getUser().getEmail());
        if(user != null) throw  new RuntimeException("User Email is Existed");
        return studentService.createStudent(studentDTO);
    }

    @PutMapping("/{studentId}")
    public StudentDTO updateStudent(@RequestBody StudentDTO studentDTO, @PathVariable Long studentId){
      studentDTO.setStudentId(studentId);
      return studentService.updateStudent(studentDTO);
    }

    @GetMapping("/{studentId}/courses")
    public Page<CourseDTO> courseByStudentId(@PathVariable Long studentId,
                                             @RequestParam(name = "page",defaultValue = "0") int page,
                                             @RequestParam(name = "size",defaultValue = "5") int size){
        return courseService.fetchCoursesForStudent(studentId, page, size);
    }

    @GetMapping("/{studentId}/other-courses")
    public Page<CourseDTO> nonSubscribedCourses(@PathVariable Long studentId,
                                                @RequestParam(name = "page",defaultValue = "0") int page,
                                                @RequestParam(name = "size",defaultValue = "5") int size){
        return courseService.fetchNonEnrolledInCoursesForStudent(studentId, page, size);

    }

    @GetMapping("/find")
    public StudentDTO loadStudentByEmail(@RequestParam(name = "email",defaultValue = "") String email){
        return studentService.loadStudentByEmail(email);
    }


}
