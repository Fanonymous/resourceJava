package com.nugget.modules.rs.service.impl;

import com.nugget.common.utils.R;
import com.nugget.modules.rs.dao.RsFileDetailsDAO;
import com.nugget.modules.rs.dao.RsTagDAO;
import com.nugget.modules.rs.dao.RsUserDAO;
import com.nugget.modules.rs.dao.RsUserTagRelationDAO;
import com.nugget.modules.rs.dto.MySubscribeTagDto;
import com.nugget.modules.rs.dto.RespBookRsTagDto;
import com.nugget.modules.rs.dto.RespRsTagDto;
import com.nugget.modules.rs.entity.RsTagEntity;
import com.nugget.modules.rs.entity.RsUserEntity;
import com.nugget.modules.rs.entity.RsUserTagRelationEntity;
import com.nugget.modules.rs.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by LIUHAO
 * Date:2020/2/7
 */
@Service("SubscribeService")
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    RsUserDAO rsUserDAO;

    @Autowired
    RsTagDAO rsTagDAO;

    @Autowired
    RsUserTagRelationDAO rsUserTagRelationDAO;

    @Autowired
    RsFileDetailsDAO rsFileDetailsDAO;

    @Override
    public R addSubscribe(Map<String, Object> param) {
        if(param.get("rsUserId") == null || param.get("tagList") == null ){
            return R.error("参数错误");
        }
        //先删除此学段之前订阅的标签
        if(param.get("firstTagId") == null){
            rsUserTagRelationDAO.deleteByFirstTagId(param);
        }
        RsUserEntity rsUser = rsUserDAO.queryByParams(param);
        List<Integer> tagsList = (List) param.get("tagList");
        for(Integer tag:tagsList){
            //查询该用户之前是否订阅该标签
            param.put("rsTagId",tag);
            param.put("enabled",0);
            int num = rsUserTagRelationDAO.queryTotal(param);
            if(num == 0){
                RsUserTagRelationEntity rsUserTagRelativity = new RsUserTagRelationEntity();
                rsUserTagRelativity.setRsTagId(tag);
                rsUserTagRelativity.setRsUserId(rsUser.getRsUserId());
                rsUserTagRelativity.setEnabled(0);
                rsUserTagRelativity.setCreateBy(rsUser.getRsUserId());
                rsUserTagRelativity.setCreateDate(new Date());
                rsUserTagRelationDAO.insert(rsUserTagRelativity);
            }
        }
        //rsUserDAO.update(rsUser);
        return R.ok();
    }

    @Override
    public R mySubscribe(Map<String, Object> param) {
        if(param.get("rsUserId") == null){
            return R.error("参数错误");
        }
        List<RsTagEntity> list = rsTagDAO.queryMySubscribe(param);
        List<MySubscribeTagDto> respList = new ArrayList<>();
        for(RsTagEntity rsTag:list){
            MySubscribeTagDto mySubscribeTag = new MySubscribeTagDto();
            mySubscribeTag.setTagId(rsTag.getTagId());
            if(rsTag.getIsShowParent() == 1){
                String tagName = getTagName(rsTag);
                if(tagName.indexOf("_") >=0){
                    String[] tagArray = tagName.split("_");
                    mySubscribeTag.setTagName(tagArray[0]);
                    mySubscribeTag.setSubName(tagArray[1]);
                }else{
                    mySubscribeTag.setTagName(tagName);
                    mySubscribeTag.setSubName(tagName);
                }
            }else{
                mySubscribeTag.setTagName(rsTag.getTagName());
                mySubscribeTag.setSubName(rsTag.getSubName());
            }
            mySubscribeTag.setChannelId(rsTag.getChannelId());
            mySubscribeTag.setTagType(rsTag.getTagType());
            mySubscribeTag.setLevelStr(rsTag.getLevelStr());

            Map<String,Object> fileDetailMap = new HashMap<>();
            fileDetailMap.put("enabled",0);
            fileDetailMap.put("tagIds",rsTag.getTagId());
            mySubscribeTag.setResourcesNum(rsFileDetailsDAO.queryTotal(fileDetailMap));
            respList.add(mySubscribeTag);
        }
        return R.ok().put("list",respList);
    }



    @Override
    public R tagList(Map<String, Object> param) {


        List<String> subscribeList = new ArrayList<>();
        if(param.get("rsUserId") != null){
            String subscribeString = rsUserTagRelationDAO.getAllTagIdByRsUserId(param.get("rsUserId").toString());
            if(subscribeString != null){
                subscribeList = Arrays.asList(subscribeString.split(","));
                //去重复
                subscribeList = subscribeList.stream().distinct().collect(Collectors.toList());
            }
        }

        param.put("usedFor",1);
        param.put("useCount",1);
        if(param.get("level") != null && "1".equals(param.get("level").toString())){
            List<RespRsTagDto> list = rsTagDAO.queryByParams(param);
            for(RespRsTagDto rsTag:list){
                if(subscribeList != null && subscribeList.size()>0 && subscribeList.contains(rsTag.getTagId()+"")){
                    rsTag.setIsSubscribe(1);
                }
            }
            return R.ok().put("list",list);
        }else{
            if(param.get("dataType") != null){
                if("1".equals(param.get("dataType").toString())){
                    param.put("tagTypes","1,2,7,8,9,10");
                }else if("2".equals(param.get("dataType").toString())){
                    param.put("tagTypes","3");
                }else if("3".equals(param.get("dataType").toString())){
                    param.put("tagTypes","4");
                }else{
                    param.put("tagTypes","5,6");
                }
            }else{
				param.put("tagTypes","");
			}


            List<RespRsTagDto> list = rsTagDAO.queryByParams(param);
            List<RespRsTagDto> respList = getChildTag(list,subscribeList,param.get("tagTypes").toString());
            return R.ok().put("list",respList);
        }
    }

    @Override
    public R bookTagList(Map<String, Object> param) {

        param.put("usedFor",1);
        param.put("useCount",1);
        if(param.get("dataType") != null){
            if("1".equals(param.get("dataType").toString())){
                param.put("tagTypes","1");
                param.put("channelId","18");
            }else if("2".equals(param.get("dataType").toString())){
                param.put("tagTypes","3");
            }else if("3".equals(param.get("dataType").toString())){
                param.put("tagTypes","4");
            }
        }else{
            param.put("tagTypes","");
        }

        if(param.get("parentId") != null && !"".equals(param.get("parentId").toString())){
            List<String> bookTagIds = Arrays.asList(param.get("parentId").toString().split(","));
            param.put("bookTagIds",bookTagIds);
        }

        List<RespBookRsTagDto> list = rsTagDAO.queryBookTagByParams(param);
        return R.ok().put("list",list);

    }

    @Override
    public R cancelSubscribe(Map<String, Object> param) {
        rsUserTagRelationDAO.cancelUserSubscribe(param);
        return R.ok();
    }

    private List<RespRsTagDto> getChildTag(List<RespRsTagDto> list, List<String> subscribeList, String tagTypes) {

        for(RespRsTagDto rsTag:list){

            Map<String, Object> param = new HashMap<>();
            param.put("parentId",rsTag.getTagId());
            param.put("tagTypes",tagTypes);
            param.put("useCount",1);
            if(subscribeList != null && subscribeList.size()>0 && subscribeList.contains(rsTag.getTagId()+"")){
                rsTag.setIsSubscribe(1);
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("subscribeList",subscribeList);
                queryMap.put("parentId",rsTag.getTagId());
                int checkedCount = rsTagDAO.queryTotal(queryMap);
                rsTag.setCheckedCount(checkedCount);
            }
            List<RespRsTagDto> childTagList = rsTagDAO.queryByParams(param);

            if(childTagList != null && childTagList.size()>0){
                //只查询到教材级
                if(rsTag.getLevel() < 4){
                    List<RespRsTagDto> childList= getChildTag(childTagList,subscribeList,tagTypes);
                    rsTag.setChildTagList(childList);
                }
            }
        }
        return list;
    }

    private String getTagName(RsTagEntity rsTag) {
        String tagName = rsTag.getTagName();
        Map<String, Object> param = new HashMap<>();
        param.put("tagIds",rsTag.getParentIds());
        List<RsTagEntity> list = rsTagDAO.queryList(param);
        if(list != null && list.size()>0){
            RsTagEntity parentRsTag = list.get(0);
            if(parentRsTag.getIsShowParent() == 1){
                parentRsTag.setTagName(tagName+"-"+parentRsTag.getTagName());
                tagName = getTagName(parentRsTag);
            }else{
                tagName = tagName +"-"+ parentRsTag.getTagName();
            }
            if(parentRsTag.getLevel() == 2) {
                tagName = tagName + "_"+ parentRsTag.getSubName();
            }
        }
        return tagName;
    }
}
