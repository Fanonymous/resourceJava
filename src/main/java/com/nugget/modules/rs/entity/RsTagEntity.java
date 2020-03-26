package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rs_tag
 * @author 
 */
@Data
public class RsTagEntity implements Serializable {
    /**
     * id
     */
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签简称
     */
    private String subName;
    /**
     * 频道id
     */
    private Integer channelId;

    /**
     * 标签类型
     */
    private Integer tagType;

    /**
     * 标签归属
     */
    private Integer usedFor;

    /**
     * 排序
     */
    private Integer orderBy;

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
     * 标签使用量
     */
    private Integer useCount;

    /**
     * 是否删除（未删除0，已删除1）
     */
    private Integer enabled;

    /**
     * 在我的订阅是否显示1:是0:否
     */
    private Integer isShowName;
    /**
     * 是否显示父级标签名称1:是0:否
     */
    private Integer isShowParent;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private static final long serialVersionUID = 1L;


}