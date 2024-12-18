package com.project.gds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableJpaAuditing // 생성 일자, 수정 일자 엔티티에 자동으로 반영해주기 위한 어노테이션 추가
@SpringBootApplication
public class GdsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GdsApplication.class, args);
        System.out.println("어플리케이션 실행 ~~~~");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(GdsApplication.class);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
    }

}
