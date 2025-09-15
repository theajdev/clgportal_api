package com.aj.clgportal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

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
				security = @SecurityRequirement(name = "bearerAuth") // Enable globally
		)
@SecurityScheme(
	    name = "bearerAuth",
	    type = SecuritySchemeType.HTTP,
	    scheme = "bearer",
	    bearerFormat = "JWT"
	)
public class OpenApiConfig {
	
}
