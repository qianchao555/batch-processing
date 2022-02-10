package easypoiexceldemo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

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


    //学生出生日期
    @TableField("Student_Birth_Date")
    private Date studentBirthDate;

    //A:语文
    @TableField("Ayuwen")
    private Integer Ayuwen;

    //A:数学
    @TableField("Ashuxue")
    private Integer Ashuxue;


    //B:语文
    @TableField("Bwuwen")
    private Integer Bwuwen;

    //B:数学
    @TableField("Bshuxue")
    private Integer Bshuxue;

}
