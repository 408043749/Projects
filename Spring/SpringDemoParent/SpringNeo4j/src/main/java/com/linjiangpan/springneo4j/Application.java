package com.linjiangpan.springneo4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.linjiangpan.springneo4j.domain.Movie;
import com.linjiangpan.springneo4j.service.Neo4jService;

@SpringBootApplication
@ImportResource(value="classpath:applicationContext.xml")
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.setWebEnvironment(false);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        Neo4jService neo4jService = (Neo4jService)configurableApplicationContext.getBean("neo4jService");
        neo4jService.save(new Movie());
    }
}