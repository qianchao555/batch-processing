package springaop.aop;

import org.springframework.stereotype.Component;

/**
 * @ClassName Landlord
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
@Component
public class Landlord {

    public void service(){
        System.out.println("签合同");
        System.out.println("收房租");
    }
}
