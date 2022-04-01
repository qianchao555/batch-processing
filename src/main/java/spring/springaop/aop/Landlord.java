package spring.springaop.aop;

import org.springframework.stereotype.Component;

/**
 * @ClassName Landlord
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
@Component
public class Landlord {

    public Object service(){
        System.out.println("签合同");
        System.out.println("收房租");
        return "234";
    }
}
