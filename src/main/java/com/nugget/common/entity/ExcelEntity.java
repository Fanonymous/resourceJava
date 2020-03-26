package com.nugget.common.entity;

import lombok.Data;

@Data
public class ExcelEntity {
    /**
     * 开始行
     */
    private int startRow;
    /**
     * 结束行
     */
    private int endRow;
    /**
     * 开始列
     */
    private int startCol;
    /**
     * 结束列
     */
    private int endCol;
    /**
     * 单元格值
     */
    private String value;

}