package com.javacorner.admin.service.impl;

import com.javacorner.admin.dao.StudentDao;
import com.javacorner.admin.dto.StudentDTO;
import com.javacorner.admin.entity.Course;
import com.javacorner.admin.entity.Student;
import com.javacorner.admin.entity.User;
import com.javacorner.admin.mapper.StudentMapper;
import com.javacorner.admin.service.StudentService;
import com.javacorner.admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.stream.Collectors;

@Transactional
@Service
public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;
    private StudentMapper studentMapper;
    private UserService userService;
    public StudentServiceImpl(StudentDao studentDao , StudentMapper studentMapper,UserService userService){
        this.studentDao = studentDao;
        this.studentMapper = studentMapper;
        this.userService = userService;
    }

    @Override
    public Student loadStudentById(Long studentId) {
        return studentDao.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student with ID " + studentId + "Not Found!"));
    }

    @Override
    public Page<StudentDTO> loadStudentByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Student> studentsByNamePage = studentDao.findStudentsByName(name,pageRequest);
        return new PageImpl<>(studentsByNamePage.getContent().stream().map(student -> studentMapper.fromStudent(student)).collect(Collectors.toList()),pageRequest,studentsByNamePage.getTotalElements());
    }

    @Override
    public StudentDTO loadStudentByEmail(String email) {
        return studentMapper.fromStudent(studentDao.findStudentByEmail(email));
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
       //create user
        User user = userService.createUser(studentDTO.getUser().getEmail(),studentDTO.getUser().getPassword());
        // assign user
        userService.assignRoleToUser(user.getEmail(),"Student");
        //create student
        Student student = studentMapper.fromStudentDTO(studentDTO);
        student.setUser(user);
        Student saveStudent = studentDao.save(student);
        return studentMapper.fromStudent(saveStudent);
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        // find current student
        Student loadStudent = loadStudentById(studentDTO.getStudentId());
        // pass DTO
        Student student = studentMapper.fromStudentDTO(studentDTO);
        // update old user;
        student.setUser(loadStudent.getUser());
        student.setCourses(loadStudent.getCourses());
        Student updateStudent = studentDao.save(student);
        return studentMapper.fromStudent(updateStudent);
    }

    @Override
    public void removeStudent(Long studentId) {
        Student loadStudent = loadStudentById(studentId);
        Iterator<Course> courseIterator = loadStudent.getCourses().iterator();
        if (courseIterator.hasNext()){
            Course course = courseIterator.next();
            course.removeStudentFromCourse(loadStudent);
        }
        studentDao.deleteById(studentId);
    }
}
