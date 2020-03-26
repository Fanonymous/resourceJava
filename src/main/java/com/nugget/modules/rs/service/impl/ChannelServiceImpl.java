package com.nugget.modules.rs.service.impl;

import com.nugget.common.utils.NuStringUtils;
import com.nugget.common.utils.R;
import com.nugget.modules.rs.dao.ChannelDao;
import com.nugget.modules.rs.dao.RsDictDAO;
import com.nugget.modules.rs.dao.RsTagDAO;
import com.nugget.modules.rs.entity.RsChannelEntity;
import com.nugget.modules.rs.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/11
 */
@Service("ChannelService")
public class ChannelServiceImpl implements ChannelService {

	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private RsTagDAO tagDAO;

	@Autowired
	private RsDictDAO dictDAO;


	/**
	 * 用户频道列表
	 * @return
	 */
	@Override
	public R channelList(Map<String,Object> param){

		// rsUserId, userType
		List<Map<String,Object>> list = channelDao.getUserChannel(param);
		if(list.isEmpty()){
			return R.ok().put("list",new ArrayList<>());
		}
		//channelId,channelName,url
		return R.ok().put("list",list);
	}

	/**
	 * 频道添加/更新
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R saveOrUpdate(Map<String,Object> param){
		//channelList, reUserId
		int res = channelDao.del(param.get("rsUserId").toString());
		if(res < 0){
			return R.error("操作失败");
		}
		int re = channelDao.saveBatch(param);
		if(re < 0 ){
			return R.error("操作失败");
		}
		return R.ok();
	}

	/**
	 * 频道推荐列表
	 * @return
	 */
	@Override
	public R hotChannelList(Map<String,Object> param){

		// reUser_id, userType
		List<Map<String,Object>> list = channelDao.hotChannelList(param);
		if(list.isEmpty()){
			return R.ok().put("list",new ArrayList<>());
		}
		//channelId,channelName,url
		return R.ok().put("list",list);
	}

	@Override
	public R navigationNar(Map<String,Object> param){
		Integer isOpen = Integer.parseInt(param.get("isOpen").toString());
		List<Map<String,Object>> list = new ArrayList<>();
		RsChannelEntity channelEntity = channelDao.queryObject(Long.parseLong(param.get("channelId").toString()));
		if(channelEntity != null){
			param.put("url",channelEntity.getUrl());
		}
		if(isOpen == 0 && channelEntity.getIsHaveLower()==1){
			list = channelDao.getSecondTag(Integer.parseInt(param.get("channelId").toString()));
		} else if (isOpen == 1) {
			list = channelDao.getUserChannel(param);
		}

		return R.ok().put("list",list);
	}

	@Override
	public R tagListTree(String keywords,Integer tagId){
		List<Map<String,Object>> list = tagDAO.getByParentId(tagId);
		if(list.isEmpty()){
			return R.ok().put("list",new ArrayList<>());
		}
		if(NuStringUtils.isBlank(keywords)){
			this.getTree(list,tagId);
		}else {
//			List<> listByKey = tagDAO.listByKey(tagId);


		}


		return R.ok().put("list",list);
	}

	private void getTree(List<Map<String,Object>> list,Integer tagId){
		if(!list.isEmpty()){
			for (Map<String,Object> map:list) {
				List<Map<String,Object>> mapList = tagDAO.getByParentId(Integer.parseInt(map.get("tagId").toString()));
				if(!mapList.isEmpty()){
					map.put("children",mapList);
					this.getTree(mapList,Integer.parseInt(map.get("tagId").toString()));
				}
			}
		}else {
			return;
		}
	}

	@Override
	public R getDict(Map<String,Object> map){
		List<Map<String,Object>> list = dictDAO.getDict(map);
		return R.ok().put("list",list);
	}

	public R changeOrder(List<Map<String,Object>> mapList){
		int re= channelDao.updateOrder(mapList);
		if(re<0){
			return R.error("修改排序失败");
		}
		return R.ok();
	}


}
