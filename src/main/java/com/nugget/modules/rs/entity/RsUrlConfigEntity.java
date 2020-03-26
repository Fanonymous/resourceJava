package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/13 15:43
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Data
public class RsUrlConfigEntity implements Serializable {

    private Long id;

    private String url;

    private Integer comeFrom;

    private Integer enabled;

    private Long createBy;

    private Date createDate;

    private static final long serialVersionUID = 1L;
}
