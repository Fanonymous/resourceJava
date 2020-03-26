package com.nugget.modules.rs.entity;

import com.nugget.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class RsChannelEntity extends BaseEntity implements Serializable{
    /**
     * id
     */
    private Long channelId;
    /**
     * 名称
     */
    private String channelName;
    /**
     * 链接
     */
    private String url;
    /**
     * 排序
     */
    private Integer orderBy;
    /**
     * 适用于
     */
    private String applyFor;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否跳转新页面
     */
    private Integer isOpen;
    /**
     * 删除字段
     */
    private Integer enabled;
    /**
     * 是否有下级
     */
    private Integer isHaveLower;




}
