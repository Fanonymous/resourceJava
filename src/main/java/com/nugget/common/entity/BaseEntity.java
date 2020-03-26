package com.nugget.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by leilei on 2017/5/5.
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -6751162599369221364L;


    /**
     * 创建者ID
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Timestamp createDate;

    /**
     * 更新者ID
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Timestamp updateDate;


//    private String createNameBy;
//    private String updateNameBy;

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

//    public String getCreateNameBy() {
//        return createNameBy;
//    }
//
//    public void setCreateNameBy(String createNameBy) {
//        this.createNameBy = createNameBy;
//    }
//
//    public String getUpdateNameBy() {
//        return updateNameBy;
//    }
//
//    public void setUpdateNameBy(String updateNameBy) {
//        this.updateNameBy = updateNameBy;
//    }
}
