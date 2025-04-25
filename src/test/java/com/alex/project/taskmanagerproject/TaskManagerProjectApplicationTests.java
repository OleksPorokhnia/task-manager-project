package com.alex.project.taskmanagerproject;

import com.alex.project.taskmanagerproject.config.MainConfig;
import com.alex.project.taskmanagerproject.config.WebSocketConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@SpringBootTest
@ImportAutoConfiguration(exclude = WebSocketConfiguration.class)
@ContextConfiguration(classes = {MainConfig.class})
class TaskManagerProjectApplicationTests {

    @Test
    void contextLoads() {
    }

}
