package com.nugget.modules.rs.controller;

import com.nugget.common.utils.NuStringUtils;
import com.nugget.common.utils.R;
import com.nugget.modules.rs.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2020/2/11
 * 频道
 */
@RestController
@RequestMapping(value = "/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;


    /**
     * 推荐左侧频道列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/channelList", method = RequestMethod.POST)
    @ResponseBody
    public R channelList(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return channelService.channelList(param);

    }

    /**
     * 导航栏
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/navigationNar", method = RequestMethod.POST)
    @ResponseBody
    public R navigationNar(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return channelService.navigationNar(param);

    }


    /**
     * 自定义频道添加/更新
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public R saveOrUpdate(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return channelService.saveOrUpdate(param);

    }

    /**
     * 修改排序
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/changeOrder", method = RequestMethod.POST)
    @ResponseBody
    public R changeOrder(@RequestBody Map<String, Object> param
    ) {
        List<Map<String, Object>> paramList = (List<Map<String, Object>>) param.get("list");

        return channelService.changeOrder(paramList);

    }

    /**
     * 频道推荐列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/hotChannelList", method = RequestMethod.POST)
    @ResponseBody
    public R hotChannelList(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return channelService.hotChannelList(param);

    }

    /**
     * 章节目录
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/tagListTree", method = RequestMethod.GET)
    @ResponseBody
    public R tagListTree(@RequestParam(required = false) String keywords, @RequestParam(required = false) Integer tagId
    ) {
        return channelService.tagListTree(keywords, tagId);

    }


    /**
     * 获取字典list
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getDict", method = RequestMethod.GET)
    @ResponseBody
    public R getDict(@RequestParam(required = false) String type, @RequestParam(required = false) Integer value
    ) {
        Map<String, Object> map = new HashMap<>();
        if (!NuStringUtils.isBlank(type)) {
            map.put("type", type);
        }
        if (value != null) {
            map.put("value", value);

        }
        return channelService.getDict(map);

    }

}
