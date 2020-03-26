package com.nugget.modules.rs.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nugget.common.utils.*;
import com.nugget.modules.rs.dao.*;
import com.nugget.modules.rs.entity.RsFileDetailsEntity;
import com.nugget.modules.rs.entity.RsTagEntity;
import com.nugget.modules.rs.service.ResourcesService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by LIUHAO
 * Date:2020/2/11
 */
@Service("ResourcesService")
public class ResourcesServiceImpl implements ResourcesService {

    @Autowired
    private RsFileDetailsDAO fileDetailsDAO;
    @Autowired
    private RsUserTagRelationDAO userTagRelationDAO;
    @Autowired
    private RsUserResourcesDAO userResourcesDAO;
    @Autowired
    private RsTagDAO tagDAO;
    @Autowired
    private ChannelDao channelDao;


    /**
     * 搜索
     *
     * @param param
     * @return
     */
    @Override
    public R search(Map<String, Object> param) {
        param.put("page", param.get("offset"));
        param.remove("offset");
        String keywords = param.get("keywords").toString();
        if (NuStringUtils.isBlank(keywords)) {
            return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 0, 0));
        }
        List<String> keywordsList = Arrays.asList(keywords.split(" "));

        param.put("keywordsList", keywordsList.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList()));


        Query query = new Query(param);
        List<Map<String, Object>> list = fileDetailsDAO.search(query);

        if (list.isEmpty()) {
            return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 0, 0));
        }
        //拼接标签名称
        addTagName(list);
        PageUtils pageUtils = new PageUtils(list, fileDetailsDAO.search(param).size(), query.getLimit(), query.getCurrPage());
        return R.ok().put("page", pageUtils);
    }

    /**
     * 资源列表
     *
     * @param param
     * @return
     */
    @Override
    public R resourcesList(Map<String, Object> param) {
        param.put("page", param.get("offset"));
        param.remove("offset");
        Query query = new Query(param);
        List<Map<String, Object>> list = fileDetailsDAO.resourcesList(query);
        if (list.isEmpty()) {
            return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 0, 0));
        }

        for (Map<String, Object> mm : list) {
            String tagsName = "";
            if (mm.get("tagsName") != null) {
                tagsName = mm.get("tagsName").toString();
                String[] ids = StringUtils.split(tagsName, ",");
                StringBuffer stringBuffer = new StringBuffer("");
                for (int i = 2; i < ids.length; i++) {
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
        PageUtils pageUtils = new PageUtils(list, fileDetailsDAO.resourcesList(param).size(), query.getLimit(), query.getCurrPage());
        return R.ok().put("page", pageUtils);

    }


    /**
     * 资源详情
     *
     * @param param
     * @return
     */
    @Override
    public R info(String resourcesId, String rsUserId) {
        Map<String, Object> map = fileDetailsDAO.getInfo(resourcesId, rsUserId);
        if (map.get("tags") != null && !"".equals(map.get("tags").toString())) {
            String tagsName = tagDAO.getTagsName(map.get("tags").toString());
            map.put("tagsName", tagsName);
        } else {
            map.put("tagsName", "");
        }
        return R.ok().put("fileDetail", map);
    }

    /**
     * 相关资源推荐
     *
     * @param param
     * @return
     */
    @Override
    public R relationResources(Map<String, Object> param) {
        param.put("page", param.get("offset"));
        param.remove("offset");
        RsFileDetailsEntity fileDetailsEntity = fileDetailsDAO.queryByResourcesId(Long.parseLong(param.get("resourcesId").toString()));
        if (NuStringUtils.isBlank(fileDetailsEntity.getTags())) {
            return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 0, 0));
        }
        String[] strings = fileDetailsEntity.getTags().split(",0,");
        List<String> tagList = new ArrayList<>();
        if (strings.length > 0) {
            for (String s1 : strings) {
                String[] tagStr = StringUtils.split(s1, ",");
                tagList.add(tagStr[tagStr.length - 1]);
            }
        }

        param.put("tagList", new HashSet<String>(tagList));
//		param.put("tagList",tagList);
        param.put("channelId", fileDetailsEntity.getChannelId());
        Query query = new Query(param);
        List<Map<String, Object>> list = fileDetailsDAO.getRelResources(query);
        if (list.isEmpty()) {
            if (strings.length > 0) {
                for (String s1 : strings) {
                    String[] tagStr = StringUtils.split(s1, ",");
                    if (tagStr != null && tagStr.length > 2) {
                        tagList.add(tagStr[tagStr.length - 2]);
                    }
                }
            }
            param.put("tagList", tagList);
            list = fileDetailsDAO.getRelResources(query);
            if (list.isEmpty()) {
                return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 0, 0));
            }
        }
        for (Map<String, Object> mm : list) {
            String tagsName = "";
            if (mm.get("tagsName") != null) {
                String tagsIds = mm.get("tagsName").toString();
                String[] ids = StringUtils.split(tagsIds, ",");
                String id = ids[ids.length - 1];
                if (!id.equals("0")) {
                    RsTagEntity tagEntity = tagDAO.queryObject(Integer.parseInt(id));
                    if (tagEntity != null) {
                        tagsName = tagEntity.getTagName();
                    }
                }

                mm.put("tagsName", tagsName);
            }
        }
        int total = fileDetailsDAO.getRelResources(param).size();
        PageUtils pageUtils = new PageUtils(list, total, query.getLimit(), query.getCurrPage());
        return R.ok().put("page", pageUtils);
    }

    @Override
    public R bestTeacherResources(Map<String, Object> param) {
        param.put("page", param.get("offset"));
        param.remove("offset");
        RsFileDetailsEntity fileDetailsEntity = fileDetailsDAO.queryByResourcesId(Long.parseLong(param.get("resourcesId").toString()));
        if (fileDetailsEntity.getAuthor() == null) {
            return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 0, 0));
        }
        param.put("author", fileDetailsEntity.getAuthor());
        param.put("channelId", fileDetailsEntity.getChannelId());

        Query query = new Query(param);
        List<Map<String, Object>> list = fileDetailsDAO.getRelResources(query);
        if (list.isEmpty()) {
            return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 0, 0));
        }
        for (Map<String, Object> mm : list) {
            String tagsName = "";
            if (mm.get("tagsName") != null) {
                tagsName = mm.get("tagsName").toString();

                String[] ids = StringUtils.split(tagsName, ",");
                String id = ids[ids.length - 1];
                if (!id.equals("0")) {
                    RsTagEntity tagEntity = tagDAO.queryObject(Integer.parseInt(id));
                    if (tagEntity != null) {
                        tagsName = tagEntity.getTagName();
                    }
                }
                mm.put("tagsName", tagsName);
            }
        }

        PageUtils pageUtils = new PageUtils(list, fileDetailsDAO.getRelResources(param).size(), query.getLimit(), query.getCurrPage());
        return R.ok().put("page", pageUtils);

    }

    @Override
    public R mayLikeResources(Map<String, Object> param) {
        param.put("page", param.get("offset"));
        param.remove("offset");
        String rsUserId = param.get("rsUserId").toString();
        String resourcesId = param.get("resourcesId").toString();
        RsFileDetailsEntity fileDetailsEntity = fileDetailsDAO.queryByResourcesId(Long.parseLong(resourcesId));
        param.put("channelId", fileDetailsEntity.getChannelId());

        List<String> subList = userTagRelationDAO.getUserSubId(rsUserId);
        List<String> strList = userResourcesDAO.mostViewTagList(rsUserId);
        List<String> mostViewTagList = new ArrayList<>();
        if (!strList.isEmpty()) {
            strList.removeIf(s -> "0".equals(s));
            for (String s : strList) {
                if (s != null && !"".equals(s)) {
                    String[] strings = s.split(",0,");
                    if (strings.length > 0) {
                        for (String s1 : strings) {
                            String[] tagStr = StringUtils.split(s1, ",");
                            mostViewTagList.add(tagStr[tagStr.length - 1]);
                        }
                    }
                }
            }
        }
        if (!mostViewTagList.isEmpty()) {
            param.put("mostViewTagList", mostViewTagList);
            Query query = new Query(param);
            List<Map<String, Object>> list = fileDetailsDAO.getMayLikeResources(query);

            PageUtils pageUtils = new PageUtils(list, fileDetailsDAO.getMayLikeResources(param).size(), query.getLimit(), query.getCurrPage());

            return R.ok().put("page", pageUtils);
        } else {
            if (!subList.isEmpty()) {
                param.put("subList", subList);
                Query query = new Query(param);
                List<Map<String, Object>> list = fileDetailsDAO.getMayLikeResources(query);

                PageUtils pageUtils = new PageUtils(list, fileDetailsDAO.getMayLikeResources(param).size(), query.getLimit(), query.getCurrPage());

                return R.ok().put("page", pageUtils);
            } else {
                return R.ok().put("page", new PageUtils(new ArrayList<>(), 0, 0, 0));
            }

        }
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

    @Override
    public R downloadFile2(String urlPath, String fileName, HttpServletResponse response, int type) throws Exception {

        BufferedOutputStream out = null;
        BufferedInputStream input = null;
        File file = null;
        if (type == 1) {
            //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)

            file = new File(Constant.RE_FILE_URL + urlPath);
        } else if (type == 2) {
            file = new File(Constant.PDF_FILE_URL + urlPath);
        } else if (type == 3) {
            file = new File(Constant.SYS_FILE_URL + urlPath);
        }

        fileName = URLDecoder.decode(fileName, "UTF-8");
        fileName = new String(fileName.getBytes(), "ISO-8859-1");
//在下载菜单文件模板时生成菜单文件模板,应用发布使用
        ParseXmlUtil.createXml();

        if (!file.exists()) {
//			throw new RRException("文件未找到,请联系超级管理员！");
            return R.error("文件未找到,请联系超级管理员！");
        }

        try {

            response.reset();
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
//			response.setContentType("application/octet-streem");

            //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Disposition", "attachment; filename =\"" + fileName + ".xml" + "\"");
            response.setContentLength((int) file.length());
            input = new BufferedInputStream(new FileInputStream(file));
            //3.通过response获取ServletOutputStream对象(out)
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] buffer = new byte[4096];// 缓冲区
            int n = -1;
            //遍历，开始下载
            while ((n = input.read(buffer, 0, 4096)) > -1) {
                out.write(buffer, 0, n);
            }
            out.flush();   //不可少
            response.flushBuffer();//不可少
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return R.ok();
    }

    @Override
    public R saveResources(Map<String, Object> param) {

        List<Map<String, Object>> list = tagDAO.getByType(6, null);

        if (!list.isEmpty()) {
            try {
                for (Map<String, Object> mm : list) {
                    String rqUrl = "http://api.qxt2000.cn/kxvideo/getlist/" + mm.get("tagId") + "/6/100000/1";
                    String res = HttpRUtils.sendGet(rqUrl, new HashMap<>());
                    JSONObject jo = JSONObject.parseObject(res);
                    if (jo.get("result") != null) {

                        JSONArray result = (JSONArray) jo.get("result");
                        for (Object obj : result
                        ) {
                            JSONObject jsonObject = (JSONObject) obj;
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", jsonObject.get("kcname"));
                            map.put("introduction", jsonObject.get("kcname"));
                            map.put("size", 0);
                            map.put("author", jsonObject.get("teacher"));
                            StringBuffer stringBuffer = new StringBuffer(jsonObject.get("kcname") + "");
                            stringBuffer.append("," + jsonObject.get("small"));
                            stringBuffer.append("," + jsonObject.get("unit"));
                            stringBuffer.append("," + jsonObject.get("grade"));
                            stringBuffer.append("," + jsonObject.get("version"));
                            stringBuffer.append("," + jsonObject.get("subject"));
                            map.put("searchFiled", stringBuffer.toString());
                            map.put("thumbAddress", jsonObject.get("imageurl"));
                            map.put("headerImageAddress", jsonObject.get("imageurl"));
                            map.put("resourcesType", 13);
                            map.put("channelId", 6);
                            if (mm.get("levelStr") != null) {
                                map.put("tags", mm.get("levelStr"));
                            }

                            String rqUrl1 = "http://api.qxt2000.cn/kxvideo/videos/" + jsonObject.get("id");
                            String res1 = HttpRUtils.sendGet(rqUrl1, new HashMap<>());
                            JSONObject jo1 = JSONObject.parseObject(res1);
                            if (jo1.get("result") != null) {
                                JSONObject result1 = (JSONObject) jo1.get("result");

                                String rr = "http://p.bokecc.com/player?vid=" +
                                        result1.get("ccid") +
                                        "&siteid=0C728A4805962911&autoStart=true&width=600&height=490&playerid=A8E5C78ED3BA6213&playertype=1";

                                map.put("resourcesAddress", rr);
                            }
                            fileDetailsDAO.saveMap(map);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return R.ok();
    }


    @Override
    public R saveResources1(Map<String, Object> param) {

//        List<Map<String,Object>> list = tagDAO.getByType(4);

//        if(!list.isEmpty()){
        try {
//                for (Map<String,Object> mm:list) {
            String rqUrl = "http://api.qxt2000.cn/kxvideo/xiaoxue";
            String res = HttpRUtils.sendGet(rqUrl, new HashMap<>());
            JSONObject jo = JSONObject.parseObject(res);
            if (jo.get("result") != null) {

                JSONArray result = (JSONArray) jo.get("result");
                for (Object obj : result
                ) {
                    JSONObject jsonObject = (JSONObject) obj;
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", jsonObject.get("title"));
                    map.put("introduction", jsonObject.get("zhishidian_name"));
                    map.put("size", 0);
                    map.put("author", "");
                    StringBuffer stringBuffer = new StringBuffer(jsonObject.get("title") + "");
                    stringBuffer.append("," + jsonObject.get("cid_name"));
                    stringBuffer.append("," + jsonObject.get("kemu_name"));
                    stringBuffer.append("," + jsonObject.get("banben_name"));
                    stringBuffer.append("," + jsonObject.get("nianji_name"));
                    stringBuffer.append("," + jsonObject.get("zhangjie_name"));
                    stringBuffer.append("," + jsonObject.get("zhishidian_name"));
                    map.put("searchFiled", stringBuffer.toString());
                    map.put("thumbAddress", jsonObject.get("image"));
                    map.put("headerImageAddress", jsonObject.get("image"));
                    map.put("resourcesType", 18);
                    map.put("channelId", 6);
                    String zhishidianid = "1" + jsonObject.get("zhishidian_id");
                    String zhangjie = "1" + jsonObject.get("zhangjie_id");
                    String nianji = "1" + jsonObject.get("nianji_id");

                    RsTagEntity tagEntity = tagDAO.queryObject(Integer.parseInt(zhishidianid));
                    if (tagEntity != null) {
                        map.put("tags", tagEntity.getLevelStr());
                    } else {
                        tagEntity = tagDAO.queryObject(Integer.parseInt(zhangjie));
                        if (tagEntity != null) {
                            map.put("tags", tagEntity.getLevelStr());
                        } else {
                            tagEntity = tagDAO.queryObject(Integer.parseInt(nianji));
                            if (tagEntity != null) {
                                map.put("tags", tagEntity.getLevelStr());
                            }
                        }

                    }


                    map.put("resourcesAddress", jsonObject.get("url"));
                    fileDetailsDAO.saveMap(map);


                }

            }


//                }
        } catch (Exception e) {

            e.printStackTrace();

        }


//        }


        return R.ok();
    }

    @Override
    public R saveResources2(Map<String, Object> param) {

        try {

            String rqUrl = "http://119.3.228.197:80/diama-media/cms/eClassClient/eClassResource?androidId=6484b864e344969c&fileType=videos";
            String res = HttpRUtils.sendGet(rqUrl, new HashMap<>());
            JSONObject jo = JSONObject.parseObject(res);
            if (jo.get("list") != null) {

                JSONArray result = (JSONArray) jo.get("list");
                for (Object obj : result
                ) {
                    JSONObject jsonObject = (JSONObject) obj;
                    String vidoType = jsonObject.getString("vidoType");
                    List<Map<String, Object>> tList = tagDAO.getByTagName(vidoType);
                    String tagId = "";
                    if (!tList.isEmpty()) {
                        tagId = tList.get(0).get("tagId").toString();
                    }
                    JSONArray vidoList = (JSONArray) jsonObject.get("vidoList");
                    if (!vidoList.isEmpty()) {
                        for (Object obj1 : vidoList) {
                            JSONObject jsonObject1 = (JSONObject) obj1;
                            Map<String, Object> map = new HashMap<>();

                            map.put("title", jsonObject1.get("vidoName"));
                            map.put("introduction", jsonObject1.get("vidoName"));
                            map.put("resourcesAddress", jsonObject1.get("vidoUrl"));
                            map.put("searchFiled", jsonObject1.get("vidoName"));
                            map.put("thumbAddress", jsonObject1.get("vidoCover"));
                            map.put("headerImageAddress", jsonObject1.get("vidoCover"));
                            map.put("resourcesType", 13);
                            map.put("channelId", 10);
                            map.put("tags", tagId);
                            fileDetailsDAO.saveMap(map);
                        }
                    }

                }

            }


        } catch (Exception e) {

            e.printStackTrace();

        }

        return R.ok();
    }

    @Override
    public R saveResources3(Map<String, Object> param) {

        try {

            String rqUrl = "http://119.3.228.197:80/diama-media/cms/eClassClient/eClassResource?androidId=6484b864e344969c&fileType=videos";
            String res = HttpRUtils.sendGet(rqUrl, new HashMap<>());
            JSONObject jo = JSONObject.parseObject(res);
            if (jo.get("list") != null) {

                JSONArray result = (JSONArray) jo.get("list");
                for (Object obj : result
                ) {
                    JSONObject jsonObject = (JSONObject) obj;
                    String vidoType = jsonObject.getString("vidoType");
                    List<Map<String, Object>> tList = tagDAO.getByTagName(vidoType);
                    String tagId = "";
                    if (!tList.isEmpty()) {
                        tagId = tList.get(0).get("tagId").toString();
                    }
                    JSONArray vidoList = (JSONArray) jsonObject.get("vidoList");
                    if (!vidoList.isEmpty()) {
                        for (Object obj1 : vidoList) {
                            JSONObject jsonObject1 = (JSONObject) obj1;
                            Map<String, Object> map = new HashMap<>();

                            map.put("title", jsonObject1.get("vidoName"));
                            map.put("introduction", jsonObject1.get("vidoName"));
                            map.put("resourcesAddress", jsonObject1.get("vidoUrl"));
                            map.put("searchFiled", jsonObject1.get("vidoName"));
                            map.put("thumbAddress", jsonObject1.get("vidoCover"));
                            map.put("headerImageAddress", jsonObject1.get("vidoCover"));
                            map.put("resourcesType", 13);
                            map.put("channelId", 10);
                            map.put("tags", tagId);
                            fileDetailsDAO.saveMap(map);
                        }
                    }

                }

            }


        } catch (Exception e) {

            e.printStackTrace();

        }

        return R.ok();
    }

    @Override
    public R saveResources4(Map<String, Object> param) {
        try {

            String rqUrl = "http://api.qxt2000.cn/library/theme";
            String res = HttpRUtils.sendGet(rqUrl, new HashMap<>());
            JSONObject jo = JSONObject.parseObject(res);
            if (jo.get("result") != null && jo.get("code").toString().equals("200")) {
                JSONArray result = (JSONArray) jo.get("result");
                for (Object obj : result
                ) {
                    JSONObject jsonObject = (JSONObject) obj;
                    String tId = jsonObject.get("id") + "";
//                String tId = 6+"";
                    String tName = jsonObject.getString("name");
//                String tName = "素材";
                    Map<String, Object> channel = channelDao.getByName(tName);
                    if (channel != null) {
                        String channelId = channel.get("channelId") + "";
                        List<Map<String, Object>> list = tagDAO.getByLevel(5);
                        if (!list.isEmpty()) {
                            for (Map<String, Object> mm : list) {
                                //6
                                List<Map<String, Object>> clist = tagDAO.getByPid(Integer.parseInt(mm.get("tagId").toString()));
                                if (!clist.isEmpty()) {
                                    //7
                                    for (Map<String, Object> mm1 : clist) {
                                        List<Map<String, Object>> cclist = tagDAO.getByPid(Integer.parseInt(mm1.get("tagId").toString()));
                                        if (!cclist.isEmpty()) {
                                            //8
                                            for (Map<String, Object> mm2 : cclist) {
                                                List<Map<String, Object>> ccclist = tagDAO.getByPid(Integer.parseInt(mm2.get("tagId").toString()));
                                                if (!ccclist.isEmpty()) {
                                                    for (Map<String, Object> mm3 : ccclist) {
                                                        this.add(mm3, channelId, tId);
                                                    }
                                                } else {
                                                    this.add(mm2, channelId, tId);
                                                }
                                            }
                                        } else {
                                            this.add(mm1, channelId, tId);
                                        }
                                    }
                                } else {
                                    this.add(mm, channelId, tId);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    private R add(Map<String, Object> mm, String channlId, String tId) {
        try {
            if (!mm.isEmpty()) {

                for (int i = 0; i < 1000; i++) {
                    Integer tagId = Integer.parseInt(mm.get("tagId") + "") - 200000;
                    String rqUrl = "http://api.qxt2000.cn/library/getlist/" + tagId + "/2/1000/" + i + "/" + tId;
                    String res = HttpRUtils.sendGet(rqUrl, new HashMap<>());
                    JSONObject jo = JSONObject.parseObject(res);
                    if (jo.get("result") != null && jo.get("code").toString().equals("200")) {

                        JSONArray result = (JSONArray) jo.get("result");
                        for (Object obj : result
                        ) {
                            JSONObject jsonObject = (JSONObject) obj;
                            Map<String, Object> fileResources = fileDetailsDAO.getByOrginId(jsonObject.get("id") + "");
                            if (fileResources != null) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("orginId", jsonObject.get("id"));
                                String tags = fileResources.get("tags") + "," + mm.get("levelStr");
                                map.put("tags", tags);
                                fileDetailsDAO.updateTags(map);
                            } else {
                                Map<String, Object> map = new HashMap<>();
                                map.put("title", jsonObject.get("name"));
                                map.put("introduction", jsonObject.get("name"));
                                map.put("orginId", jsonObject.get("id"));

                                map.put("searchFiled", jsonObject.get("name"));
//                            map.put("thumbAddress", jsonObject.get("imageurl"));
//                            map.put("headerImageAddress", jsonObject.get("imageurl"));
                                String type = jsonObject.get("ext") + "";
                                if (type.equals(".doc")) {

                                    map.put("resourcesType", 3);
                                }
                                if (type.equals(".docx")) {

                                    map.put("resourcesType", 4);
                                }
                                if (type.equals(".ppt")) {

                                    map.put("resourcesType", 8);
                                }
                                if (type.equals(".pptx")) {

                                    map.put("resourcesType", 9);
                                }
                                if (type.equals(".pdf")) {

                                    map.put("resourcesType", 7);
                                }

                                map.put("channelId", channlId);


                                map.put("downLoadadress", "http://api.qxt2000.cn/library/download/" + jsonObject.get("id"));
                                String rqUrl1 = "http://api.qxt2000.cn/library/info/" + jsonObject.get("id");
                                String res1 = HttpRUtils.sendGet(rqUrl1, new HashMap<>());
                                JSONObject jo1 = JSONObject.parseObject(res1);
                                if (jo1.get("code").toString().equals("200")) {
                                    if (jo1.get("result") != null) {
                                        JSONObject result1 = (JSONObject) jo1.get("result");
                                        map.put("resourcesAddress", result1.getString("viewurl"));
                                        map.put("size", result1.getString("length"));
                                    }
                                }

                                if (mm.get("levelStr") != null) {
                                    map.put("tags", mm.get("levelStr"));
                                }

                                fileDetailsDAO.saveMap1(map);
                            }
                        }
                    } else {
                        break;
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    @Override
    public R saveResources5(Map<String, Object> param) {

        try {
            for (int i = 0; i < 100; i++) {
                String rqUrl = "http://api.qxt2000.cn/kxvideo/getcourselist/108187/1/100/" + i;
                String res = HttpRUtils.sendGet(rqUrl, new HashMap<>());
                JSONObject jo = JSONObject.parseObject(res);
                if (jo.get("result") != null && jo.get("code").toString().equals("200")) {

                    JSONArray result = (JSONArray) jo.get("result");
                    for (Object obj : result
                    ) {
                        JSONObject jsonObject = (JSONObject) obj;
                        Map<String, Object> map = new HashMap<>();
                        map.put("title", jsonObject.get("kcname"));
                        map.put("introduction", jsonObject.get("kcname"));
                        map.put("size", jsonObject.get("length"));
                        if (jsonObject.get("teacher") != null) {
                            JSONObject teacher = (JSONObject) jsonObject.get("teacher");
                            map.put("author", teacher.get("name"));
                        }
                        StringBuffer stringBuffer = new StringBuffer(jsonObject.get("kcname") + "");
                        stringBuffer.append("," + jsonObject.get("small"));
                        stringBuffer.append("," + jsonObject.get("unit"));
                        stringBuffer.append("," + jsonObject.get("grade"));
                        stringBuffer.append("," + jsonObject.get("version"));
                        stringBuffer.append("," + jsonObject.get("subject"));
                        map.put("searchFiled", stringBuffer.toString());
                        map.put("thumbAddress", jsonObject.get("imageurl"));
                        map.put("headerImageAddress", jsonObject.get("imageurl"));
                        map.put("resourcesType", 13);
                        map.put("channelId", 7);
                        List<Map<String, Object>> list = new ArrayList<>();
                        if (jsonObject.get("small") != null) {
                            list = tagDAO.getByType(6, jsonObject.get("small").toString());
                            if (!list.isEmpty()) {
                                map.put("tags", list.get(0).get("levelStr"));
                            }
                        } else {
                            if (jsonObject.get("unit") != null) {
                                list = tagDAO.getByType(5, jsonObject.get("unit").toString());
                                if (!list.isEmpty()) {
                                    map.put("tags", list.get(0).get("levelStr"));
                                }
                            }
                        }

                        String rqUrl1 = "http://api.qxt2000.cn/kxvideo/videos/" + jsonObject.get("id");
                        String res1 = HttpRUtils.sendGet(rqUrl1, new HashMap<>());
                        JSONObject jo1 = JSONObject.parseObject(res1);
                        if (jo1.get("result") != null && jo1.get("code").toString().equals("200")) {
                            JSONObject result1 = (JSONObject) jo1.get("result");
                            String rr = "http://p.bokecc.com/player?vid=" +
                                    result1.get("ccid") +
                                    "&siteid=0C728A4805962911&autoStart=true&width=600&height=490&playerid=A8E5C78ED3BA6213&playertype=1";
                            map.put("resourcesAddress", rr);
                        }
                        fileDetailsDAO.saveMap(map);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok();
    }

}
