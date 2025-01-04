package com.aj.clgportal.service;

import java.util.List;

import com.aj.clgportal.dto.AdminDto;

public interface AdminService {
	public AdminDto newAdmin(AdminDto adminDto);
	public AdminDto updateAdmin(AdminDto adminDto,long id);
	public void deleteAdmin(long id);
	public AdminDto getAdminDetailsById(long id);
	public List<AdminDto> getAllAdminsList();
}
