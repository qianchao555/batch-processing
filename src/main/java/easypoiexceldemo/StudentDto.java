package easypoiexceldemo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName StudentDto   模板上对应的字段
 * @Author qianchao
 * @Date 2022/2/9
 **/
@Data
public class StudentDto implements Serializable {
    /**
     * 序号
     */
    private String oderNum;

    /**
     * 日期
     */
    private Date studentBirthDate;

    /**
     * A语文
     */
    private int Ayuwen;
    /**
     * A数学
     */
    private int Ashuxue;

    /**
     * B语文
     */
    private int Byuwen;
    /**
     * B数学
     */
    private int  Bshuxue;


}
