package stream.test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName MyTest
 * @Author qianchao
 * @Date 2021/11/2
 * @Version OPRA V1.0
 **/
public class MyTest {
    public static void main(String[] args) {
        List<String> stringList= Arrays.asList("abc","bcd","cde","def");
        Stream<String> abc = stringList.stream().filter(x -> x.equals("abc"));
        List<String> l = abc.collect(Collectors.toList());
        System.out.println(l);
        //map:映射每个元素到对应的结果.它会接受一个函数作为参数。这个函数会被应用到每个元素上=>对流中每一个元素应用函数
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        List<Integer> collect = numbers.stream().map(num -> num * num).distinct().collect(Collectors.toList());
        System.out.println(collect);
        //filter
        List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> bc1 = strings.stream().filter(x -> x.equals("bc")).map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(bc1);
        //统计
        List<Integer> nList = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        IntSummaryStatistics intSummaryStatistics = nList.stream().mapToInt(x -> x).summaryStatistics();
        System.out.println(intSummaryStatistics.getSum());

    }

}
