package lamda.supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @ClassName SupplierTest
 * @Author qianchao
 * @Date 2021/11/2
 * @Version OPRA V1.0
 **/
public class SupplierTest {
    public static void main(String[] args) {
        List<Integer> numList=getNumList(6,()-> (int)5);
        for(Integer i:numList){
            System.out.println(i);
        }
    }

    /**
     *  Supplier<T>  供给型接口   //T get(); 小括号无参数
     * @param x
     * @param supplier
     * @return
     */
    public static List<Integer> getNumList(int x, Supplier<Integer> supplier){
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<x;i++){
            //get方法得到数字
            list.add(supplier.get());
        }
        return list;
    }

}
