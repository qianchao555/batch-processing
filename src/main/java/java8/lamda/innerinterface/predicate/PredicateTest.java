package java8.lamda.innerinterface.predicate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @ClassName PredicateTest
 * @Author qianchao
 * @Date 2021/11/2
 * @Version java8 V1.0
 **/
public class PredicateTest {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("张三", 2500.00, 34),
                new Employee("李四", 4500.00, 45),
                new Employee("lisi", 3500.00, 35)
        );
//        checkSalary(employees, (em) -> em.getSalary() > 2600, (employee -> employee.getAge() > 40));
        List<String> list = addString(Arrays.asList("wo", "hi", " qian chao"), (s) -> s.length() > 3);
        System.out.println(list);
    }

    public static void checkSalary(List<Employee> list, Predicate<Employee> p, Predicate<Employee> p2) {
        Predicate<Employee> andCondition = p.and(p2);
//        for(Employee e:list){
//            //判断 e  是否满足 :e传给em      em->em.getSalary()>2600
//            if(andCondition.test(e)){
//                System.out.println(e.getName());
//            }
//        }
        list.stream()
                .filter(andCondition)
                .forEach(System.out::println);
    }

    //将满足条件的字符串放入List中
    public static List<String> addString(List<String> stringList, Predicate<String> predicate) {
//        List<String> list = new ArrayList<>();
//        for (String s : stringList) {
////            if (predicate.test(s)) {
////                list.add(s);
////            }
////
////        }
        List<String> collect = stringList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
        return collect;
//        return list;
    }
}
