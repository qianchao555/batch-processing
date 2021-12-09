package java8.lamda.innerinterface.function;

import java.util.function.Function;

/**
 * @ClassName FunctionTest
 * @Author qianchao
 * @Date 2021/11/2
 * @Version java8 V1.0
 **/
public class FunctionTest {
    public static void main(String[] args) {
        String s="abcde";
        String s1= strHandler(s,(x->x.toUpperCase()));
        String s2= strHandler(s,(x->x.substring(1,2)));

        System.out.println(s1);
        System.out.println(s2);
    }

    /**
     * Function<R,T>  函数型接口 特定功能
     * @param f
     * @param function
     * @return
     */
    public static String strHandler(String f, Function<String ,String>function){
        return function.apply(f);
    }

}
