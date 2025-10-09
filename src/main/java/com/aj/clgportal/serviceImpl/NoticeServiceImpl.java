package com.aj.clgportal.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.NoticeDto;
import com.aj.clgportal.entity.Department;
import com.aj.clgportal.entity.Notice;
import com.aj.clgportal.exception.DuplicateResourceException;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.DeptRespository;
import com.aj.clgportal.repository.NoticeRepository;
import com.aj.clgportal.service.NoticeService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private NoticeRepository noticeRepo;
	
	@Autowired
	private DeptRespository deptRepo;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public NoticeDto newNotice(NoticeDto dto) {
		
		
		if (noticeRepo.existsByNoticeTitle(dto.getNoticeTitle())) {
			throw new DuplicateResourceException(dto.getNoticeTitle() + " notice already exists.");
		} else {
			List<Department> departments = deptRepo.findAllById(dto.getDeptId());
			
			Date currentDate = new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        String formattedDate = formatter.format(currentDate);
	        
	        Date postedDate=null;
			try {
				postedDate = formatter.parse(formattedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			Notice notice = new Notice();
			notice.setNoticeTitle(dto.getNoticeTitle());
			notice.setNoticeDesc(dto.getNoticeDesc());
			notice.setStatus(dto.getStatus());
			notice.setDepts(departments);
			notice.setPostedOn(postedDate);
			notice.setUpdatedOn(null);
			dto.setDeptId(dto.getDeptId());
			Notice newNotice = noticeRepo.save(notice);
			NoticeDto noticeDto = noticeToDto(newNotice);
			return noticeDto;
		}
	}

	@Override
	public NoticeDto updateNotice(NoticeDto dto, Long id) {
		
			List<Department> departments = deptRepo.findAllById(dto.getDeptId());
			
			Date currentDate = new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        String formattedDate = formatter.format(currentDate);
	        
	        Date updatedDate=null;
			try {
				updatedDate = formatter.parse(formattedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Notice notice = noticeRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("notice", "notice id", id));
			notice.setNoticeTitle(dto.getNoticeTitle());
			notice.setNoticeDesc(dto.getNoticeDesc());
			notice.setStatus(dto.getStatus());
			notice.setDepts(departments);
			notice.setPostedOn(dto.getPostedOn());
			notice.setUpdatedOn(updatedDate);
			dto.setDeptId(dto.getDeptId());
			Notice updatedNotice = noticeRepo.save(notice);
			NoticeDto noticeDto = noticeToDto(updatedNotice);
			return noticeDto;
	}

	@Override
	public void removeNotice(Long id) {
		Notice notice = noticeRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("notice", "notice id", id));
		noticeRepo.delete(notice);

	}

	@Override
	public NoticeDto getNoticeById(Long id) {
	    Notice notice = noticeRepo.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Notice", "notice id", id));
	    
	    return noticeToDto(notice); // All deptIds already handled in this method
	}


	@Override
	public List<NoticeDto> getAllNotices() {
	    List<Notice> notices = noticeRepo.findAll();
	    return notices.stream()
	                  .map(this::noticeToDto)
	                  .collect(Collectors.toList());
	}
	
	@Override
	public Long getMaxNoticeId() {
		Long maxNoticeId = noticeRepo.findMaxNoticeId();
		return maxNoticeId;
	}

	@Transactional
	@Override
	public void resetNoticeSequence(Long id) {
		String sql = "ALTER SEQUENCE tbl_notice_seq RESTART WITH " + id;
		entityManager.createNativeQuery(sql).executeUpdate();
	}
	
	@Transactional
	@Override
	public void removeDepartmentNotice(Long id) {
		String sql = "DELETE FROM tbl_notice_department WHERE notice_id = :noticeId";
	    entityManager.createNativeQuery(sql)
	        .setParameter("noticeId", id)
	        .executeUpdate();
	}
	
	@Override
	public List<NoticeDto> getNoticeByStatus(Character status) {
		 List<Notice> list = noticeRepo.findByStatus(status);
		    return list.stream()
		               .map(this::noticeToDto)
		               .collect(Collectors.toList());
	}

	public NoticeDto noticeToDto(Notice notice) {
	    NoticeDto dto = new NoticeDto();
	    dto.setId(notice.getId());
	    dto.setNoticeTitle(notice.getNoticeTitle());
	    dto.setNoticeDesc(notice.getNoticeDesc());
	    dto.setStatus(notice.getStatus());
	    dto.setPostedOn(notice.getPostedOn());
	    dto.setUpdatedOn(notice.getUpdatedOn());
	    if (notice.getDepts() != null) {
	        dto.setDeptId(
	            notice.getDepts()
	                  .stream()
	                  .map(Department::getId) // Assuming Department has getId()
	                  .collect(Collectors.toList())
	        );
	    }
	    return dto;
	}

	public Notice dtoToNotice(NoticeDto dto) {
	    Notice notice = new Notice();
	    notice.setId(dto.getId());
	    notice.setNoticeTitle(dto.getNoticeTitle());
	    notice.setNoticeDesc(dto.getNoticeDesc());
	    notice.setStatus(dto.getStatus());
	    notice.setPostedOn(dto.getPostedOn());
	    notice.setUpdatedOn(notice.getUpdatedOn());
	    if (dto.getDeptId() != null) {
	        List<Department> departments = dto.getDeptId().stream()
	            .map(id -> {
	                Department d = new Department();
	                d.setId(id);
	                return d;
	            })
	            .collect(Collectors.toList());
	        notice.setDepts(departments);
	    }
	    return notice;
	}

	@Override
	public Long getCountOfNotice(Character status) {
		long validNotices = noticeRepo.countByStatus(status);
		
		return validNotices;
		
	}

}
