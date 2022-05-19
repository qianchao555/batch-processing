package com.easypoiexceldemo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName Student
 * @Author qianchao
 * @Date 2022/2/9
 **/
@TableName("Student")
@Data
public class Student {

    //学生
    @TableField("CREATE_BY_Id")
    private String createdById;



    //A:语文
    @TableField("Ayuwen")
    private Integer Ayuwen;

    //A:数学
    @TableField("Ashuxue")
    private Integer Ashuxue;


    //B:语文
    @TableField("Byuwen")
    private Integer Byuwen;

    //B:数学
    @TableField("Bshuxue")
    private Integer Bshuxue;

}
