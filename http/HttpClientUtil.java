package com.yj.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * Http客户端 工具类
 */
public class HttpClientUtil {

    private HttpClientUtil(){
        httpClient = HttpClientBuilder.create().build();
    }

    /**
     * 单例构件类
     * @author elon
     * @version 2018年2月26日
     */
    private static class HttpClientUtilBuilder {
        private static HttpClientUtil instance = new HttpClientUtil();
    }

    /*
     * 获取单例对象
     */
    public static HttpClientUtil instance() {
        return HttpClientUtilBuilder.instance;
    }

    private  CloseableHttpClient httpClient;



    public  CloseableHttpClient getHttpClient(){
        return httpClient;
    }

    /**
     * GET---有参测试 (方式一:手动在url后面加上参数)
     *
     * @date 2018年7月13日 下午4:19:23
     */
    public void doGetWayByParams(String url, Map<String,Object> paramMap) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = getHttpClient();

        // 参数
        StringBuffer params = new StringBuffer();
        try {
            // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
            for(Map.Entry<String,Object> entry : paramMap.entrySet()){
                params.append(entry.getKey());
                params.append("=");
                params.append(URLEncoder.encode((String)entry.getValue(), "utf-8"));
                params.append("&");
            }
            params.deleteCharAt(params.length()-1);
//            params.append("name=" + URLEncoder.encode("&", "utf-8"));
//            params.append("&");
//            params.append("age=24");
        } catch (UnsupportedEncodingException e1) {
            LOGINFO.error("不支持的编码异常：",e1);
        }

        // 创建Get请求
        HttpGet httpGet = new HttpGet(url + "?" + params);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(5000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();

            // 将上面的配置信息 运用到这个Get请求里
            httpGet.setConfig(requestConfig);

            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);

            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            LOGINFO.info("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                LOGINFO.info("响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                LOGINFO.info("响应内容为:" + EntityUtils.toString(responseEntity,StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            LOGINFO.error("get请求异常：",e);
        }  finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOGINFO.error("关闭资源异常：",e);
            }
        }
    }


    /**
     * POST---有参测试(普通参数 + 对象参数)
     *
     * @date 2018年7月13日 下午4:18:50
     */
    public void doPostWayThree(String url, Map<String,Object> paramMap) {

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = getHttpClient();

        // 创建Post请求
        // 参数
        StringBuffer params = new StringBuffer();
        try {
            // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
            for(Map.Entry<String,Object> entry : paramMap.entrySet()){
                params.append(entry.getKey());
                params.append("=");
                params.append(URLEncoder.encode((String)entry.getValue(), "utf-8"));
                params.append("&");
            }
            params.deleteCharAt(params.length()-1);
        } catch (Exception e1) {
            LOGINFO.error("URL构建异常：",e1);
        }

        HttpPost httpPost = new HttpPost(url + "?" + params);
        // HttpPost httpPost = new
        // HttpPost("http://localhost:12345/doPostControllerThree1");

        // 创建user参数
        User user = new User();
        user.setName("潘晓婷");
        user.setAge(18);
        user.setGender("女");
        user.setMotto("姿势要优雅~");

        // 将user对象转换为json字符串，并放入entity中
        StringEntity entity = new StringEntity(GsonUtil.objectToJson(user), "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            LOGINFO.info("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                LOGINFO.info("响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                LOGINFO.info("响应内容为:" + EntityUtils.toString(responseEntity,StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            LOGINFO.error("post请求异常：",e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOGINFO.error("关闭资源异常：",e);
            }
        }
    }




}
