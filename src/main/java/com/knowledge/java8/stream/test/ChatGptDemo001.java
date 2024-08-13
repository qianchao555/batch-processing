package com.knowledge.java8.stream.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/1/8 21:18
 * @version:1.0
 */
public class ChatGptDemo001 {

        public static void main(String[] args) {
//            Map<String,String> headers = new HashMap<String,String>();
//            headers.put("Content-Type","application/json;charset=UTF-8");
//
//            JSONObject json = new JSONObject();
//            //搜索关键字
//            json.set("prompt","漂亮小姐姐");
//            //生成图片数
//            json.set("n",2);
//            //生成图片大小
//            json.set("size","1024x1024");
//            //返回格式
//            json.set("response_format","url");
//
//            //发送请求
//            HttpResponse response = HttpRequest.post("https://api.openai.com/v1/images/generations")
//                    .headerMap(headers, false)
//                    .bearerAuth("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")
//                    .body(String.valueOf(json))
//                    .timeout(5 * 60 * 1000)
//                    .execute();
//
//            System.out.println(response.body());



            Map<String,String> headers = new HashMap<String,String>();
            headers.put("Content-Type","application/json;charset=UTF-8");

            JSONObject json = new JSONObject();
            //搜索关键字
            json.set("prompt","你在哪里上学");
//            //生成图片数
//            json.set("n",2);
//            //生成图片大小
//            json.set("size","1024x1024");
            //返回格式
//            json.set("response_format","data");

            //发送请求
            HttpResponse response = HttpRequest.post("https://api.openai.com/v1/images/generations")
                    .headerMap(headers, false)
                    .bearerAuth("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")
                    .body(String.valueOf(json))
                    .timeout(5 * 60 * 1000)
                    .execute();

            System.out.println(response.body());
        }





}
