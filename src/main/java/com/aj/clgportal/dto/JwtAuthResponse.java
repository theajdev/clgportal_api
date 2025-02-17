package com.aj.clgportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
	private String accessToken;
    private String tokenType="Bearer";
    private String loginId;
    private String authority;
}
