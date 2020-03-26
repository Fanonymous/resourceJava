package com.nugget.modules.rs.controller;

import com.nugget.common.utils.R;
import com.nugget.modules.rs.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/7
 * 订阅
 */
@RestController
@RequestMapping(value = "/subscribe")
public class SubscribeController {

	@Autowired
	private SubscribeService subscribeService;

	/**
	 * 添加订阅
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/addSubscribe", method = RequestMethod.POST)
	@ResponseBody
	public R addSubscribe(@RequestBody Map<String,Object> param) {
		if(param.isEmpty()){
			return R.error("参数为空");
		}
		return subscribeService.addSubscribe(param);
	}

	/**
	 * 我的订阅
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/mySubscribe", method = RequestMethod.POST)
	@ResponseBody
	public R mySubscribe(@RequestBody Map<String,Object> param) {
		if(param.isEmpty()){
			return R.error("参数为空");
		}
		return subscribeService.mySubscribe(param);
	}

	/**
	 * 订阅标签列表
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/tagList", method = RequestMethod.POST)
	@ResponseBody
	public R tagList(@RequestBody Map<String,Object> param) {
		return subscribeService.tagList(param);
	}

	@RequestMapping(value = "/bookTagList", method = RequestMethod.POST)
	@ResponseBody
	public R bookTagList(@RequestBody Map<String,Object> param) {
		return subscribeService.bookTagList(param);
	}

	/**
	 * 取消订阅
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/cancelSubscribe", method = RequestMethod.POST)
	@ResponseBody
	public R cancelSubscribe(@RequestBody Map<String,Object> param) {
		return subscribeService.cancelSubscribe(param);
	}
}
