package com.panda.oracle.service.impl;

import com.panda.oracle.dao.StudentDao;
import com.panda.oracle.model.Student;
import com.panda.oracle.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GuoShunFa
 **/
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public List<Student> getAllStudent() {
        return studentDao.getAllStudent();
    }
}
