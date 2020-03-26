package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.entity.RsUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * RsUserDAO继承基类
 */
@Mapper
public interface RsUserDAO extends BaseDao<RsUserEntity> {

    RsUserEntity queryByParams(Map<String,Object> params);
}