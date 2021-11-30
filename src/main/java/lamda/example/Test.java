package lamda.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2021/11/2
 * @Version OPRA V1.0
 **/
public class Test {
    public static void main(String[] args) {
        List<Employee> employeeList = new ArrayList<>();
        Employee employee = new Employee("qc", 15789.0, 34);
        Employee employee1 = new Employee("qc1", 789.0, 34);
        Employee employee2= new Employee("qc2", 16549.0, 34);
        Employee employee3= new Employee("qc3", 1787.0, 34);
        Employee employee4 = new Employee("qc4", 1599.0, 34);
        Employee employee5 = new Employee("qc5", 1.0, 34);
        employeeList.add(employee);
        employeeList.add(employee1);
        employeeList.add(employee2);
        employeeList.add(employee3);
        employeeList.add(employee4);
        employeeList.add(employee5);
//        employeeList.forEach(n->System.out.print(n));
//        employeeList.forEach(System.out::println);
        //分组
        Map<String, List<Employee>> collect = employeeList.stream().collect(Collectors.groupingBy(e -> e.getName()));
        System.out.println(collect);
        System.out.println("=======================================================================================");


        //指定k-v  v是对象中某个属性值
        Map<String, Integer> collect1 = employeeList.stream().collect(Collectors.toMap(Employee::getName, Employee::getAge));
        collect.keySet().forEach(key->System.out.println(key+":"+collect.get(key)));


    }

}
