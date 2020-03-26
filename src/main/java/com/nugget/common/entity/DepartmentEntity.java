package com.nugget.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class DepartmentEntity implements Serializable {

    /**
     * 各系统组织机构id
     */
    private Integer deptId;
    /**
     * 机构名称
     */
    private String deptName;

    /**
     * 部门级别字符串
     */
    private String level;

    /**
     * 父级主键id
     */
    private Integer parentId;

    /**
     * 机构类型字典值
     */
    private Integer deptType;

    /**
     * 机构类型
     */
    private String deptTypeName;

    private String schoolSideType;

    /**
     * 学校类型
     */
    private String schoolType;

    /**
     * 机构负责人
     */
    private String manager;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 排序号
     */
    private Long order;

    /**
     * 创建者ID
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Timestamp createDate;

    /**
     * 更新者ID
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Timestamp updateDate;


}
