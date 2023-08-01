package com.javacorner.admin.controller;

import com.javacorner.admin.dto.CourseDTO;
import com.javacorner.admin.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Page<CourseDTO> searchCourses(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                         @RequestParam(name = "page",defaultValue = "0") int page,
                                         @RequestParam(name = "size",defaultValue = "5") int size){
        return courseService.findCourseByCourseName(keyword,page,size);

    }

    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable Long courseId){
        courseService.removeCourse(courseId);
    }
    @PostMapping
    public CourseDTO createCourse(@RequestBody CourseDTO courseDTO){
        return courseService.createCourse(courseDTO);
    }

    @PutMapping("/{courseId}")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO,@PathVariable Long courseId){
        courseDTO.setCourseId(courseId);
        return courseService.updateCourse(courseDTO);
    }

    @PostMapping("/{courseId}/enroll/students/{studentId}")
    public void enrollStudentToCourse(@PathVariable Long courseId,@PathVariable Long studentId){
        courseService.assignStudentToCourse(courseId,studentId);
    }
}
