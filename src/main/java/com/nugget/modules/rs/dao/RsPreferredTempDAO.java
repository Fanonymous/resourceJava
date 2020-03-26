package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.entity.RsPreferredTempEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/13 11:54
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Mapper
public interface RsPreferredTempDAO extends BaseDao<RsPreferredTempEntity> {


    void deleteAllData();
}
