package com.boot;

import com.SpringBootRun;
import com.boot.annotation.TestDataSet;
import com.boot.listener.DockerContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SpringBootRun.class)
class BatchProcessingApplicationTests extends DockerContainerTest {

	@Test
	void contextLoads() {
	}

	@Test
	@TestDataSet(locals = "/student.xls")
	void myTest(){
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}

}
