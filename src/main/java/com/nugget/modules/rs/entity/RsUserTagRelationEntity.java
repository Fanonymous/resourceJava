package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rs_user_tag_relativity
 * @author 
 */
@Data
public class RsUserTagRelationEntity implements Serializable {
    /**
     * 主键
     */
    private Long relationId;

    /**
     * 资源用户id
     */
    private Long rsUserId;

    /**
     * 资源标签id
     */
    private Integer rsTagId;

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