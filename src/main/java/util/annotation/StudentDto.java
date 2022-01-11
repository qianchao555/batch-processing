package util.annotation;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName StudentDto
 * @Author qianchao
 * @Date 2022/1/11
 * @Version V1.0
 **/
@Data
@Builder
public class StudentDto {
    @QueryAlias("stu")
    private String studentName;

    @QueryAlias("stu")
    private String studentAge;
}
