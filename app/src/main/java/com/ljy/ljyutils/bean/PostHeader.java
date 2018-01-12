package com.ljy.ljyutils.bean;

import com.ljy.util.LjyEncryUtil;
import com.ljy.util.LjyTimeUtil;

/**
 * Created by Mr.LJY on 2018/1/12.
 */

public class PostHeader {
    private String appId;
    private String appSecret;
    private String accessToken;
    private String axid;
    private String timeStamp;
    private String password;

    public PostHeader(String appId, String appSecret,String safeCode,String axid,String password) {
        this.appId = appId;
        this.axid = axid;
        this.password = password;
        this.appSecret = appSecret;
        this.timeStamp = LjyTimeUtil.timestampToDate(System.currentTimeMillis(),"yyyyMMddHHmmss");
        String row="appId:"+appId+"|appSecret:"+appSecret+"|timeStamp:"+timeStamp+"|"+safeCode;
        this.accessToken= LjyEncryUtil.getMD5(row);
    }
}
