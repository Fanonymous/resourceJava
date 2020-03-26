package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rs_application
 * @author 
 */
@Data
public class RsApplicationEntity implements Serializable {
    private Long appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 地址
     */
    private String address;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderBy;

    /**
     * 访问次数
     */
    private Integer viewCount;

    /**
     * 删除标识（未删除0，已删除1）
     */
    private Byte enabled;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private static final long serialVersionUID = 1L;
}