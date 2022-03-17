package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JVMTest
 * @Author qianchao
 * @Date 2022/3/15
 * @Version OPRA V1.0
 **/
public class JVMTest {
    static class A{}
    public static void main(String[] args) {
        List<Object> list=new ArrayList<>();
        int a=1;
        while(a==1){
            list.add(new A());
        }
    }

}
