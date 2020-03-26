package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rs_file_details
 * @author 
 */
@Data
public class RsFileDetailsEntity implements Serializable {
    /**
     * 主键
     */
    private Long resourcesId;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 资源大小（M）
     */
    private Long size;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 上传人
     */
    private Long uploadBy;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 是否为优选
     */
    private Byte isPreferred;

    /**
     * 来源
     */
    private Integer comeFrom;

    /**
     * 搜索字段
     */
    private String searchFiled;

    /**
     * 资源地址
     */
    private String resourcesAddress;

    /**
     * 缩略图
     */
    private String thumbAddress;

    /**
     * 首图地址
     */
    private String headerImageAddress;

    /**
     * 资源标签
     */
    private String tags;

    /**
     * 收藏次数
     */
    private Integer collectionCount;

    /**
     * 资源类型（视频，音频等）
     */
    private String resourcesType;

    /**
     * 订阅次数
     */
    private Integer subscribeCount;

    /**
     * 分享次数
     */
    private Integer shareCount;

    /**
     * 是否删除（未删除0，已删除1）
     */
    private Byte enabled;

    private Integer channelId;

    private String author;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private static final long serialVersionUID = 1L;

}