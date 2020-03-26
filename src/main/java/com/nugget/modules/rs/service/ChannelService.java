package com.nugget.modules.rs.service;

import com.nugget.common.utils.R;

import java.util.List;
import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/11
 */
public interface ChannelService {

	/**
	 * 频道列表
	 * @return
	 */
	R channelList(Map<String,Object> param);

	/**
	 * 频道添加/更新
	 * @return
	 */
	R saveOrUpdate(Map<String,Object> param);


	/**
	 * 频道推荐列表
	 * @return
	 */
	R hotChannelList(Map<String,Object> param);

	/**
	 * 章节树形结构
	 * @return
	 */
	R tagListTree(String keywords,Integer tagId);

	/**
	 * 导航栏
	 * @param param
	 * @return
	 */
	R navigationNar(Map<String,Object> param);

	/**
	 * 查询字典表
	 * @param map
	 * @return
	 */
	R getDict(Map<String,Object> map);

	/**
	 * 修改排序
	 * @param mapList
	 * @return
	 */
	R changeOrder(List<Map<String, Object>> mapList);
}
