package com.company_request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Arrays;
import java.util.Collections;

@EnableWebMvc
@Configuration
@ComponentScan({"com.company_request"})
public class SpringWebConfig implements WebMvcConfigurer {
    // All web configuration will go here.

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setSuffix(".ftl");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates");
        return freeMarkerConfigurer;
    }

    @Bean
    public SimpleUrlHandlerMapping myFaviconHandlerMapping()
    {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap("/favicon.ico",
                myFaviconRequestHandler()));
        return mapping;
    }

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    protected ResourceHttpRequestHandler myFaviconRequestHandler()
    {
        ResourceHttpRequestHandler requestHandler =
                new ResourceHttpRequestHandler();
        requestHandler.setLocations(Arrays
                .<Resource> asList(applicationContext.getResource("/")));
        requestHandler.setCacheSeconds(0);
        return requestHandler;
    }
}