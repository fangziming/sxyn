/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.sxyn.modules.zxy.dao;

import com.sxyn.common.persistence.CrudDao;
import com.sxyn.common.persistence.annotation.MyBatisDao;
import com.sxyn.modules.zxy.entity.Vedio;

/**
 * 视频管理DAO接口
 * @author sheungxin
 * @version 2016-06-06
 */
@MyBatisDao
public interface VedioDao extends CrudDao<Vedio> {
	
	public Vedio getByName(Vedio vedio);
	
	public Vedio getByUrl(Vedio vedio);
	
	public void deleteAll();
	
}