package com.hyeji.petbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableElasticsearchRepositories(basePackages = "com.hyeji.petbook.repository")
public class PetbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetbookApplication.class, args);
    }

}
