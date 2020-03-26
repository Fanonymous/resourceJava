package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.entity.RsApplicationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * RsApplicationDAO继承基类
 */
@Mapper
public interface RsApplicationDAO extends BaseDao<RsApplicationEntity> {
	/**
	 * 查询应用中心
	 * @return
	 */
	List<Map<String,Object>> getUserApp(Map<String,Object> map);



}