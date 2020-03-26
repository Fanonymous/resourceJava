package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.entity.RsSourceEntiry;
import org.apache.ibatis.annotations.Mapper;
/**
 * RsSourceDAO继承基类
 */
@Mapper
public interface RsSourceDAO extends BaseDao<RsSourceEntiry> {
}