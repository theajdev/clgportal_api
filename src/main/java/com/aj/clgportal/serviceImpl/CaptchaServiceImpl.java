package com.aj.clgportal.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.aj.clgportal.service.CaptchaService;


@Service
public class CaptchaServiceImpl implements CaptchaService {
	
	@Value("${recaptcha.secret.key}")
    private String secretKey;

    @Value("${recaptcha.verify.url}")
    private String verifyUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    
	@Override
	public boolean verifyCaptcha(String token) {
		 if (token == null || token.isEmpty()) {
	            return false;
	        }

	        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	        params.add("secret", secretKey);
	        params.add("response", token);

	        ResponseEntity<Map> response = restTemplate.postForEntity(verifyUrl, params, Map.class);

	        Map<String, Object> body = response.getBody();

	        if (body == null) {
	            return false;
	        }

	        Boolean success = (Boolean) body.get("success");

	        // Optional: log error codes if any
	        if (!success && body.containsKey("error-codes")) {
	            System.out.println("reCAPTCHA error: " + body.get("error-codes"));
	        }

	        return success != null && success;
	}

}
