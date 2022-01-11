package util.annotation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import util.QueryWrapperUtil;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2022/1/11
 * @Version V1.0
 **/
public class Test {
    public static void main(String[] args) {
        QueryWrapper queryWrapper = QueryWrapperUtil.pareseWarper(StudentDto.builder()
                .studentAge("12")
                .studentName("qcYhc")
                .build());
        System.out.println(queryWrapper);
    }

}
