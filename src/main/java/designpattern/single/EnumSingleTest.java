package designpattern.single;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.single
 * @author:xiaoyige
 * @createTime:2021/11/13 22:04
 * @version:1.0
 */
public class EnumSingleTest {
    public static void main(String[] args) {
        SingleEnum instance = SingleEnum.INSTANCE;
        SingleEnum instance1 = SingleEnum.INSTANCE;
        System.out.println(instance == instance1);
    }
}

/**
 * 使用枚举
 * 不存在多线程问题、能防止反序列化重新创建新的对象
 *
 * Eff  中推荐写的方式
 */
enum SingleEnum {
    INSTANCE
}
