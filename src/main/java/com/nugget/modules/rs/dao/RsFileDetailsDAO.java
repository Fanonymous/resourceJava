package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.common.utils.R;
import com.nugget.modules.rs.entity.RsFileDetailsEntity;
import com.nugget.modules.rs.entity.RsPreferredTempEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import java.util.List;

/**
 * RsFileDetailsDAO继承基类
 */
@Mapper
public interface RsFileDetailsDAO extends BaseDao<RsFileDetailsEntity> {

    RsFileDetailsEntity queryByResourcesId(@Param("resourcesId") Long resourcesId);
	/**
	 * 设置优选资源
	 * @param preferredList
	 * @return
	 */
	Integer updatePreferred(List<RsPreferredTempEntity> preferredList);

	/**
	 * 重置优选资源
	 * @return
	 */
	Integer resetPreferred();
    /**
     * 热门资源
     * @param map
     * @return
     */
    List<Map<String,Object>> getHotList(Map<String,Object> map);

	/**
	 * 优选
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getPreferred(Map<String,Object> map);

	/**
	 * 推荐资源列表
	 * @return
	 */
	List<Map<String,Object>> getRecommendList(Map<String,Object> map);
	int getRecommendTotal(Map<String,Object> map);
	/**
	 * 推荐资源列表
	 * @return
	 */
	List<Map<String,Object>> getRecommendList1(Map<String,Object> map);
	int getRecommendTotal1(Map<String,Object> map);

    List<Map<String,Object>> getRecommendList2(Map<String,Object> map);
	int getRecommendTotal2(Map<String,Object> map);

	/**
	 * 查询相关资源
	 * @return
	 */
	List<Map<String,Object>> getRelResources(Map<String,Object> map);

	/**
	 * 猜您喜欢
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getMayLikeResources(Map<String,Object> map);

	/**
	 * 资源列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> resourcesList(Map<String,Object> map);

	/**
	 * 资源搜索
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> search(Map<String,Object> map);

	/**
	 * 根据id查资源详情
	 * @param resourcesId
	 * @param rsUserId
	 * @return
	 */
	Map<String,Object> getInfo(@Param("resourcesId") String resourcesId,
							   @Param("rsUserId") String rsUserId);

	int saveMap(Map<String,Object> map);

	int saveMap1(Map<String,Object> map);

	Map<String,Object> getByOrginId(@Param("orginId") String orginId);

	int updateTags(Map<String,Object> map);

	List<String> getTags(Map<String,Object> map);

	List<Map<String,Object>> getK12Resource();


	void updateFileTags(@Param("oldTags") String oldTags,@Param("newTags")  String newTags);
}
