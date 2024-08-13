package com.knowledge.java8.stream.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/1/8 21:14
 * @version:1.0
 */
public class ChatGptDemo {


        public static void main(String[] args) {
            Map<String,String> headers = new HashMap<String,String>();
            headers.put("Content-Type","application/json;charset=UTF-8");

            JSONObject json = new JSONObject();
            //选择模型
            json.set("model","text-davinci-003");
            //添加我们需要输入的内容
            json.set("prompt","Java程序员总结300字");
            json.set("temperature",0.9);
            //tokens的过期时间？哪种茶叶好喝哪种茶叶好喝
            json.set("max_tokens",2048);
            json.set("top_p",1);
            json.set("frequency_penalty",0.0);
            json.set("presence_penalty",0.6);

//            HttpResponse response = HttpRequest.post("https://api.openai.com/v1/completions")
//                    .headerMap(headers, false)
//                    .bearerAuth("填写自己注册的token")
//                    .body(String.valueOf(json))
//                    .timeout(5 * 60 * 1000)
//                    .execute();

            HttpResponse response = HttpRequest.post("https://api.openai.com/v1/completions")
                    .headerMap(headers, false)

                    .bearerAuth("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")
                    .body(JSON.toJSONString(json))
                    .timeout(5 * 60 * 1000)
                    .execute();



//            System.out.println(response.body());
            String body = response.body();
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(body);

            JSONArray choices = (JSONArray) jsonObject.get("choices");
            com.alibaba.fastjson.JSONObject jsonObject1 = choices.getJSONObject(0);
            String text = (String) jsonObject1.get("text");
            System.out.println(text);


        }



}
