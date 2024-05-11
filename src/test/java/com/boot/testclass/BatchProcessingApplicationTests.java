package com.boot.testclass;

import com.SpringBootRun;
import com.boot.annotation.TestDataSet;
import com.boot.base.pgimpl.DockerPgContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SpringBootRun.class)
class BatchProcessingApplicationTests extends DockerPgContainerTest {

	@Test
	void contextLoads() {
	}

	@Test
	@TestDataSet(locals = "/dataset/student.xls")
	void myTest(){
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}

}
