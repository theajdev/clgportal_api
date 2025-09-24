package com.aj.clgportal.serviceImpl;

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
			Department department = deptRepo.findById(dto.getDeptId()).orElseThrow(
					() -> new ResourceNotFoundException("Department", "department id", dto.getDeptId()));

			Notice notice = new Notice();
			notice.setNoticeTitle(dto.getNoticeTitle());
			notice.setNoticeDesc(dto.getNoticeDesc());
			notice.setStatus(dto.getStatus());
			notice.setDepts(department);
			dto.setDeptId(department.getId());
			Notice newNotice = noticeRepo.save(notice);
			NoticeDto noticeDto = noticeToDto(newNotice);
			return noticeDto;
		}
	}

	@Override
	public NoticeDto updateNotice(NoticeDto dto, Long id) {
		if (noticeRepo.existsByNoticeTitle(dto.getNoticeTitle())) {
			throw new DuplicateResourceException(dto.getNoticeTitle() + " notice already exists.");
		} else {
			Department department = deptRepo.findById(dto.getDeptId()).orElseThrow(
					() -> new ResourceNotFoundException("Department", "department id", dto.getDeptId()));
			
			Notice notice = noticeRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("notice", "notice id", id));
			notice.setNoticeTitle(dto.getNoticeTitle());
			notice.setNoticeDesc(dto.getNoticeDesc());
			notice.setStatus(dto.getStatus());
			notice.setDepts(department);
			dto.setDeptId(notice.getDepts().getId());
			Notice updatedNotice = noticeRepo.save(notice);
			NoticeDto noticeDto = noticeToDto(updatedNotice);
			return noticeDto;
		}
	}

	@Override
	public void removeNotice(Long id) {
		Notice notice = noticeRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("notice", "notice id", id));
		noticeRepo.delete(notice);

	}

	@Override
	public NoticeDto getNoticeById(Long id) {
		Notice notice = noticeRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("notice", "notice id", id));
		NoticeDto noticeDto = noticeToDto(notice);
		noticeDto.setDeptId(notice.getDepts().getId());
		return noticeDto;
	}

	@Override
	public List<NoticeDto> getAllNotices() {
		List<Notice> notices = noticeRepo.findAll();
		List<NoticeDto> allNotices = notices.stream().map((list)->noticeToDto(list)).collect(Collectors.toList());
		notices.forEach(notice->{
			allNotices.forEach(noticeDto->{
				noticeDto.setDeptId(notice.getDepts().getId());
			});
		});
		return allNotices;
	}
	
	@Override
	public Long getMaxNoticeId() {
		Long maxNoticeId = noticeRepo.findMaxNoticeId();
		System.out.println("maxNoticeId: "+maxNoticeId);
		return maxNoticeId;
	}

	@Override
	public void resetNoticeSequence(Long id) {
		String sql = "ALTER SEQUENCE tbl_notice_seq RESTART WITH " + id;
		entityManager.createNativeQuery(sql).executeUpdate();
	}
	
	@Override
	public List<NoticeDto> getNoticeByStatus(Character status) {
		List<Notice> list = noticeRepo.findByStatus(status);
		List<NoticeDto> notices = list.stream().map((lst)->noticeToDto(lst)).collect(Collectors.toList());
		list.forEach(notice->{
			notices.forEach(noticeDto->{
				noticeDto.setDeptId(notice.getDepts().getId());
			});
		});
		return notices;
	}

	public NoticeDto noticeToDto(Notice notice) {
		NoticeDto noticeDto = modelMapper.map(notice, NoticeDto.class);
		return noticeDto;
	}

	public Notice dtoToNotice(NoticeDto dto) {
		Notice notice = modelMapper.map(dto, Notice.class);
		return notice;
	}
}
