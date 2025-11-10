package com.aj.clgportal.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePicService {
	String uploadProfilePic(String path, MultipartFile file) throws IOException;

	InputStream getResource(String path, String fileName) throws IOException;
}
