package lamda.runoob.Java8.lamda;

/**
 * @ClassName PrivateVarTest
 * @Author qianchao
 * @Date 2021/11/2
 * @Version OPRA V1.0
 **/
public class PrivateVarTest {
    public static void main(String[] args) {
    int num=1;
    Converter<Integer,String> converter1=(param)->
        System.out.println(String.valueOf(param+num));
    converter1.convert(2);
    }




}
interface Converter<T1,T2>{
    void convert(int i);
}
