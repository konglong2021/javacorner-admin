package com.javacorner.admin.service.impl;

import com.javacorner.admin.dao.CourseDao;
import com.javacorner.admin.dao.InstructorDao;
import com.javacorner.admin.dao.StudentDao;
import com.javacorner.admin.dto.CourseDTO;
import com.javacorner.admin.entity.Course;
import com.javacorner.admin.entity.Instructor;
import com.javacorner.admin.entity.Student;
import com.javacorner.admin.mapper.CourseMapper;
import com.javacorner.admin.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Transactional
@Service
public class CourseServiceImpl implements CourseService {

    private CourseDao courseDao;
    private CourseMapper courseMapper;
    private InstructorDao instructorDao;
    private StudentDao studentDao;

    public CourseServiceImpl(CourseDao courseDao,CourseMapper courseMapper,InstructorDao instructorDao,StudentDao studentDao){
        this.courseDao = courseDao;
        this.courseMapper = courseMapper;
        this.instructorDao = instructorDao;
        this.studentDao = studentDao;

    }

    @Override
    public Course loadCourseById(Long courseId) {
        return courseDao.findById(courseId).orElseThrow(()->new EntityNotFoundException("Course with ID" + courseId + "Not Found!"));
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseMapper.fromCourseDTO(courseDTO);
        Instructor instructor = instructorDao.findById(courseDTO.getInstructor().getInstructorId()).orElseThrow(()->new EntityNotFoundException("Instructor with ID "+ courseDTO.getInstructor().getInstructorId()+"Not Found!"));
        course.setInstructor(instructor);
        Course saveCourse = courseDao.save(course);
        return courseMapper.fromCourse(saveCourse);
    }


    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        Course loadCourse = loadCourseById(courseDTO.getCourseId());
//        Instructor instructor = instructorDao.findById(courseDTO.getInstructor().getInstructorId()).orElseThrow(()->new EntityNotFoundException("Instructor with ID "+ courseDTO.getInstructor().getInstructorId()+"Not Found!"));
        Course course = courseMapper.fromCourseDTO(courseDTO);
        course.setInstructor(loadCourse.getInstructor());
        Course updateCourse = courseDao.save(course);
        return courseMapper.fromCourse(updateCourse);
    }

    @Override
    public Page<CourseDTO> findCourseByCourseName(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Course> coursePage = courseDao.findCourseByCourseNameContains(keyword,pageRequest);
        return new PageImpl<>(coursePage.getContent().stream().map(course -> courseMapper.fromCourse(course)).collect(Collectors.toList()),pageRequest,coursePage.getTotalElements());
    }

    @Override
    public void assignStudentToCourse(Long courseId, Long studentId) {
        Student student = studentDao.findById(studentId).orElseThrow(()->new EntityNotFoundException("Student with ID "+ studentId + "Not Found!"));
        Course course = courseDao.findById(courseId).orElseThrow(()->new EntityNotFoundException("Course with ID "+ courseId + "Not Found!"));
        course.assignStudentToCourse(student);
    }

    @Override
    public Page<CourseDTO> fetchCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Course> studentCoursesPage = courseDao.getCoursesByStudentId(studentId,pageRequest);
        return new PageImpl<>(studentCoursesPage.getContent().stream().map(course -> courseMapper.fromCourse(course)).collect(Collectors.toList()),pageRequest,studentCoursesPage.getTotalElements());
    }

    @Override
    public Page<CourseDTO> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Course> nonEnrolledInCoursePage = courseDao.getNonEnrolledInCoursesByStudentId(studentId,pageRequest);
        return new PageImpl<>(nonEnrolledInCoursePage.getContent().stream().map(course -> courseMapper.fromCourse(course)).collect(Collectors.toList()),pageRequest,nonEnrolledInCoursePage.getTotalElements());
    }

    @Override
    public void removeCourse(Long courseId) {
        courseDao.deleteById(courseId);

    }

    @Override
    public Page<CourseDTO> fetchCourseForInstructor(Long instructorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Course> coursesByInstructorPage = courseDao.getCoursesByInstructorId(instructorId,pageRequest);
        return new PageImpl<>(coursesByInstructorPage.getContent().stream().map(course -> courseMapper.fromCourse(course)).collect(Collectors.toList()),pageRequest,coursesByInstructorPage.getTotalElements());
    }
}
