package com.aj.clgportal.config;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aj.clgportal.security.JwtAuthenticationEntryPoint;
import com.aj.clgportal.security.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;


@Getter
@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {

	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // You can use other implementations if needed
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((authorize) -> {
			authorize.requestMatchers("/api/auth/**").permitAll();
			authorize.requestMatchers("/api/admin/**").hasRole("ADMIN");
			authorize.requestMatchers("/api/role/**").hasRole("ADMIN");
			authorize.requestMatchers("/api/teacher/**").hasRole("ADMIN");
			authorize.requestMatchers("/api/student/**").hasRole("TEACHER");
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

}
