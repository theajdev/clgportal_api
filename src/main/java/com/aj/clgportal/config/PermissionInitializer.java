package com.aj.clgportal.config;

import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.aj.clgportal.entity.ApiPermission;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.repository.ApiPermissionRepository;
import com.aj.clgportal.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionInitializer {
	
	private final RequestMappingHandlerMapping mapping;
    private final ApiPermissionRepository permissionRepo;
    private final RoleRepository roleRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void initPermissions() {

        Map<RequestMappingInfo, HandlerMethod> handlers =
                mapping.getHandlerMethods();

        for (RequestMappingInfo info : handlers.keySet()) {

            Set<String> urls = info.getPatternValues();
            Set<RequestMethod> methods =
                    info.getMethodsCondition().getMethods();

            for (String url : urls) {

                if (url.startsWith("/swagger")
                        || url.startsWith("/v3/api-docs")
                        || url.equals("/error")) {
                    continue;
                }

                // Convert path variables to /**
                if (url.contains("{")) {
                    int index = url.indexOf("/{");

                    if (index > 0) {
                        url = url.substring(0, index) + "/**";
                    } else {
                        url = url.replaceAll("\\{[^/]+\\}", "**");
                    }
                }

                
                for (RequestMethod method : methods) {

                    boolean exists =
                            permissionRepo.existsByApiUrlAndHttpMethod(
                                    url,
                                    method.name());

                    if (!exists) {

                        ApiPermission permission =
                                new ApiPermission();

                        permission.setApiUrl(url);
                        permission.setHttpMethod(method.name());

                        permissionRepo.save(permission);

						/*
						 * System.out.println( "Permission Added : " + method.name() + " " + url);
						 */
                    }
                }
            }
        }

        // Assign ALL permissions to admin
        Role adminRole =
                roleRepo.findByRoleName("ROLE_ADMIN")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "ROLE_ADMIN not found"));

        adminRole.setPermissions(
                Set.copyOf(permissionRepo.findAll()));

        roleRepo.save(adminRole);

        System.out.println(
                "All permissions assigned to ROLE_ADMIN");
    }

}
