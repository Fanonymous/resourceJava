package com.nugget.modules.rs.service;

import com.nugget.common.utils.R;

import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/7
 */
public interface SubscribeService {

	/**
	 * 添加订阅
	 * @param param
	 * @return
	 */
	R addSubscribe(Map<String,Object> param);

	R mySubscribe(Map<String, Object> param);

    R tagList(Map<String, Object> param);

    R bookTagList(Map<String, Object> param);

    R cancelSubscribe(Map<String, Object> param);
}
