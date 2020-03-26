package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.dto.RespBookRsTagDto;
import com.nugget.modules.rs.dto.RespRsTagDto;
import com.nugget.modules.rs.entity.RsTagEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * RsTagDAO继承基类
 */
@Mapper
public interface RsTagDAO extends BaseDao<RsTagEntity> {
    List<RespRsTagDto> queryByParams(Map<String, Object> param);

    List<RsTagEntity> queryMySubscribe(Map<String, Object> param);

    /**
     * 根据父级查询子级
     * @param parentId
     * @return
     */
    List<Map<String,Object>> getByParentId(Integer parentId);

    /**
     * 根据标签名称查询
     * @param tagName
     * @return
     */
    List<Map<String,Object>> getByTagName(@Param("tagName") String tagName);

    /**
     *	获取标签名称
     * @param tagIds
     * @return
     */
    String getTagsName(@Param("tagIds") String tagIds);

    List<Map<String,Object>> getByType(@Param("tagType") Integer tagType,@Param("tagName") String tagName);

    List<Map<String,Object>> getByLevel(@Param("level") Integer level);

    List<Map<String,Object>> getByPid(@Param("parentId") Integer parentId);

    List<RespBookRsTagDto> queryBookTagByParams(Map<String, Object> param);
}
