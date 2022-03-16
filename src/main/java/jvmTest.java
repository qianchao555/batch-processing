import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/3/16 22:43
 * @version:1.0
 */
public class jvmTest {
    public static void main(String[] args) {
        String str=new String("1");
        String intern = str.intern();
        String str2="1";
        System.out.println(intern==str2);
    }

    @Test
    public void testString(){
        String s1 = new String("1") + new String("1");
        String intern = s1.intern();
        String s2 = "11";
        System.out.println(intern == s2);
    }
}
