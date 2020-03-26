package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.entity.RsUrlConfigEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/13 15:45
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Mapper
public interface RsUrlConfigDAO extends BaseDao<RsUrlConfigEntity> {

    RsUrlConfigEntity queryByComeFrom(Map<String,Object> params);
}
