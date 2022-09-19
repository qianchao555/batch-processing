package com.boot.utils;

import org.springframework.util.StringUtils;

/**
 * @ClassName ResourceLocalProcessUtil
 * @Author qianchao
 * @Date 2022/9/6
 * @Version   V1.0
 **/
public class ResourceLocalProcessUtil {

    public static String[] modifyLocal(Class<?> clazz, String... locals) {
        String[] modifyLocal = new String[locals.length];
        //修正每一个locals里面的路径
        for (int i = 0; i < locals.length; i++) {
            String path = locals[i];
            if (path.startsWith("/")) {
                modifyLocal[i] = "classpath:" + path;
            } else {
                //自动修正为：/xxx/xx/ 这种路径
                modifyLocal[i] = StringUtils.cleanPath(path);
            }
        }
        return modifyLocal;
    }

}
