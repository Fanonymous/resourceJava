package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rs_user
 * @author 
 */
@Data
public class RsUserEntity implements Serializable {
    /**
     * 主键
     */
    private Long rsUserId;

    /**
     * 用户唯一标识
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;
     /**
     * 密码
     */
    private String password;
     /**
     * 姓名
     */
    private String fullName;
     /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 头像地址
     */
    private String imageUrl;

    /**
     * 来源
     */
    private Integer comeFrom;

    /**
     * 标签
     */
    private String tags;

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