package com.nugget.modules.rs.service.impl;

import com.nugget.common.utils.NuStringUtils;
import com.nugget.common.utils.PageUtils;
import com.nugget.common.utils.Query;
import com.nugget.common.utils.R;
import com.nugget.modules.rs.dao.RsApplicationDAO;
import com.nugget.modules.rs.dao.RsFileDetailsDAO;
import com.nugget.modules.rs.dao.RsTagDAO;
import com.nugget.modules.rs.dao.RsUserTagRelationDAO;
import com.nugget.modules.rs.entity.RsTagEntity;
import com.nugget.modules.rs.service.HomepageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by LIUHAO
 * Date:2020/2/7
 */
@Service("HomepageService")
public class HomepageServiceImpl implements HomepageService {

    @Autowired
    private RsApplicationDAO rsApplicationDAO;
    @Autowired
    private RsFileDetailsDAO rsFileDetailsDAO;
    @Autowired
    private RsFileDetailsDAO fileDetailsDAO;
    @Autowired
    private RsUserTagRelationDAO userTagRelationDAO;
    @Autowired
    private RsTagDAO tagDAO;


    /**
     * 推荐资源列表
     *
     * @param param
     * @return
     */
    @Override
    public R resourcesList(Map<String, Object> param) {
        //reUserId,page,limit,order,sidx
        param.put("page", param.get("offset"));
        param.remove("offset");
        String reUserId = param.get("rsUserId").toString();
        List<Map<String, Object>> list = new ArrayList<>();
        //订阅id
        List<String> subLists = userTagRelationDAO.getUserSubId(reUserId);
        LinkedHashSet subList = new LinkedHashSet(subLists);

        if (param.get("channelId") != null) {
            //我的订阅
            if (param.get("channelId").toString().equals("1")) {
                param.remove("channelId");
                if (!subList.isEmpty()) {
                    param.put("tagList", subList);
                } else {
                    return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 10, 0));
                }
                Query query = new Query(param);
                int total = 0;
                list = rsFileDetailsDAO.getRecommendList2(query);
                total = rsFileDetailsDAO.getRecommendTotal2(param);
                addTagName(list);
                PageUtils pageUtils = new PageUtils(list, total, query.getLimit(), query.getCurrPage());
                //
                return R.ok().put("page", pageUtils);
            }
            Integer channelId = Integer.parseInt(param.get("channelId").toString());
            RsTagEntity tagEntity = tagDAO.queryObject(channelId);
            if (tagEntity != null) {
                param.put("tagId", channelId);
                param.put("channelId", tagEntity.getChannelId());
            }
        }
        //不感兴趣类型
        param.put("isInterested", 1);
        List<String> tagStrList = rsFileDetailsDAO.getTags(param);
        if (!tagStrList.isEmpty()) {
            param.put("notTagList", tagStrList);
        }
        param.remove("isInterested");
        param.put("isCollected", 1);
        tagStrList = rsFileDetailsDAO.getTags(param);
        if (!tagStrList.isEmpty()) {
            List<String> tags = new ArrayList<>();
            tagStrList.removeIf(s -> "0".equals(s));
            for (String s : tagStrList) {
                String[] strings = s.split(",0,");
                if (strings.length > 0) {
                    for (String s1 : strings) {
                        String[] tagStr = StringUtils.split(s1, ",");
                        tags.add(tagStr[tagStr.length - 1]);
                    }
                }
            }
            subList.addAll(tags);
        }
        if (!subList.isEmpty()) {
            LinkedHashSet subList1 = new LinkedHashSet(subList);
            param.put("subList", subList1);
        }

        Query query = new Query(param);
        int total = 0;
        //订阅+收藏+不感兴趣
        list = rsFileDetailsDAO.getRecommendList(query);

        if (list.isEmpty()) {
            //按用户属性查询
            param.remove("isInterested");
            param.remove("isCollected");
            param.put("recentlyCeen", 1);
            tagStrList = rsFileDetailsDAO.getTags(param);
            if (!tagStrList.isEmpty()) {
                param.put("tagList", tagStrList);
                query.put("tagList", tagStrList);
            }

            list = rsFileDetailsDAO.getRecommendList1(query);
            if (list.isEmpty()) {
                list = rsFileDetailsDAO.getRecommendList2(query);
                total = rsFileDetailsDAO.getRecommendTotal2(param);
            } else {
                total = rsFileDetailsDAO.getRecommendTotal(param);
            }
        } else {
            total = rsFileDetailsDAO.getRecommendTotal(param);
        }

        //拼接标签名称
        addTagName(list);

        PageUtils pageUtils = new PageUtils(list, total, query.getLimit(), query.getCurrPage());
        //
        return R.ok().put("page", pageUtils);
    }

    private void addTagName(List<Map<String, Object>> list) {
        for (Map<String, Object> mm : list) {
            String tagsName = "";
            if (mm.get("tagsName") != null) {
                tagsName = mm.get("tagsName").toString();
                String[] ids = StringUtils.split(tagsName, ",");
                StringBuffer stringBuffer = new StringBuffer("");
                if (ids[0].equals("0") && ids.length > 4) {
                    for (int i = 1; i < 5; i++) {
                        RsTagEntity tagEntity = tagDAO.queryObject(Integer.parseInt(ids[i]));
                        if (tagEntity != null) {
                            if (i != 4 && i != ids.length - 1) {
                                stringBuffer.append(tagEntity.getTagName() + "|");
                            } else {
                                stringBuffer.append(tagEntity.getTagName());
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < ids.length; i++) {
                        RsTagEntity tagEntity = tagDAO.queryObject(Integer.parseInt(ids[i]));
                        if (tagEntity != null) {
                            if (i != ids.length - 1) {
                                stringBuffer.append(tagEntity.getTagName() + "|");
                            } else {
                                stringBuffer.append(tagEntity.getTagName());
                            }
                        }
                    }
                }
                mm.put("tagsName", stringBuffer.toString());
            }
        }
    }

    /**
     * 应用中心
     *
     * @param param
     * @return
     */
    @Override
    public R applicationList(Map<String, Object> param) {

        List<Map<String, Object>> list = rsApplicationDAO.getUserApp(param);
        if (list.isEmpty()) {
            return R.ok().put("list", new ArrayList<>());
        }
        return R.ok().put("list", list);
    }

    /**
     * 热门榜
     *
     * @param param
     * @return
     */
    @Override
    public R hotResourceList(Map<String, Object> param) {
        if (param.get("channelId") != null) {
            if (param.get("channelId").toString().equals("1")) {
                param.remove("channelId");
            }
        }
        List<Map<String, Object>> list = rsFileDetailsDAO.getHotList(param);
        if (list.isEmpty()) {
            return R.ok().put("list", new ArrayList<>());
        }

        for (Map<String, Object> mm : list) {
            String tagsName = "";
            if (mm.get("tags") != null) {
                tagsName = mm.get("tags").toString();
                String[] ids = StringUtils.split(tagsName, ",");
                if (ids[0].equals("0") && ids.length > 1) {
                    RsTagEntity tagEntity = tagDAO.queryObject(Integer.parseInt(ids[2]));
                    if (tagEntity != null) {
                        mm.put("tagsName", tagEntity.getTagName());
                    }
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < ids.length; i++) {
                        RsTagEntity tagEntity = tagDAO.queryObject(Integer.parseInt(ids[i]));
                        if (tagEntity != null) {
                            if (i != ids.length - 1) {
                                stringBuffer.append(tagEntity.getTagName() + "|");
                            } else {
                                stringBuffer.append(tagEntity.getTagName());
                            }
                        }
                    }
                    mm.put("tagsName", stringBuffer.toString());
                }
            }
        }


        return R.ok().put("list", list);
    }

    /**
     * 优选
     *
     * @param param
     * @return
     */
    @Override
    public R optimizationResourceList(Map<String, Object> param) {
        if (param.get("channelId") != null) {
            if (param.get("channelId").toString().equals("1")) {
                param.remove("channelId");
            }
        }

        List<Map<String, Object>> list = rsFileDetailsDAO.getPreferred(param);

        if (list.isEmpty()) {
            return R.ok().put("list", new ArrayList<>());
        }

        return R.ok().put("list", list);
    }

}
