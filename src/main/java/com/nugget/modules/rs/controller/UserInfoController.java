package com.nugget.modules.rs.controller;


import com.nugget.common.utils.R;
import com.nugget.modules.rs.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @Author zhaoyifan
 * @Date 2020/02/06 10:47
 * @Description TODO
 */
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据token信息获取单点用户信息
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/getMsgByToken")
    public R getMsgByToken(@RequestBody Map<String,Object> params, HttpServletRequest request) {

        return userInfoService.getMsgByToken(params);
    }

    /**
     * 获取用户信息
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/userinfo")
    public R userinfo(@RequestBody Map<String,Object> params, HttpServletRequest request) {

        return userInfoService.userinfo(params);
    }

    /**
     * 添加&取消收藏
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/add/userCollected")
    public R addUserCollected(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        return userInfoService.addUserCollected(params);
    }

    /**
     * 添加不感兴趣
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/add/userNotLike")
    public R addUserNotLike(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        return userInfoService.addUserNotLike(params);
    }

    /**
     * 我的收藏列表
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/myCollected")
    public R myCollected(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        return userInfoService.myCollected(params);
    }

    @PostMapping(value = "/myViewList")
    public R myViewList(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        return userInfoService.myViewList(params);
    }

    /**
     * 用户看过资源
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/read/resourcesInfo")
    public R readResourcesInfo(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        return userInfoService.readResourcesInfo(params);
    }

    /**
     * 删除我已经看过的资源
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/delete/myRead")
    public R deleteMyRead(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        return userInfoService.deleteMyRead(params);
    }
}
