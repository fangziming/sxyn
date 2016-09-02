/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.sxyn.modules.zxy.dao;

import java.util.List;

import com.sxyn.common.persistence.CrudDao;
import com.sxyn.common.persistence.annotation.MyBatisDao;
import com.sxyn.modules.zxy.entity.Course;

/**
 * 课程管理DAO接口
 * @author sheungxin
 * @version 2016-05-29
 */
@MyBatisDao
public interface CourseDao extends CrudDao<Course> {
	
	public List<Course> getByName(Course course);
	
}