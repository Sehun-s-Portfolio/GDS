package com.project.gds.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    public static String totalWord;


    // http://116.125.141.139:8080/upload/about.png
    // 리소스 접근 시 addResourceHandler를 통해 요청 경로 값을 설정하고
    // add ResourceLocations를 통해 접근할 파일 이들어가져있는 실질적인 경로를 입력받아
    // 요청 url + addResourceHandlers에서 설정한 경로 값을 요청하면 locations 에 설정한 파일들에 실질적으로 접근할 수 있또록 하는 Config 함수
    // 예) http://116.125.141.139:8080/upload/about.png 을 요청하면 locations에 설정한 접근 경로 중 하나에 일치한 파일에 접근한다.

    /** 회사 자체 서버 config **/
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/project/**", "/contact/**", "/product/**", "/product/img/**", "/product/file/**")
                .addResourceLocations("file:///home/gds_korea/gds_korea/webapp/project/",
                        "file:///home/gds_korea/gds_korea/webapp/contact/",
                        "file:///home/gds_korea/gds_korea/webapp/product/img/",
                        "file:///home/gds_korea/gds_korea/webapp/product/file/");
    }*/

    /** GDS 측 가비아 서버 config **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/project/**", "/contact/**", "/product/**", "/product/img/**", "/product/file/**")
                .addResourceLocations("file:///web/home/gds_korea/gds_korea/webapp/project/",
                        "file:///web/home/gds_korea/gds_korea/webapp/contact/",
                        "file:///web/home/gds_korea/gds_korea/webapp/product/img/",
                        "file:///web/home/gds_korea/gds_korea/webapp/product/file/");
    }
}
