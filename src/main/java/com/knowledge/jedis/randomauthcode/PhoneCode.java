package com.knowledge.jedis.randomauthcode;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * 模拟手机验证码
 *
 * @author:xiaoyige
 * @createTime:2021/12/19 19:20
 * @version:1.0
 */
public class PhoneCode {
    public static void main(String[] args) {
        //模拟验证码发送
//        verifyCode("17856453432");

        getRedisCode("17856453432", "077121");
    }

    //生成六个随机数
    public static String getCode() {
        Random random = new Random();
        StringBuilder sbCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int code = random.nextInt(10);
            sbCode.append(code);
        }
        return sbCode.toString();
    }

    //每个手机每天只能发送三次，验证码过期时间2分钟
    public static void verifyCode(String phoneNo) {
        //创建jedis对象
        Jedis jedis = new Jedis("主机ip", 6379);
        jedis.auth("密码");
        //手机发送次数的key
        String countKey = "VerifyCode" + phoneNo + ":count";
        String count = jedis.get(countKey);
        if (count == null) {
            //第一次发送
            jedis.setex(countKey, 24 * 60 * 60, "1");
        } else if (Integer.parseInt(count) <= 2) {
            //发送次数+1
            jedis.incr(countKey);
        } else if (Integer.parseInt(count) > 2) {
            //今天没有发送次数了
            System.out.println("今天发送次数已经超过三次了。。。");
            jedis.close();
            return;
        }
        //验证码过期时间的key
        String codeKey = "VerifyCode" + phoneNo + ":code";
        String vCode = getCode();
        jedis.setex(codeKey, 120, vCode);
        jedis.close();
    }

    //校验验证码,从redis获取验证码
    public static void getRedisCode(String phoneNo, String code) {
        Jedis jedis = new Jedis("主机ip", 6379);
        jedis.auth("密码");
        String codeKey = "VerifyCode" + phoneNo + ":code";
        String redisCode = jedis.get(codeKey);
        if (redisCode.equals(code)) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
        jedis.close();
    }
}
