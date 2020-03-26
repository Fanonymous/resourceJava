package com.nugget.modules.rs.service;

import com.nugget.common.utils.R;

import java.util.Map; /**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/7 18:25
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
public interface UserInfoService {

    R getMsgByToken(Map<String, Object> params);

    R userinfo(Map<String, Object> params);

    R addUserCollected(Map<String, Object> params);

    R addUserNotLike(Map<String, Object> params);

    R myCollected(Map<String, Object> params);

    R readResourcesInfo(Map<String, Object> params);

    R deleteMyRead(Map<String, Object> params);

    R myViewList(Map<String, Object> params);
}
