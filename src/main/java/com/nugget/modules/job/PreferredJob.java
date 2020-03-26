package com.nugget.modules.job;

import com.nugget.modules.rs.dao.*;
import com.nugget.modules.rs.entity.RsFileDetailsEntity;
import com.nugget.modules.rs.entity.RsPreferredTempEntity;
import com.nugget.modules.rs.entity.RsTagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/12 17:23
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Component
public class PreferredJob {

    @Autowired
    RsFileDetailsDAO rsFileDetailsDAO;

    @Autowired
    RsUserResourcesDAO rsUserResourcesDAO;

    @Autowired
    RsUserTagRelationDAO rsUserTagRelationDAO;

    @Autowired
    RsPreferredTempDAO rsPreferredTempDAO;

    @Autowired
    RsTagDAO rsTagDAO;

    //每天凌晨12点半执行
    public final static String JOB_CRON =  "0 30 0 * * ? ";

    //每天凌晨1点半执行
    public final static String JOB_CRON_TAG =  "0 30 1 * * ? ";


    @Scheduled(cron = JOB_CRON)
    public void fixedPreferredJob() {
        //先清空临时表信息
        rsPreferredTempDAO.deleteAllData();

        //重置之前优选的数据
        rsFileDetailsDAO.resetPreferred();

        int total = rsFileDetailsDAO.queryTotal();
        if(total>1000){
            //每一千条遍历查询一次
            int queryNum = total/1000;
            for(int i=0;i<queryNum+1;i++){
                Map<String,Object> map = new HashMap<>();
                map.put("offset",i*1000);
                map.put("limit",1000);
                List<RsFileDetailsEntity> list = rsFileDetailsDAO.queryList(map);

                List<RsPreferredTempEntity> saveList = new ArrayList<>();
                for(RsFileDetailsEntity rsFileDetails:list){
                    //查询被收藏次数
                    Map<String,Object> queryMap = new HashMap<>();
                    queryMap.put("isCollected",1);
                    queryMap.put("enabled",0);
                    queryMap.put("resourcesId",rsFileDetails.getResourcesId());
                    Integer collectedNum = rsUserResourcesDAO.queryTotalByMap(queryMap);
                    queryMap.put("isCollected",null);
                    //查询被浏览次数
                    queryMap.put("recentlyCeen",1);
                    Integer viewNum = rsUserResourcesDAO.queryTotalByMap(queryMap);
                    queryMap.put("recentlyCeen",null);

                    //查询被订阅次数
                    Integer subscribeNum = 0;
                    if(rsFileDetails.getTags() != null && !"".equals(rsFileDetails.getTags())){
                        queryMap.put("tagIds",rsFileDetails.getTags());
                        subscribeNum = rsUserTagRelationDAO.queryTotal(queryMap);
                    }

                    Double preferredNum = (collectedNum*0.5) + (viewNum*0.3) + (subscribeNum*0.2);
                    RsPreferredTempEntity rsPreferredTemp = new RsPreferredTempEntity();
                    rsPreferredTemp.setResourcesId(rsFileDetails.getResourcesId());
                    rsPreferredTemp.setPreferredNum(preferredNum);
                    saveList.add(rsPreferredTemp);
                    System.out.println(preferredNum);
                }

                rsPreferredTempDAO.saveBatch(saveList);
            }
        }else{
            Map<String,Object> map = new HashMap<>();
            map.put("offset",0);
            map.put("limit",total);
            List<RsFileDetailsEntity> list = rsFileDetailsDAO.queryList(map);

            List<RsPreferredTempEntity> saveList = new ArrayList<>();
            for(RsFileDetailsEntity rsFileDetails:list){
                //查询被收藏次数
                Map<String,Object> queryMap = new HashMap<>();
                queryMap.put("isCollected",1);
                queryMap.put("enabled",0);
                queryMap.put("resourcesId",rsFileDetails.getResourcesId());
                Integer collectedNum = rsUserResourcesDAO.queryTotalByMap(queryMap);
                queryMap.put("isCollected",null);
                //查询被浏览次数
                queryMap.put("recentlyCeen",1);
                Integer viewNum = rsUserResourcesDAO.queryTotalByMap(queryMap);
                queryMap.put("recentlyCeen",null);

                //查询被订阅次数
                Integer subscribeNum = 0;
                if(rsFileDetails.getTags() != null && !"".equals(rsFileDetails.getTags())){
                    queryMap.put("tagIds",rsFileDetails.getTags());
                    subscribeNum = rsUserTagRelationDAO.queryTotal(queryMap);
                }

                Double preferredNum = (collectedNum*0.5) + (viewNum*0.3) + (subscribeNum*0.2);

                RsPreferredTempEntity rsPreferredTemp = new RsPreferredTempEntity();
                rsPreferredTemp.setResourcesId(rsFileDetails.getResourcesId());
                rsPreferredTemp.setPreferredNum(preferredNum);
                saveList.add(rsPreferredTemp);
                System.out.println(preferredNum);
            }

            rsPreferredTempDAO.saveBatch(saveList);
        }

        //更新资源表优选标示
        Map<String,Object> preferredMap = new HashMap<>();
        preferredMap.put("offset",0);
        if(total > 10){
            preferredMap.put("limit",total/10);
        }else{
            preferredMap.put("limit",total);
        }
        List<RsPreferredTempEntity> preferredList = rsPreferredTempDAO.queryList(preferredMap);

        Integer state = rsFileDetailsDAO.updatePreferred(preferredList);
        System.out.println(" >>fixedPreferredJob执行...."+ LocalDateTime.now());
    }


    @Scheduled(cron = JOB_CRON_TAG)
    public void fixedTagJob() {
        Map<String,Object> map = new HashMap<>();
        map.put("enabled",0);
        List<RsTagEntity> list = rsTagDAO.queryList(map);

        for(RsTagEntity rsTag:list){
            if(rsTag.getTagType() > 6){
                continue;
            }
            //判断该标签下是否有资源
            Map<String,Object> fileDetailMap = new HashMap<>();
            fileDetailMap.put("enabled",0);
            fileDetailMap.put("tagIds",rsTag.getTagId());
            int fileCount = rsFileDetailsDAO.queryTotal(fileDetailMap);
            if(fileCount > 0){
                if(rsTag.getLevel() == 3){
                    rsTag.setOrderBy(fileCount);
                }
                rsTag.setUseCount(1);
                rsTagDAO.update(rsTag);
            }
        }
    }
}
