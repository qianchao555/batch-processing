package com.java8;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @ClassName GougudingliStream
 * @Author qianchao
 * @Date 2022/12/26
 * @Version  V1.0
 **/
public class GougudingliStream {
    public static void main(String[] args) {
        Stream<int[]> stream = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a ->
                        //这里应该从a开始，否则会造成重复的三元数（3，4，5）、（4，3，5）等等
                       IntStream.rangeClosed(1, 100)
                                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                                .mapToObj(b ->
                                        new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
                );

        stream.limit(20)
                .forEach(t ->
                        System.out.println(t[0] + ", " + t[1] + ", " + t[2]));


        //方式2：优化了一些，方式一要进行2次平方根
        Stream<double[]> pythagoreanTriples2 =
                IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(a ->
                                IntStream.rangeClosed(a, 100)
                                        .mapToObj(
                                                b -> new double[]{a, b, Math.sqrt(a*a + b*b)})
                                        .filter(t -> t[2] % 1 == 0));
    }

    @Test
    //有状态的斐波那契数
    public void testIntSupplier(){
        //IntSupplier此对象有可变的状态：它在两个实例变量中记录了前一个斐波纳契项和当前的斐波纳契项
        IntSupplier fib = new IntSupplier(){
            private int previous = 0;
            private int current = 1;

            //调用时，会改变对象的状态
            public int getAsInt(){
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        //generate、iterate区别在于一个是有状态一个是无状态的，我们应该才有始终不变的方法以便并行处理流并保持结果正确，即：用iterate
        IntStream.generate(fib).limit(10).forEach(System.out::print);
    }

}
