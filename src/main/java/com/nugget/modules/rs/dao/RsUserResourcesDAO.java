package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.dto.UserResourcesDto;
import com.nugget.modules.rs.entity.RsUserResourcesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * RsUserResourcesDAO继承基类
 */
@Mapper
public interface RsUserResourcesDAO extends BaseDao<RsUserResourcesEntity> {

    Integer queryTotalByMap(Map<String, Object> params);

    RsUserResourcesEntity queryByMap(Map<String, Object> queryMap);

    List<UserResourcesDto> getUserResources(Map<String, Object> params);

    int queryUserResourcesTotal(Map<String, Object> params);

    /**
     * 查询访问最多资源的资源标签
     * @param rsUserId
     * @return
     */
    List<String> mostViewTagList(String rsUserId);
}
