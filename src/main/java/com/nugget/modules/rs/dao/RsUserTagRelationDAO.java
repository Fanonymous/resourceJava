package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.entity.RsUserTagRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * RsUserTagRelativityDAO继承基类
 */
@Mapper
public interface RsUserTagRelationDAO extends BaseDao<RsUserTagRelationEntity> {

    void cancelUserSubscribe(Map<String, Object> param);

    /**订阅标签id
     * 获取
     * @param rsUserId
     * @return
     */
    List<String> getUserSubId(@Param("rsUserId") String rsUserId);

    /**
     * 删除之前订阅的标签
     * @param param
     */
    void deleteByFirstTagId(Map<String, Object> param);

    String getAllTagIdByRsUserId(String rsUserId);
}
