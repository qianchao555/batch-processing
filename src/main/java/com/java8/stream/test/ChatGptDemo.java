package com.java8.stream.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;

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
            json.set("prompt","Java编写helloworld");
            json.set("temperature",0.9);
            //tokens的过期时间？
//            json.set("max_tokens",2048);
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



            System.out.println(response.body());
        }



}
