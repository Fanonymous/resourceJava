package com.nugget.modules.rs.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2020/2/13 11:49
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Data
public class RsPreferredTempEntity implements Serializable {

    private Long id;

    private Long resourcesId;

    private Double preferredNum;

    private static final long serialVersionUID = 1L;
}
