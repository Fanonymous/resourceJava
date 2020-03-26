package com.nugget.modules.rs.service.impl;

import com.alibaba.fastjson.JSON;
import com.nugget.common.utils.*;
import com.nugget.modules.rs.dao.*;
import com.nugget.modules.rs.dto.UserResourcesDto;
import com.nugget.modules.rs.dto.RsUserinfoDto;
import com.nugget.modules.rs.dto.UserinfoApiDto;
import com.nugget.modules.rs.dto.UserinfoDto;
import com.nugget.modules.rs.entity.*;
import com.nugget.modules.rs.service.UserInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/7 18:25
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    RsUserDAO rsUserDAO;

    @Autowired
    RsUserResourcesDAO rsUserResourcesDAO;

    @Autowired
    RsFileDetailsDAO rsFileDetailsDAO;

    @Autowired
    RsUrlConfigDAO rsUrlConfigDAO;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private RsUserTagRelationDAO rsUserTagRelationDAO;

    @Autowired
    private RsTagDAO rsTagDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R getMsgByToken(Map<String, Object> params) {
        if(params.get("token") == null){
            return R.error("token不能为空！");
        }
        if(params.get("comeFrom") == null){
            return R.error("参数错误！");
        }
        try {
            RsUrlConfigEntity rsUrlConfig = rsUrlConfigDAO.queryByComeFrom(params);
            if(rsUrlConfig == null){
                return R.error("单点配置错误，请检查配置");
            }
            String url = rsUrlConfig.getUrl();
            Map<String,String> getMap = new HashMap<>();
            getMap.put("token",params.get("token").toString());
            String userInfoString = HttpRUtils.sendGetParam(url,getMap);
            UserinfoApiDto userinfoApiDto = JSON.parseObject(userInfoString, UserinfoApiDto.class);
            if(userinfoApiDto.getCode() != 0){
                return R.error(userinfoApiDto.getMsg());
            }
            UserinfoDto userinfo = userinfoApiDto.getUserinfo();

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("userId",userinfo.getUserId());
            queryMap.put("comeFrom",params.get("comeFrom"));
            RsUserEntity rsUser = rsUserDAO.queryByParams(queryMap);
            if(rsUser != null && rsUser.getRsUserId() > 0){
                rsUser.setUsername(userinfo.getUsername());
                rsUser.setPassword(userinfo.getPassword());
                rsUser.setUserType(userinfo.getUserType());
                rsUser.setFullName(userinfo.getFullName());
                rsUser.setComeFrom(Integer.valueOf(params.get("comeFrom").toString()));
                rsUser.setImageUrl(userinfo.getImageUrl());
                rsUser.setUpdateBy(1l);
                rsUser.setUpdateDate(new Date());
                rsUserDAO.update(rsUser);
                return R.ok().put("rsUserId",rsUser.getRsUserId()).put("isFirstLogin",0);
            }else{
                rsUser = new RsUserEntity();
                rsUser.setRsUserId(SnowflakeIdWorker.getInstant().nextId());
                rsUser.setUserId(userinfo.getUserId());
                rsUser.setUsername(userinfo.getUsername());
                rsUser.setPassword(userinfo.getPassword());
                rsUser.setUserType(userinfo.getUserType());
                rsUser.setFullName(userinfo.getFullName());
                rsUser.setComeFrom(Integer.valueOf(params.get("comeFrom").toString()));
                rsUser.setImageUrl(userinfo.getImageUrl());
                rsUser.setCreateBy(1l);
                rsUser.setCreateDate(new Date());
                rsUserDAO.insert(rsUser);

                //添加频道
                Map<String,Object> map = new HashMap<>();
                map.put("rsUserId",rsUser.getRsUserId());
                List<Map<String,Object>> list = new ArrayList<>();
                //id固定
                Map<String,Object> map1 = new HashMap<>();
                map1.put("orderBy",1000);
                map1.put("channelId",1);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1001);
                map1.put("channelId",2);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1005);
                map1.put("channelId",16);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1006);
                map1.put("channelId",17);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1007);
                map1.put("channelId",6);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1008);
                map1.put("channelId",7);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1009);
                map1.put("channelId",18);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1010);
                map1.put("channelId",10);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1011);
                map1.put("channelId",12);
                list.add(map1);

                map1 = new HashMap<>();
                map1.put("orderBy",1012);
                map1.put("channelId",13);
                list.add(map1);

                if(rsUser.getUserType() != 3){
                    map1 = new HashMap<>();
                    map1.put("orderBy",1002);
                    map1.put("channelId",3);
                    list.add(map1);
                    map1 = new HashMap<>();
                    map1.put("orderBy",1003);
                    map1.put("channelId",4);
                    list.add(map1);
                    map1 = new HashMap<>();
                    map1.put("orderBy",1004);
                    map1.put("channelId",5);
                    list.add(map1);
                }
                map.put("list",list);
                channelDao.saveListData(map);
                return R.ok().put("rsUserId",rsUser.getRsUserId()).put("isFirstLogin",1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    @Override
    public R userinfo(Map<String, Object> params) {
        if(params.get("rsUserId") == null){
            return R.error("参数错误");
        }
        RsUserEntity rsUser = rsUserDAO.queryByParams(params);

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("enabled",0);
        queryMap.put("rsUserId",params.get("rsUserId"));
        queryMap.put("isSubscribed",1);
        //获取用户订阅数
        Integer subscribedNum = rsUserTagRelationDAO.queryTotal(queryMap);
        queryMap.put("isSubscribed",null);
        //获取用户收藏数量
        queryMap.put("isCollected",1);
        params.put("notChannelId",18);
        Integer collectedNum = rsUserResourcesDAO.queryUserResourcesTotal(queryMap);
        queryMap.put("isCollected",null);
        params.put("notChannelId",null);
        //获取用户最近看过
        queryMap.put("recentlyCeen",1);
        Integer viewNum = rsUserResourcesDAO.queryUserResourcesTotal(queryMap);
        queryMap.put("recentlyCeen",null);

        RsUserinfoDto userinfo = new RsUserinfoDto();
        userinfo.setRsUserId(rsUser.getRsUserId());
        userinfo.setUserId(rsUser.getUserId());
        userinfo.setUserType(rsUser.getUserType());
        userinfo.setUsername(rsUser.getUsername());
        userinfo.setFullName(rsUser.getFullName());
        userinfo.setImageUrl(rsUser.getImageUrl());
        userinfo.setSubscribedNum(subscribedNum);
        userinfo.setCollectedNum(collectedNum);
        userinfo.setViewNum(viewNum);
        return R.ok().put("userinfo",userinfo);

    }

    @Override
    public R addUserCollected(Map<String, Object> params) {
        if(params.get("rsUserId") == null || params.get("resourcesId") == null || params.get("type") == null ){
            return R.error("参数错误");
        }
        //判断用户是否已经收藏该资源
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("rsUserId",params.get("rsUserId"));
        queryMap.put("resourcesId",params.get("resourcesId"));
        //queryMap.put("isCollected",1);
        RsUserResourcesEntity userResources = rsUserResourcesDAO.queryByMap(queryMap);
        if(userResources == null){
            //收藏
            if(Integer.valueOf(params.get("type").toString()) == 1){
                userResources = new RsUserResourcesEntity();
                userResources.setResourcesId(Long.valueOf(params.get("resourcesId").toString()));
                userResources.setRsUserId(Long.valueOf(params.get("rsUserId").toString()));
                userResources.setIsCollected(1);
                userResources.setCollectedDate(new Date());
                userResources.setEnabled(0);
                userResources.setCreateBy(Long.valueOf(params.get("rsUserId").toString()));
                userResources.setCreateDate(new Date());
                rsUserResourcesDAO.insert(userResources);

                //对应的资源的收藏量+1
                RsFileDetailsEntity rsFileDetails = rsFileDetailsDAO.queryByResourcesId(Long.valueOf(params.get("resourcesId").toString()));
                if(rsFileDetails != null){
                    rsFileDetails.setResourcesType(null);
                    if(rsFileDetails != null && rsFileDetails.getCollectionCount() != null){
                        rsFileDetails.setCollectionCount(rsFileDetails.getCollectionCount()+1);
                    }else {
                        rsFileDetails.setCollectionCount(1);
                    }
                    rsFileDetailsDAO.update(rsFileDetails);
                }
            }
        }else{
            //收藏
            if(Integer.valueOf(params.get("type").toString()) == 1){
                if(userResources.getIsCollected() == null || userResources.getIsCollected() != 1){
                    userResources.setIsCollected(1);
                    userResources.setCollectedDate(new Date());
                    userResources.setUpdateBy(Long.valueOf(params.get("rsUserId").toString()));
                    userResources.setUpdateDate(new Date());
                    rsUserResourcesDAO.update(userResources);

                    //对应的资源的收藏量+1
                    RsFileDetailsEntity rsFileDetails = rsFileDetailsDAO.queryByResourcesId(Long.valueOf(params.get("resourcesId").toString()));
                    if(rsFileDetails != null){
                        rsFileDetails.setResourcesType(null);
                        if(rsFileDetails != null && rsFileDetails.getCollectionCount() != null){
                            rsFileDetails.setCollectionCount(rsFileDetails.getCollectionCount()+1);
                        }else {
                            rsFileDetails.setCollectionCount(1);
                        }
                        rsFileDetailsDAO.update(rsFileDetails);
                    }
                }else{
                    return R.error("您已经收藏该资源！");
                }
            }else {//取消收藏
                userResources.setIsCollected(0);
                userResources.setCollectedDate(new Date());
                userResources.setUpdateBy(Long.valueOf(params.get("rsUserId").toString()));
                userResources.setUpdateDate(new Date());
                rsUserResourcesDAO.update(userResources);

                //对应的资源的收藏量-1
                RsFileDetailsEntity rsFileDetails = rsFileDetailsDAO.queryByResourcesId(Long.valueOf(params.get("resourcesId").toString()));
                if(rsFileDetails != null){
                    rsFileDetails.setResourcesType(null);
                    if(rsFileDetails != null && rsFileDetails.getCollectionCount() != null && rsFileDetails.getCollectionCount() != 0){
                        rsFileDetails.setCollectionCount(rsFileDetails.getCollectionCount()-1);
                    }else {
                        rsFileDetails.setCollectionCount(0);
                    }
                    rsFileDetailsDAO.update(rsFileDetails);
                }
            }

        }
        return R.ok();
    }

    @Override
    public R addUserNotLike(Map<String, Object> params) {
        if(params.get("rsUserId") == null || params.get("resourcesId") == null){
            return R.error("参数错误");
        }
        //判断用户是否已经添加不感兴趣
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("rsUserId",params.get("rsUserId"));
        queryMap.put("resourcesId",params.get("resourcesId"));
        //queryMap.put("isInterested",1);
        RsUserResourcesEntity userResources = rsUserResourcesDAO.queryByMap(queryMap);
        if(userResources == null){
            //添加不感兴趣
            userResources = new RsUserResourcesEntity();
            userResources.setResourcesId(Long.valueOf(params.get("resourcesId").toString()));
            userResources.setRsUserId(Long.valueOf(params.get("rsUserId").toString()));
            userResources.setIsInterested(1);
            userResources.setInterestedDate(new Date());
            userResources.setEnabled(0);
            userResources.setCreateBy(Long.valueOf(params.get("rsUserId").toString()));
            userResources.setCreateDate(new Date());
            rsUserResourcesDAO.insert(userResources);
        }else{
            //添加不感兴趣
            if(userResources.getIsInterested() == null || userResources.getIsInterested() != 1){
                userResources.setIsInterested(1);
                userResources.setInterestedDate(new Date());
                userResources.setUpdateBy(Long.valueOf(params.get("rsUserId").toString()));
                userResources.setUpdateDate(new Date());
                rsUserResourcesDAO.update(userResources);
            }else{
                return R.error("您已经添加不感兴趣！");
            }
        }
        return R.ok();
    }

    @Override
    public R myCollected(Map<String, Object> params) {

        params.put("isCollected",1);
        //获取我的课本库
        if(params.get("type") != null && "1".equals(params.get("type").toString())){
            params.put("channelId",18);
            params.put("orderByStr","order by tagsOrderBy");

            List<UserResourcesDto> list = rsUserResourcesDAO.getUserResources(params);
            //遍历获取tags的名称
            this.addTagName(list);
            return R.ok().put("list", list);
        }else{
            params.put("notChannelId",18);
        }

        Query query = new Query(params);
        query.put("orderByStr","order by rur.collected_date desc");
        List<UserResourcesDto> list = rsUserResourcesDAO.getUserResources(query);
        //遍历获取tags的名称
        this.addTagName(list);
//        for(UserResourcesDto userResources:list){
//            if(userResources.getTags() != null && !"".equals(userResources.getTags())){
//                String tagsName = rsTagDAO.getTagsName(userResources.getTags());
//                userResources.setTags(tagsName);
//            }else{
//                userResources.setTags("");
//            }
//        }
        int total = rsUserResourcesDAO.queryUserResourcesTotal(params);
        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getCurrPage());
        return R.ok().put("page", pageUtil);
    }


    private void addTagName(List<UserResourcesDto> list){
        for (UserResourcesDto mm:list){
            String tagsName ="";
            if(mm.getTags() != null){
                tagsName = mm.getTags();
                String [] ids = StringUtils.split(tagsName,",");
                StringBuffer stringBuffer = new StringBuffer("");
                if(ids[0].equals("0") && ids.length>4){
                    for (int i = 1; i < 5; i++) {
                        RsTagEntity tagEntity= rsTagDAO.queryObject(Integer.parseInt(ids[i]));
                        if(tagEntity != null){
                            if(i!=4 && i!=ids.length-1){
                                stringBuffer.append(tagEntity.getTagName()+"|");
                            }else {
                                stringBuffer.append(tagEntity.getTagName());
                            }
                        }
                    }
                }else {
                    for (int i = 0; i < ids.length; i++) {
                        RsTagEntity tagEntity= rsTagDAO.queryObject(Integer.parseInt(ids[i]));
                        if(tagEntity != null){
                            if( i!=ids.length-1){
                                stringBuffer.append(tagEntity.getTagName()+"|");
                            }else {
                                stringBuffer.append(tagEntity.getTagName());
                            }
                        }
                    }
                }
                mm.setTags(stringBuffer.toString());
            }
        }
    }

    @Override
    public R readResourcesInfo(Map<String, Object> params) {
        if(params.get("rsUserId") == null || params.get("resourcesId") == null ){
            return R.error("参数错误");
        }
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("rsUserId",params.get("rsUserId"));
        queryMap.put("resourcesId",params.get("resourcesId"));
        RsUserResourcesEntity userResources = rsUserResourcesDAO.queryByMap(queryMap);
        if(userResources == null){
            userResources = new RsUserResourcesEntity();
            userResources.setResourcesId(Long.valueOf(params.get("resourcesId").toString()));
            userResources.setRsUserId(Long.valueOf(params.get("rsUserId").toString()));
            userResources.setRecentlyCeen(1);
            userResources.setRecentlyDate(new Date());
            userResources.setEnabled(0);
            userResources.setCreateBy(Long.valueOf(params.get("rsUserId").toString()));
            userResources.setCreateDate(new Date());
            rsUserResourcesDAO.insert(userResources);

            //对应的资源的浏览量+1
            RsFileDetailsEntity rsFileDetails = rsFileDetailsDAO.queryByResourcesId(Long.valueOf(params.get("resourcesId").toString()));
            if(rsFileDetails != null){
                rsFileDetails.setResourcesType(null);
                if(rsFileDetails != null && rsFileDetails.getViewCount() != null){
                    rsFileDetails.setViewCount(rsFileDetails.getViewCount()+1);
                }else {
                    rsFileDetails.setViewCount(1);
                }
                rsFileDetailsDAO.update(rsFileDetails);
            }
        }else{
            if(userResources.getRecentlyCeen() == null || userResources.getRecentlyCeen() != 1){
                userResources.setRecentlyCeen(1);
                userResources.setRecentlyDate(new Date());
                userResources.setUpdateBy(Long.valueOf(params.get("rsUserId").toString()));
                userResources.setUpdateDate(new Date());
                rsUserResourcesDAO.update(userResources);

                //对应的资源的浏览量+1
                RsFileDetailsEntity rsFileDetails = rsFileDetailsDAO.queryByResourcesId(Long.valueOf(params.get("resourcesId").toString()));
                if(rsFileDetails != null){
                    rsFileDetails.setResourcesType(null);
                    if(rsFileDetails != null && rsFileDetails.getViewCount() != null){
                        rsFileDetails.setViewCount(rsFileDetails.getViewCount()+1);
                    }else {
                        rsFileDetails.setViewCount(1);
                    }
                    rsFileDetailsDAO.update(rsFileDetails);
                }
            }else{
                //更新阅读时间
                userResources.setRecentlyDate(new Date());
                userResources.setUpdateBy(Long.valueOf(params.get("rsUserId").toString()));
                userResources.setUpdateDate(new Date());
                rsUserResourcesDAO.update(userResources);
            }
        }

        return R.ok();
    }

    @Override
    public R deleteMyRead(Map<String, Object> params) {
        if(params.get("rsUserId") == null || params.get("resourcesId") == null ){
            return R.error("参数错误");
        }
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("rsUserId",params.get("rsUserId"));
        queryMap.put("resourcesId",params.get("resourcesId"));
        RsUserResourcesEntity userResources = rsUserResourcesDAO.queryByMap(queryMap);
        if(userResources != null){
            userResources.setRecentlyCeen(0);
            userResources.setUpdateBy(Long.valueOf(params.get("rsUserId").toString()));
            userResources.setUpdateDate(new Date());
            rsUserResourcesDAO.update(userResources);
        }
        return R.ok();
    }

    @Override
    public R myViewList(Map<String, Object> params) {
        params.put("recentlyCeen",1);
        Query query = new Query(params);
        query.put("orderByStr","order by rur.recently_date desc");
        List<UserResourcesDto> list = rsUserResourcesDAO.getUserResources(query);
        //遍历获取tags的名称
//        for(UserResourcesDto userResources:list){
//            if(userResources.getTags() != null && !"".equals(userResources.getTags())){
//                String tagsName = rsTagDAO.getTagsName(userResources.getTags());
//                userResources.setTags(tagsName);
//            }else{
//                userResources.setTags("");
//            }
//        }
        this.addTagName(list);
        int total = rsUserResourcesDAO.queryUserResourcesTotal(params);
        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getCurrPage());
        return R.ok().put("page", pageUtil);
    }
}
