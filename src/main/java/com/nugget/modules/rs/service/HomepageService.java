package com.nugget.modules.rs.service;

import com.nugget.common.utils.R;

import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/7
 */
public interface HomepageService {

	/**
	 * 推荐资源列表
	 * @param param
	 * @return
	 */
	R resourcesList(Map<String,Object> param);

	/**
	 * 应用中心
	 * @param param
	 * @return
	 */
	R applicationList(Map<String,Object> param);

	/**
	 * 热门榜
	 * @param param
	 * @return
	 */
	R hotResourceList(Map<String,Object> param);

	/**
	 * 优选
	 * @param param
	 * @return
	 */
	R optimizationResourceList(Map<String,Object> param);


}
