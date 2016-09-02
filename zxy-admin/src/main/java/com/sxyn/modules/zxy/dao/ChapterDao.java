/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.sxyn.modules.zxy.dao;

import java.util.List;

import com.sxyn.common.persistence.CrudDao;
import com.sxyn.common.persistence.annotation.MyBatisDao;
import com.sxyn.modules.zxy.entity.Chapter;

/**
 * 小节管理DAO接口
 * @author sheungxin
 * @version 2016-05-29
 */
@MyBatisDao
public interface ChapterDao extends CrudDao<Chapter> {
	
	public Chapter getByName(Chapter chapter);
	
	public List<Chapter> getByObjId(Chapter chapter);
}