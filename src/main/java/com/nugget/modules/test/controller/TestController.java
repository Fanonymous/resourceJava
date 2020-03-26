package com.nugget.modules.test.controller;

import com.alibaba.fastjson.JSON;
import com.nugget.common.utils.HttpRUtils;
import com.nugget.common.utils.R;
import com.nugget.modules.rs.dao.RsApplicationDAO;
import com.nugget.modules.rs.dao.RsTagDAO;
import com.nugget.modules.rs.entity.RsTagEntity;
import com.nugget.modules.test.dto.RsTagDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhaoyifan
 * @Date 2020/02/04 10:47
 * @Description TODO
 */
@RestController
public class TestController {

    @Autowired
    RsApplicationDAO rsApplicationDAO;

    @Autowired
    RsTagDAO rsTagDAO;

    @GetMapping("/test")
    public R test(){
        return R.ok();
    }

    @GetMapping("/test1")
    public R test1(){
        Map<String,Object> map = new HashMap<>();
        List list = rsApplicationDAO.queryList(map);
        return R.ok().put("list",list);
    }

    @GetMapping("/rs/tag/data")
    public R rsTagData(){
        try {
            String url = "http://api.qxt2000.cn/kxvideo/classify/1";
            Map<String, String> params = new HashMap<>();
            params.put("11","11");
            String respData = HttpRUtils.sendGetParam(url,params);

            Map<String,Object> aaa = JSON.parseObject(respData, Map.class);
            Map<String,Object> result = JSON.parseObject(aaa.get("result").toString(), Map.class);
            List<RsTagDataDto> classify = JSON.parseArray(result.get("classify").toString(), RsTagDataDto.class);

            List<RsTagEntity> list = new ArrayList<>();
            for(RsTagDataDto rsTagData:classify){
                list = getRsTagEntity(rsTagData,list,0);
            }
            rsTagDAO.saveBatch(list);
            System.out.printf(respData);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return R.ok();
    }

    private List<RsTagEntity> getRsTagEntity(RsTagDataDto rsTagData, List<RsTagEntity> list, Integer parentId) {
        RsTagEntity rsTagEntity = new RsTagEntity();
        rsTagEntity.setLevel(rsTagData.getType());
        rsTagEntity.setTagType(rsTagData.getType());
        rsTagEntity.setTagName(rsTagData.getName());
        rsTagEntity.setTagId(rsTagData.getId());
        rsTagEntity.setParentIds(parentId+"");
        rsTagEntity.setEnabled(0);
        list.add(rsTagEntity);
        if(rsTagData.getList() != null && rsTagData.getList().size()>0) {
            for (RsTagDataDto rsTagData1 : rsTagData.getList()) {
                getRsTagEntity(rsTagData1, list, rsTagData.getId());
            }
        }
        return list;
    }
}
