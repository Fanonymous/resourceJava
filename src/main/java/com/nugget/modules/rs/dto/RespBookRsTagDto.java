package com.nugget.modules.rs.dto;

import lombok.Data;

import java.util.List;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/10 13:14
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Data
public class RespBookRsTagDto {
    /**
     * id
     */
    private Integer tagId;

    private String tagIds;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签类型
     */
    private Integer tagType;

    /**
     * 标签类型
     */
    private String tagTypeName;

    /**
     * 标签归属
     */
    private Integer usedFor;

    /**
     * 层级
     */
    private Integer level;

    private String levelStr;

    /**
     * 父级标签id
     */
    private String parentIds;
    /**
     * 是否已经订阅1:是0:否
     */
    private int isSubscribe;

    private List<RespBookRsTagDto> childTagList;
}
