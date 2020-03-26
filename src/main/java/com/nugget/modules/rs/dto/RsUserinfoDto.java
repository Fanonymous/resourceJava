package com.nugget.modules.rs.dto;

import lombok.Data;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/8 12:55
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Data
public class RsUserinfoDto {

    private Long rsUserId;

    private Long userId;

    private String username;

    private String fullName;

    private Integer userType;

    private String imageUrl;

    private Integer subscribedNum;

    private Integer collectedNum;

    private Integer viewNum;
}
