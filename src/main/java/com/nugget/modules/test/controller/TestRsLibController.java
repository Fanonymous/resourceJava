package com.nugget.modules.test.controller;

import com.nugget.common.utils.R;
import com.nugget.modules.rs.dao.RsFileDetailsDAO;
import com.nugget.modules.rs.dao.RsTagDAO;
import com.nugget.modules.rs.entity.RsTagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author zhaoyifan
 * @Date 2020/02/04 10:47
 * @Description TODO
 */
@RestController
public class TestRsLibController {


    @Autowired
    RsTagDAO rsTagDAO;

    @Autowired
    RsFileDetailsDAO rsFileDetailsDAO;

    @GetMapping("/test/rs/tag")
    public R test(){

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
        return R.ok();
    }
}
