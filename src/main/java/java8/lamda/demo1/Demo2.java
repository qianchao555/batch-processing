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

    /**
     * 拆分string abcDefGhiJklCsv
     * abc_Def_Ghi_Jkl_Csv
     */
    @Test
    public void hhh(){
        String sss="uplReportSabCsv";
        String s = sss.replaceAll("[A-Z]", "_$0");
        System.out.println("S:"+s);
        String[] s1 = s.split("_");
        for (String s2 :s1) {
            System.out.print(s2+" ");
        }

        System.out.println(s1.length);
            if(s1[(s1.length-1)].contains("Csv")){
                System.out.println(s1[(s1.length-1)]);
        }
    }
    @Test
    public void hhh2(){
        String s = "00000123";
        String s1 = String.format("%07d", Integer.valueOf(s));
        System.out.println(s1);
    }
}
