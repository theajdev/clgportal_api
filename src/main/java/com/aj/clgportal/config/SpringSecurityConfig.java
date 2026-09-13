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
import com.aj.clgportal.security.DynamicPermissionFilter;
import com.aj.clgportal.security.JwtAuthenticationEntryPoint;
import com.aj.clgportal.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {

	private final DynamicPermissionFilter dynamicPermissionFilter;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // You can use other implementations if needed
	}

	public static final String[] PUBLIC_URLS = { "/", "/api/auth/**", "/api/auth/register", "/v3/api-docs", "/api-docs",
			"/v3/api-docs/**", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/swagger-ui.html",
			"/webjars/**", "/api/user/**", "/ws/**", "/ws/info/**", "/api/notifications/**", "/api/mark-one/**",
			"/api/mark-read/**", "/api/chat/**" };

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())

				.authorizeHttpRequests(auth -> auth

						.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())

				.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))

				.httpBasic(Customizer.withDefaults());

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.addFilterAfter(dynamicPermissionFilter, JwtAuthenticationFilter.class);

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
		corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://10.255.11.225:3000","http://localhost:8085","https://theajdev.github.io/clgportal_api"));
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", corsConfiguration);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(-110);
		return bean;
	}

}
