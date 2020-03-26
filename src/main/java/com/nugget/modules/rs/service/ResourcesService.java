package com.nugget.modules.rs.service;

import com.nugget.common.utils.R;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/11
 */
public interface ResourcesService {

	/**
	 * 搜索
	 * @param param
	 * @return
	 */
	R search(Map<String,Object> param);

	/**
	 * 资源列表
	 * @param param
	 * @return
	 */
	R resourcesList(Map<String,Object> param);

	/**
	 * 资源详情
	 * @param resourcesId
	 * @return
	 */
	R info(String resourcesId,String rsUserId);

	/**
	 * 相关资源推荐
	 * @param param
	 * @return
	 */
	R relationResources(Map<String,Object> param);

	/**
	 * 名师资源
	 * @param param
	 * @return
	 */
	R bestTeacherResources(Map<String,Object> param);

	/**
	 * 猜您喜欢
	 * @param param
	 * @return
	 */
	R mayLikeResources(Map<String,Object> param);


	R downloadFile2(String urlPath, String excelName, HttpServletResponse response, int i) throws Exception;

	//添加微课资源
	R saveResources(Map<String,Object> param);
	//添加flash
    R saveResources1(Map<String,Object> param);
	//添加德育
    R saveResources2(Map<String,Object> param);
	//添加德育
	R saveResources3(Map<String,Object> param);
	//添加文库
	R saveResources4(Map<String,Object> param);
	//添加精品课程
	R saveResources5(Map<String,Object> param);

}
