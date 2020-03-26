package com.nugget.common.utils;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class HttpClientSendMsg {

    private static Logger logger = LoggerFactory.getLogger(HttpClientSendMsg.class);

    public String doPost(String url, String params) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        //返回消息
        String jsonString = null;
        try {

            try {
                response = httpclient.execute(httpPost);
            } catch (IOException e) {
                logger.error("发送Post请求失败。url:"+url+" "+e.toString());
                return "";
            }
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();

                try {
                    jsonString = EntityUtils.toString(responseEntity,"UTF-8");
                    logger.info("send doPost result."+jsonString);
                } catch (IOException e) {
                    logger.error("解析信息错误，error:"+e.toString());
                }
                return jsonString;
            }
            else{
                logger.error("请求返回:"+state+"("+url+")");
            }
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonString;
    }

    /**
     * 获取在线用户数
     * @return
     */
    public int  getOnlineUserNum(){

        String baseURL =  Constant.BaseURL;
        Map<String,Object> params = new HashMap<>();
        params.put("cmd",17);
        params.put("type",0);
        params.put("userid","admin");
        String body = JSON.toJSONString(params);
        String  result = doPost(baseURL,body);
        if(result != ""){
            Map<String, Object> maps = parseJSONMap(result);
            Integer code = Integer.valueOf(maps.get("code").toString());
            if(code != null && code == 10005){
                Map<String,Object> dataMap = parseJSONMap(maps.get("data").toString());
                if(dataMap == null ){ return 0;}
                List<Map<String,Object>> groupsList = (List)dataMap.get("groups");
                if(groupsList.size() <= 0 ){   return 0;}
                Map<String,Object> groupsMap = groupsList.get(0);
                if(groupsMap == null ){   return 0;}
                Integer userNum = ((List)groupsMap.get("users")).size();
                return userNum;
            }
        }
        return 0;
    }

    /**
     * 获取在线用户数集合
     * @return
     */
    public List<Map<String,Object>> getOnlineUser(String baseURL){

        Map<String,Object> params = new HashMap<>();
        params.put("cmd",17);
        params.put("type",0);
        params.put("userid","admin");
        String body = JSON.toJSONString(params);
        String  result = doPost(baseURL,body);
        if(result != ""){
            Map<String, Object> maps = parseJSONMap(result);
            Integer code = Integer.valueOf(maps.get("code").toString());
            if(code != null && code == 10005){
                Map<String,Object> dataMap = parseJSONMap(maps.get("data").toString());
                if(dataMap == null ){ return null;}
                List<Map<String,Object>> groupsList = (List)dataMap.get("groups");
                if(groupsList.size() <= 0 ){   return null;}
                Map<String,Object> groupsMap = groupsList.get(0);
                if(groupsMap == null ){   return null;}
                List<Map<String,Object>> userList = (List)groupsMap.get("users");
                return userList;
            }
        }
        return null;
    }



    public static Map<String, Object> parseJSONMap(String jsonStr){
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k);
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(parseJSONMap(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }
}
