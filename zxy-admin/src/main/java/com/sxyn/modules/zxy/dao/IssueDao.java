/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.sxyn.modules.zxy.dao;

import com.sxyn.common.persistence.CrudDao;
import com.sxyn.common.persistence.annotation.MyBatisDao;
import com.sxyn.modules.zxy.entity.Issue;

/**
 * 期次管理DAO接口
 * @author sheungxin
 * @version 2016-06-16
 */
@MyBatisDao
public interface IssueDao extends CrudDao<Issue> {
	
	public Issue checkExist(Issue issue);
	
}