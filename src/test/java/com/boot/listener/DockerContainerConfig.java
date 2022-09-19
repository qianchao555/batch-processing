package com.boot.listener;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.TestExecutionListener;
import org.testcontainers.dockerclient.DockerClientProviderStrategy;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @ClassName DockerContainerConfig
 * @Author qianchao
 * @Date 2022/9/6
 * @Version V1.0
 **/
@Slf4j
public class DockerContainerConfig extends DockerClientProviderStrategy implements TestExecutionListener {
    //可配置远程docker机器 2375端口，提供外部访问docker
//    private static String DOCKER_HOST = "tcp://远程机器ip:2375";
    private static String DOCKER_HOST = "tcp://101.35.51.33:2375";
    //本地测试就用localhost

    public DockerContainerConfig() {
        //这一步可以封装到一个Utils里面
        InetAddress localIp4 = null;
        try {
            localIp4 = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        log.info("本机ip为：{}", localIp4);
        //判断ip环境，根据公司ip判断
        DOCKER_HOST = "tcp://101.35.51.33:2375";

        String dockerHost = System.getProperty("docker.host", DOCKER_HOST);
        log.info("docker host:{}", dockerHost);

        //本机测试
        if (dockerHost.contains("localhost")) {
            config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
            return;
        }
        System.setProperty("DOCKER_HOST", dockerHost);
        TestcontainersConfiguration.getInstance()
                .updateGlobalConfig("docker.client.strategy", DockerContainerConfig.class.getName());
        config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
    }

    @Override
    public void test() {
        try {
            client = getClientForConfig(config);
            int timout = Integer.parseInt(System.getProperty("testcontainers.environmentprovider.timeout", "30"));
            ping(client, timout);
        } catch (Exception e) {
            log.error("ping失败,docker配置为：{},由于：{}", getDescription(), e.toString());
        }
        log.info("从environment中,查询到docker client 配置!");
    }

    @Override
    public String getDescription() {
        return "Resolved dockerHost" + (config != null ? config.getDockerHost() : "");
    }


}
