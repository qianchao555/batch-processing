package springaop;

import com.example.batchprocessing.BatchProcessingApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName DemoTest
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
@SpringBootApplication
public class DemoTest {

    public static void main(String[] args) {

            SpringApplication.run(DemoTest.class, args);

    }

}
