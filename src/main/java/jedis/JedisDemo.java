package jedis;

import redis.clients.jedis.Jedis;

/**
 * @author:xiaoyige
 * @createTime:2021/12/19 14:52
 * @version:1.0
 */
public class JedisDemo {
    public static void main(String[] args) {
        //创建jedis对象
        Jedis jedis = new Jedis("远程主机ip", 6379);
        jedis.auth("密码");
        //测试
        String ping = jedis.ping();
        System.out.println(ping);
        jedis.close();
    }
}
