package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.entity.RsDictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.omg.CORBA.OBJ_ADAPTER;

import java.util.List;
import java.util.Map;

/**
 * RsDictDAO继承基类
 */
@Mapper
public interface RsDictDAO extends BaseDao<RsDictEntity> {

	List<Map<String,Object>> getDict(Map<String, Object> map);
}