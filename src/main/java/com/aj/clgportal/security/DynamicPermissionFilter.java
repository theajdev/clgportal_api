package com.aj.clgportal.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DynamicPermissionFilter extends OncePerRequestFilter {

	@SuppressWarnings("unchecked")
	private final PathPatternParser pathPatternParser = new PathPatternParser();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			filterChain.doFilter(request, response);
			return;
		}

		// Skip public APIs
		String requestPath = request.getRequestURI();

		if (requestPath.startsWith("/api/auth") || requestPath.startsWith("/swagger-ui")
				|| requestPath.startsWith("/v3/api-docs") || requestPath.startsWith("/ws")) {

			filterChain.doFilter(request, response);
			return;
		}

		@SuppressWarnings("unchecked")
		List<String> permissions = (List<String>) authentication.getDetails();

		if (permissions == null || permissions.isEmpty()) {

			response.sendError(HttpServletResponse.SC_FORBIDDEN, "No permissions assigned");

			return;
		}

		String requestMethod = request.getMethod();

		boolean allowed = permissions.stream()
				.anyMatch(permission -> matchesPermission(permission, requestMethod, requestPath));

		if (!allowed) {

			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");

			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean matchesPermission(String permission, String requestMethod, String requestPath) {

		try {

			String[] parts = permission.split(":", 2);

			if (parts.length != 2) {
				return false;
			}

			String permissionMethod = parts[0];

			String permissionPath = parts[1];

			if (!permissionMethod.equalsIgnoreCase(requestMethod)) {
				return false;
			}

			PathPattern pattern =
			        pathPatternParser.parse(
			                permissionPath);
			

			return pattern.matches(org.springframework.http.server.PathContainer.parsePath(requestPath));

		} catch (Exception ex) {

			return false;
		}
	}
}
