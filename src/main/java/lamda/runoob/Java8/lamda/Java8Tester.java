package lamda.runoob.Java8.lamda;

public class Java8Tester {
    /**
     * @ClassName Java8Tester
     * @Author qianchao
     * @Date 2021/11/2
     * @Version OPRA V1.0
     **/
    public static void main(String[] args) {
        Java8Tester java8Tester=new Java8Tester();
        MathOperation add=(int a,int b)->{
            return a+b;
        };
        MathOperation sub=(int a,int b)->{
            return a-b;
        };
        System.out.println(java8Tester.opration(5,6,add));
        System.out.println(java8Tester.opration(6,7,sub));

        GreentingService greentingService=(message -> {
            System.out.println("hello,"+message);
        });
        greentingService.sayMessage("qc");
    }
    private int opration(int a,int b,MathOperation mathOperation){
        return mathOperation.operation(a,b);
    }

}

interface MathOperation {
    int operation(int a, int b);
}

interface GreentingService {
    void sayMessage(String message);
}
