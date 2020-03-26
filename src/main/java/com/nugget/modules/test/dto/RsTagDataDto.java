package com.nugget.modules.test.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by LIUHAO
 * Date:2020/2/28
 */
@Data
public class RsTagDataDto {

	private Integer id;

	private String name;

	private Integer type;

	private List<RsTagDataDto> list;
}
