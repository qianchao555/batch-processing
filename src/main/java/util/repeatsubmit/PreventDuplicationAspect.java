package util.repeatsubmit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @description:防止重复提交 切面类
 * @author:xiaoyige
 * @createTime:2022/4/21 20:44
 * @version:1.0
 */
@Slf4j
@Aspect
@Component
public class PreventDuplicationAspect {
    private static final String point_cut = "execution(public * *..controller..*.*(..))";

    @Autowired
    RedisTemplate redisTemplate;

    @Around(point_cut)
    public Object interceptor(ProceedingJoinPoint p) throws Throwable {

        MethodSignature signature = (MethodSignature) p.getSignature();
        Method method = signature.getMethod();
        if (method != null) {
            log.info("当前执行的方法为：{}.{},参数为：{}",
                    p.getTarget().getClass(), method.getName(), p.getArgs());
        }
        //获取重复提交注解
        PreventDuplication preventDuplication = AnnotatedElementUtils.findMergedAnnotation(method, PreventDuplication.class);

        if (preventDuplication == null) {
            return p.proceed();
        }

        //生成key
        String redisCacheKey = getRedisCacheKey(method, p.getArgs());

        //查询redis里面是否有key的缓存
        if (redisTemplate.hasKey(redisCacheKey)) {
            throw new RuntimeException("数据已经提交，请等待！");
        } else {
            redisTemplate.opsForValue().set(redisCacheKey, "testUser", preventDuplication.expireSecond(), TimeUnit.SECONDS);
        }

        //正常执行方法并返回
        try {
            return p.proceed();
        } catch (Exception e) {
            //确保方法执行异常时，释放限时标记
            redisTemplate.delete(redisCacheKey);
        }
        return null;

    }

    /**
     * 组装key
     *
     * @param method
     * @param args
     * @return
     */
    private String getRedisCacheKey(Method method, Object[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName()).append(":");
        for (Object arg : args) {
            sb.append(arg.toString());
        }
        return sb.toString();
    }
}
