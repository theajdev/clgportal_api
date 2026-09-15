package com.aj.clgportal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
		info = @Info(
				title = "College Portal",
				description = "This application contains APIs for college portal app.",
				version = "1.0",
				contact = @Contact(
						name = "AjTechSoft",
						email = "asksoftwares1@gmail.com",
						url = "https://ajtechsoft.qzz.io"
					),
				termsOfService = "ajtechsoft@2025"
				),
				security = @SecurityRequirement(name = "bearerAuth"), // Enable globally
		servers= {
				@Server(
						url = "http://localhost:2025",
						description = "Local Server"
						),
				@Server(
						
						url = "https://theajdev.github.io",
						description = "Production Server"
						)
		},
		tags = {
				@Tag(name = "Authentication API", description = "Login"),
				@Tag(name = "Notice APIs",description = "Read, write, add & remove notices"),
				@Tag(name = "Academic Years APIs",description = "Add new academic year, also update and remove existing academic year.")
		}
		)
@SecurityScheme(
	    name = "bearerAuth",
	    type = SecuritySchemeType.HTTP,
	    scheme = "bearer",
	    bearerFormat = "JWT"
	)
public class OpenApiConfig {
	
}
