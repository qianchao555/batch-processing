package util.beansutildemo;

import util.BeansUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Demo
 * @Author qianchao
 * @Date 2021/12/21
 * @Version V1.0
 **/
public class Demo {
   private static final Long a=Long.valueOf(1L);
   private static final Long b=1L;
    private static int v=123;  //类加载过程的准备阶段 赋予默认值0，类实例化时赋值123
    private int my; //实例化对象时，分配内存阶段赋予默认值
    public static void main(String[] args) {
        Student student1=new Student();
        student1.setAge(1);
        student1.setName("qc");
        List<Student>list=new ArrayList<>();
        list.add(student1);
        List<Student> list2 = BeansUtil.copyPropertiesByList(list, Student.class);
        System.out.println(list2.toString());

    }
}
