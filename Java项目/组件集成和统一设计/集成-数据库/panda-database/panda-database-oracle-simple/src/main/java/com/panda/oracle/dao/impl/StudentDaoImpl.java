package com.panda.oracle.dao.impl;

import com.panda.oracle.dao.StudentDao;
import com.panda.oracle.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author GuoShunFa
 **/
@Repository
public class StudentDaoImpl extends JdbcDaoSupport implements StudentDao {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        setDataSource(dataSource);
        System.out.println("Datasource used: " + dataSource);
    }

    @Override
    public List<Student> getAllStudent() {
        final String sql = "SELECT * FROM panda.student";
        return getJdbcTemplate().query(sql,
                (rs, rowNum) -> new Student(rs.getString("id"),
                        rs.getString("name")));
    }

}
