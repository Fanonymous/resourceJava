package com.nugget.common.validator.group;

import javax.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 * @author hb
 * @email 1247898199@qq.com
 * @date 2019-01-09
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
