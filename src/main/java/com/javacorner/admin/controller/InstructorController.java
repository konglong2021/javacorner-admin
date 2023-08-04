package com.javacorner.admin.controller;

import com.javacorner.admin.dto.CourseDTO;
import com.javacorner.admin.dto.InstructorDTO;
import com.javacorner.admin.entity.User;
import com.javacorner.admin.service.CourseService;
import com.javacorner.admin.service.InstructorService;
import com.javacorner.admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("instructors")
@CrossOrigin("*")
public class InstructorController {

    private InstructorService instructorService;
    private UserService userService;
    private CourseService courseService;

    public InstructorController(InstructorService instructorService, UserService userService, CourseService courseService) {
        this.instructorService = instructorService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping
    public Page<InstructorDTO> searchInstructors(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                                 @RequestParam(name = "page",defaultValue = "0") int page,
                                                 @RequestParam(name = "size",defaultValue = "5")int size){
        return instructorService.findInstructorByName(keyword, page, size);
    }

    @DeleteMapping("/{instructorId}")
    public void deleteInstructor(@PathVariable Long instructorId){
        instructorService.removeInstructorById(instructorId);
    }

    @PostMapping
    public InstructorDTO createInstructor(@RequestBody InstructorDTO instructorDTO){
        User user = userService.loadUserByEmail(instructorDTO.getUser().getEmail());
        if (user != null)throw new RuntimeException("Email Already Exist");
        return instructorService.createInstructor(instructorDTO);
    }

    @PutMapping("/{instructorId}")
    public InstructorDTO updateInstructor(@RequestBody InstructorDTO instructorDTO,@PathVariable Long instructorId){
        instructorDTO.setInstructorId(instructorId);
        return instructorService.updateInstructor(instructorDTO);
    }

    @GetMapping("/all")
    public List<InstructorDTO> getAllInstructor()
    {
        return instructorService.fetchInstructors();
    }

    @GetMapping("/{instructorId}/courses")
    public Page<CourseDTO> courseByInstructorId(@PathVariable Long instructorId,
                                                @RequestParam(name = "page",defaultValue = "0") int page,
                                                @RequestParam(name = "size",defaultValue = "5") int size){
        return courseService.fetchCourseForInstructor(instructorId, page, size);
    }

    @GetMapping("/find")
    public InstructorDTO findInstructorByEmail(@RequestParam(name = "email",defaultValue = "")String email){
        return instructorService.loadInstructorByEmail(email);
    }
}
