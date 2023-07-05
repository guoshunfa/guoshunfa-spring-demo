package com.panda.oracle;

import com.panda.oracle.model.Student;
import com.panda.oracle.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.util.List;

@SpringBootApplication
public class PandaDatabaseOracleSimpleApplication implements CommandLineRunner {

    @Autowired
    StudentService studentService;

    public static void main(String[] args) {
        SpringApplication.run(PandaDatabaseOracleSimpleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Student> allStudent =
                studentService.getAllStudent();
        System.out.println();
    }
}
