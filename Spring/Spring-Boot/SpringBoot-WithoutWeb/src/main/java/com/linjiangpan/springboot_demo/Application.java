package com.linjiangpan.springboot_demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.linjiangpan.springboot_demo.service.DemoService;

@SpringBootApplication
@ImportResource(value="classpath:applicationContext.xml")
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.setWebEnvironment(false);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        DemoService demoService = (DemoService)configurableApplicationContext.getBean("demoService");
        demoService.printf();
    }
}