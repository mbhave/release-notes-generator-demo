package com.example.releasenotes.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	private final LoggerHandlerInterceptor interceptor;

	public WebConfiguration(LoggerHandlerInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.interceptor);
	}
}
