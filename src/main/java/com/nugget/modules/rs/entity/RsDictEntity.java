package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rs_dict
 * @author 
 */
@Data
public class RsDictEntity implements Serializable {
    /**
     * id
     */
    private Integer dictId;

    /**
     * 值
     */
    private Integer value;

    /**
     * 标签
     */
    private String dictName;

    /**
     * 字典分类
     */
    private String type;

    /**
     * 类型名称
     */
    private String description;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 删除状态（0：可用   1：不可用）
     */
    private Integer enabled;

    /**
     * 创建Id
     */
    private Long createBy;

    /**
     * 更新Id
     */
    private Long updateBy;

    private static final long serialVersionUID = 1L;


}