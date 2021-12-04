package spring.circleexception.springinner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/4 22:40
 * @version:1.0
 */
@Component
@Scope("prototype")
public class B {
    @Autowired
    private A a;

    public B() {
        System.out.println("B的无参构造");
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}
