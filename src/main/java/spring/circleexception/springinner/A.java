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
public class A {
    @Autowired
    private B b;

    public A() {
        System.out.println("A的无参构造");
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
