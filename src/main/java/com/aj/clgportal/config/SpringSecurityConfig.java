package com.aj.clgportal.config;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.aj.clgportal.security.JwtAuthenticationEntryPoint;
import com.aj.clgportal.security.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;

@Configuration
@Getter
@Setter
@AllArgsConstructor
public class SpringSecurityConfig {

	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // You can use other implementations if needed
	}

	public static final String[] PUBLIC_URLS = { "/","/api/auth/**", "/api/auth/register", "/v3/api-docs",
			"/api-docs", "/v3/api-docs/**", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui/**",
			"/swagger-ui.html", "/webjars/**"

	};
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((authorize) -> {
			authorize.requestMatchers(PUBLIC_URLS).permitAll();
			authorize.requestMatchers("/api/admin/**").hasRole("ADMIN");
			authorize.requestMatchers("/api/role/**").hasRole("ADMIN");
			authorize.requestMatchers("/api/department/**").hasRole("ADMIN");
			authorize.requestMatchers("/api/notice/**").hasRole("ADMIN");
			authorize.requestMatchers(HttpMethod.POST, "/api/teacher/**").hasRole("ADMIN");
			authorize.requestMatchers(HttpMethod.PUT, "/api/teacher/**").hasRole("ADMIN");
			authorize.requestMatchers(HttpMethod.GET, "/api/teacher/**").hasAnyRole("ADMIN", "TEACHER");
			authorize.requestMatchers("/api/student/**").hasRole("TEACHER");
			authorize.requestMatchers(HttpMethod.POST, "/api/student/**").hasRole("TEACHER");
			authorize.requestMatchers(HttpMethod.PUT, "/api/student/**").hasRole("TEACHER");
			authorize.requestMatchers(HttpMethod.GET, "/api/student/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT");
			authorize.anyRequest().authenticated();
		}).httpBasic(Customizer.withDefaults());
		http.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint));
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfigure) throws Exception {
		return authenticationConfigure.getAuthenticationManager();
	}
	
	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOriginPattern("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", corsConfiguration);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(-110);
		return bean;
	}

}
