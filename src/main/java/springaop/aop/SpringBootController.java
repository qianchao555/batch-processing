package springaop.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName SpringBootController
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
@Controller
public class SpringBootController {
    @Autowired
    Landlord landlord;

    @RequestMapping("/hello")
    @ResponseBody
    public void hello(){
        landlord.service();
    }
}
