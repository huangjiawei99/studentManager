package com.yuan.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuan.bean.Student;
import com.yuan.dao.StudentDao;
import com.yuan.service.StudentService;

/**
 * 学生
 * @author hjw
 *
 */
@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao studentDao;
	
	@Override
	public int add(Student student) {
		return studentDao.add(student);
	}

	@Override
	public int edit(Student student) {
		return studentDao.edit(student);
	}

	@Override
	public int delete(String ids) {
		return studentDao.delete(ids);
	}

	@Override
	public List<Student> findList(Map<String, Object> queryMap) {
		return studentDao.findList(queryMap);
	}

	@Override
	public List<Student> findAll() {
		return studentDao.findAll();
	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		return studentDao.getTotal(queryMap);
	}

	@Override
	public Student findByUserName(String username) {
		return studentDao.findByUserName(username);
	}

}
