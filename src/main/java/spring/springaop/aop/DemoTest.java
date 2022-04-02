package spring.springaop.aop;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * @ClassName DemoTest
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
@SpringBootApplication
public class DemoTest {

    public static void main(String[] args) {
//        SpringApplication springApplication=new SpringApplication(DemoTest.class);
//        springApplication.setBannerMode(Banner.Mode.OFF);
//        springApplication.run(args);

//            SpringApplication.run(DemoTest.class, args);

        SpringApplicationBuilder sab= (SpringApplicationBuilder) new SpringApplicationBuilder(DemoTest.class)
                .bannerMode(Banner.Mode.OFF).run(args);

    }

}
