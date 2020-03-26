package com.nugget.modules.rs.dto;

import lombok.Data;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/12 12:34
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Data
public class MySubscribeTagDto {
    private Integer tagId;

    private String tagName;

    private String subName;

    private String levelStr;

    private Integer channelId;

    private Integer tagType;

    private Integer subscribeNum;

    private Integer resourcesNum;
}
