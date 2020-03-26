package com.nugget.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LIUHAO
 * Date:2019/2/15
 */
@Configuration
@ConfigurationProperties(prefix = "myprops")
public class Myprops {

    private String appId;

    private String appSecret;

    private Map<String, String> influxDb = new HashMap<>();

    private Map<String, String> websocket = new HashMap<>();

    private Integer baseScore;

    private String nzappId;

    private String nzappSecret;

    private String feignUrl;

    private String influxdbOpenurl;

    private String nlgpassWord;

    private String filePath;

    private String staticFilePath;

    public String getNlgpassWord() {
        return nlgpassWord;
    }

    public void setNlgpassWord(String nlgpassWord) {
        this.nlgpassWord = nlgpassWord;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Map<String, String> getInfluxDb() {
        return influxDb;
    }

    public void setInfluxDb(Map<String, String> influxDb) {
        this.influxDb = influxDb;
    }

    public Map<String, String> getWebsocket() {
        return websocket;
    }

    public void setWebsocket(Map<String, String> websocket) {
        this.websocket = websocket;
    }

    public Integer getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(Integer baseScore) {
        this.baseScore = baseScore;
    }

    public String getNzappId() {
        return nzappId;
    }

    public void setNzappId(String nzappId) {
        this.nzappId = nzappId;
    }

    public String getNzappSecret() {
        return nzappSecret;
    }

    public void setNzappSecret(String nzappSecret) {
        this.nzappSecret = nzappSecret;
    }

    public String getFeignUrl() {
        return feignUrl;
    }

    public void setFeignUrl(String feignUrl) {
        this.feignUrl = feignUrl;
    }

    public String getInfluxdbOpenurl() {
        return influxdbOpenurl;
    }

    public void setInfluxdbOpenurl(String influxdbOpenurl) {
        this.influxdbOpenurl = influxdbOpenurl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStaticFilePath() {
        return staticFilePath;
    }

    public void setStaticFilePath(String staticFilePath) {
        this.staticFilePath = staticFilePath;
    }
}

