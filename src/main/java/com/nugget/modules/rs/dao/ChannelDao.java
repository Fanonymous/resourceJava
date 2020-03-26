package com.nugget.modules.rs.dao;

import com.nugget.common.dao.BaseDao;
import com.nugget.modules.rs.entity.RsChannelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/11
 */
@Mapper
public interface ChannelDao extends BaseDao<RsChannelEntity> {

	/**
	 * 查询频道列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getUserChannel(Map<String,Object> map);
	/**
	 * 删除
	 * @param reUserId
	 * @return
	 */
	int del(String reUserId);
	/**
	 * 添加
	 * @param map
	 * @return
	 */
	int saveBatch(Map<String,Object> map);

	/**
	 * 推荐频道
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> hotChannelList(Map<String,Object> map);

	/**
	 * 查询二级频道
	 * @param channelId
	 * @return
	 */
	List<Map<String,Object>> getSecondTag(Integer channelId);


	int updateOrder(@Param("list") List<Map<String,Object>> list);

	/**
	 * 批量保存
	 * @param map
	 */
	void saveListData(Map<String, Object> map);


	Map<String,Object> getByName(@Param("channelName") String channelNae);
}
