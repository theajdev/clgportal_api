package com.aj.clgportal.dto;

import lombok.Data;

@Data
public class PermissionDto {
	private Long id;

    private String apiUrl;

    private String httpMethod;

    private boolean selected;
}
