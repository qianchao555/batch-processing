package com.util.enumutil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ArgsTypeEnum
 * @Author qianchao
 * @Date 2022/6/8
 * @Version   V1.0
 **/
@Getter
@AllArgsConstructor
public enum  ArgsTypeEnum implements BaseEnum<String>{

    A("A","这是A"),B("B","这是B");

    private String code;
    private String msg;


}
