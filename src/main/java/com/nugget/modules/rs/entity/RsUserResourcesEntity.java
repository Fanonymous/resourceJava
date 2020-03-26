package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rs_user_resources
 * @author 
 */
@Data
public class RsUserResourcesEntity implements Serializable {
    /**
     * id
     */
    private Long relationId;

    /**
     * 用户id
     */
    private Long rsUserId;

    /**
     * 资源id
     */
    private Long resourcesId;

    /**
     * 是否订阅（订阅1，未订阅0）
     */
    private Integer isSubscribed;

    /**
     * 是否收藏（收藏1，未收藏0）
     */
    private Integer isCollected;

    /**
     * 收藏时间
     */
    private Date collectedDate;

    /**
     * 是否最近看过（看过1，未看过0）
     */
    private Integer recentlyCeen;
    /**
     * 最近看过时间
     */
    private Date recentlyDate;

    /**
     * 是否感兴趣（感兴趣0，不感兴趣1）
     */
    private Integer isInterested;

    /**
     * 不感兴趣时间
     */
    private Date interestedDate;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 是否下载过（下载过1，未下载过0）
     */
    private Integer isDownload;

    /**
     * 删除标识（未删除0，已删除1）
     */
    private Integer enabled;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private static final long serialVersionUID = 1L;


}