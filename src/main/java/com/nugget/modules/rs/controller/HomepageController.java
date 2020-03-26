package com.nugget.modules.rs.controller;

import com.nugget.common.utils.R;
import com.nugget.modules.rs.service.HomepageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/7
 * 资源首页
 */
@RestController
@RequestMapping(value = "/homepage")
public class HomepageController {

    @Autowired
    private HomepageService homepageService;

    /**
     * 推荐资源列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/resourcesList", method = RequestMethod.POST)
    @ResponseBody
    public R resorcesList(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return homepageService.resourcesList(param);

    }

    /**
     * 应用中心
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/applicationList", method = RequestMethod.POST)
    @ResponseBody
    public R applicationList(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return homepageService.applicationList(param);

    }

    /**
     * 热门榜
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/hotResourceList", method = RequestMethod.POST)
    @ResponseBody
    public R hotResourceList(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return homepageService.hotResourceList(param);

    }


    /**
     * 每日优选
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/optimizationResourceList", method = RequestMethod.POST)
    @ResponseBody
    public R optimizationResourceList(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return homepageService.optimizationResourceList(param);

    }


}
