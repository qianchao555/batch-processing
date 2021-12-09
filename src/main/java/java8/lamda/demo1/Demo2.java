package java8.lamda.demo1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/9 20:33
 * @version:1.0
 */
public class Demo2 {
    List<Employee> employeeList = Arrays.asList(
            new Employee("qc", 111.6, 34),
            new Employee("qc1", 222.6, 4),
            new Employee("qc2", 333.6, 34),
            new Employee("qc3", 444.6, 9),
            new Employee("qc4", 555.6, 89),
            new Employee("qc5", 666.6, 8));

    @Test
    public void sort() {
        Collections.sort(employeeList, (e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return Integer.compare(e1.getAge(), e2.getAge());
            }
        });
        employeeList.stream().forEach(System.out::println);
    }

    /**
     * 对两个Long进行操作
     *
     * @param l1
     * @param l2
     * @param myFunction
     * @return
     */
    public void op(Long l1, Long l2, MyFunction<Long, Long> myFunction) {
        Long handler = myFunction.handler(l1, l2);
        System.out.println(handler);
    }

    @Test
    public void handleTest(){
        op(100l,200l,(x,y)->{
           return x+y;
        });
    }
}
