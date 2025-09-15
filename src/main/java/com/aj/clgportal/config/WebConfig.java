package com.aj.clgportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	public void addViewControllers(ViewControllerRegistry reg) {
		reg.addRedirectViewController("/", "/swagger-ui.html");
	}
}
