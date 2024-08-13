package com.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/11/13 17:29
 * @version:1.0
 */
public class MySelectorClass implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                MySelectorService.class.getName()
        };
    }
}
