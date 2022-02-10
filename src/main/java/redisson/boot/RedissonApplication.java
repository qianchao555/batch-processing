package redisson.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/29 22:58
 * @version:1.0
 */

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,scanBasePackages = "redisson.*")
//@ComponentScan(basePackages = "*.*")
public class RedissonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedissonApplication.class, args);
    }

}
