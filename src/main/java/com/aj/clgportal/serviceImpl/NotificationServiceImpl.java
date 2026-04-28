package com.aj.clgportal.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.entity.Notification;
import com.aj.clgportal.repository.NotificationRepository;
import com.aj.clgportal.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
    private NotificationRepository repository;

	@Override
	public List<Notification> getNotificationDetails(Long notificationId) {
		return repository.findNotificationById(notificationId);
	}

	@Override
	public List<Notification> getAllNotificationsByDept(Long deptId, int pageSize, int pageNumber, String sortBy, String sortDir, String search) {

	    Sort sort = sortDir.equalsIgnoreCase("asc") 
	            ? Sort.by(sortBy).ascending() 
	            : Sort.by(sortBy).descending();

	    PageRequest p = PageRequest.of(pageNumber, pageSize, sort);

	    Page<Notification> notices = repository.findByDepartmentIdAndTitleContainingIgnoreCase(deptId, search,p);

	    return notices.getContent();
	}
}
