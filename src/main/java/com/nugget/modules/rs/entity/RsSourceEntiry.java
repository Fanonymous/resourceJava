package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * rs_source
 * @author 
 */
@Data
public class RsSourceEntiry implements Serializable {
    /**
     * id 
     */
    private Long sourceId;

    /**
     * 来源名称
     */
    private String sourceName;

    /**
     * 来源地址
     */
    private String sourceAddress;

    /**
     * appid
     */
    private String appid;

    /**
     * appkey
     */
    private String appkey;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 联系电话
     */
    private String telphone;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderBy;

    private static final long serialVersionUID = 1L;


}