package lamda;

@FunctionalInterface
public interface ConsumerInterface<T> {
/**
 * @ClassName ConsumInterface
 * @Author qianchao
 * @Date 2021/11/1 
 * @Version java8 V1.0
 **/
int accept(T t);
}
