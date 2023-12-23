package org.gfg.minor1.service;

import org.gfg.minor1.models.*;
import org.gfg.minor1.repository.StudentRepository;
import org.gfg.minor1.repository.UserRepository;
import org.gfg.minor1.request.CreateStudentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${student.authority}")
    private String studentAuthority;

    public List<Student> findStudent(StudentFilterType studentFilterType, String value, OperationType operationType){
        switch(operationType){
            case EQUALS :
                switch (studentFilterType){
                    case EMAIL :
                        return studentRepository.findByEmail(value);
                    case CONTACT:
                        return studentRepository.findByContact(value);
                }
            default:
                return new ArrayList<>();
        }
    }

    public Student create(CreateStudentRequest createStudentRequest){
        // check if the student already exist
        List<Student> students = findStudent(StudentFilterType.CONTACT,createStudentRequest.getContact(),OperationType.EQUALS);
        if(students == null || students.isEmpty()){
            // save it
            Student student = createStudentRequest.to();
            User user = User.builder().
                    contact(student.getContact()).
                    password(passwordEncoder.encode(createStudentRequest.getPassword())).
                    authorities(studentAuthority).
                    build();
            user = userRepository.save(user);
            student.setUser(user);

            return studentRepository.save(student);
        }
        return students.get(0);
    }
}
