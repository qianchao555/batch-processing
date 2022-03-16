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
        str.intern();//这里发现字符串常量池中已经有了"1"这个对象
        String str2="1";//这里指向的是字符串常量池中的对象
        System.out.println(str.intern()==str2);//true
    }

    @Test
    public void testString(){
        //new String(“a”) + new String(“b”)
        // 在堆中创建了三个字符串对象（a字符串对象，b字符串对象，ab字符串对象），
        //在堆中用两个new创建了两个String对象，然后通过StringBuilder对象调用append方法进行两次字符串的添加操作，最后用StringBuilder的toString方法进行返回了一个堆中的字符串对象


        // 在堆中的字符串常量池中创建了2个字符串对象（a字符串对象，b字符串对象），一共创建了5个对象

        String s1 = new String("2") + new String("2");//这里编译时常量池中存放的是new String("2") ,并没有生成new String("22")
        s1.intern(); //首先没有发现"22"这个字符串对象，所以复制一份堆对象的引用在字符串常量池中，所以，此时字符串常量池中存放的"22"是s1这个引用
        String s2 = "22";//所以这个22指向的是s1这个引用指向的对象，所以是相同的
        System.out.println(s1 == s2);
    }

    @Test
    public void testString2(){
        String s1 = new String("a") + new String("a");
        s1.intern();
        String s2 = "aa";
        System.out.println(s1 == s2);
    }
}
