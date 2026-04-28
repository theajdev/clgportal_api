package com.aj.clgportal.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.NoticeReplyDto;
import com.aj.clgportal.dto.NoticeReplyEvent;
import com.aj.clgportal.entity.Notice;
import com.aj.clgportal.entity.NoticeReply;
import com.aj.clgportal.entity.Notification;
import com.aj.clgportal.entity.Student;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.repository.NoticeReplyRepository;
import com.aj.clgportal.repository.NoticeRepository;
import com.aj.clgportal.repository.NotificationRepository;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.service.NoticeReplyService;
import com.aj.clgportal.util.AppConstants;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoticeReplyServiceImpl implements NoticeReplyService {

	@Autowired
	private NoticeReplyRepository noticeReplyRepository;

	@Autowired
	private NoticeRepository noticeRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private NoticeReplyProducer producer;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private TeacherRepository teacherRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public void addReply(Long noticeId, NoticeReply reply, Long userDeptId, Authentication authentication) {

		Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new RuntimeException("Notice not found"));

		boolean allowed = notice.getDepts().stream().anyMatch(dept -> dept.getId().equals(userDeptId));

		if (!allowed) {
			throw new RuntimeException("You are not allowed to reply");
		}

		setUserDetails(reply, authentication);

		reply.setNotice(notice);
		reply.setRepliedOn(new Date());

		NoticeReply noticeReply = noticeReplyRepository.save(reply);

		NoticeReplyEvent event = new NoticeReplyEvent();
		event.setNoticeId(notice.getId());
		event.setMessage(noticeReply.getMessage());
		event.setProfilePic(noticeReply.getProfilePic());
		event.setRepliedOn(noticeReply.getRepliedOn());
		event.setSenderName(noticeReply.getRepliedBy());

		producer.sendNoticeReply(event);
	}

	@Override
	public Long getUserDeptId(Authentication authentication) {

		UserDetails user = (UserDetails) authentication.getPrincipal();
		System.out.println(user.getAuthorities());
		String username = authentication.getName();

		String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst()
				.orElse("");

		if ("ROLE_TEACHER".equals(role)) {
			Teacher teacher = teacherRepo.findByUsernameOrEmail(username, username);

			if (teacher == null) {
				throw new RuntimeException("Teacher not found");
			}

			return teacher.getDepts().getId();
		}

		if ("ROLE_STUDENT".equals(role)) {
			Student student = studentRepo.findByUsernameOrEmail(username, username);

			if (student == null) {
				throw new RuntimeException("Student not found");
			}

			return student.getDepts().getId();
		}

		throw new RuntimeException("Invalid role");

	}

	private void setUserDetails(NoticeReply reply, Authentication authentication) {

		String username = authentication.getName();

		String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst()
				.orElse("");

		if ("ROLE_STUDENT".equals(role)) {
			Optional<Student> student = Optional.ofNullable(studentRepo.findByUsernameOrEmail(username, username));

			if (student.isPresent()) {
				Student s = student.get();

				reply.setUsername(s.getUsername());
				reply.setRepliedBy(s.getFirstName() + " " + s.getLastName());
				reply.setProfilePic(s.getProfilePic());
				reply.setDesignation(AppConstants.STUD);
				return;
			}
		}
		if ("ROLE_TEACHER".equals(role)) {
			Optional<Teacher> teacher = Optional.ofNullable(teacherRepo.findByUsernameOrEmail(username, username));
			if (teacher.isPresent()) {
				Teacher t = teacher.get();

				reply.setUsername(t.getUsername());
				reply.setRepliedBy(t.getFirstName() + " " + t.getLastName());
				reply.setProfilePic(t.getProfilePic());
				reply.setDesignation(t.getDesignation());
				return;
			}

			throw new RuntimeException("User not found");
		}
	}

	@Override
	public Long getNoticeId(Long notificationId) {
		Notification n = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new RuntimeException("Notification not found"));
		List<Notice> searchNotices = noticeRepository.searchNotices(n.getTitle());
		return searchNotices.stream().findFirst().map(Notice::getId).orElse(null);
	}

	@Override
	public List<NoticeReplyDto> getRepliesByNotice(Long noticeId) {
		return noticeReplyRepository.findByNoticeIdOrderByRepliedOnAsc(noticeId).stream()
				.map((dto) -> noticeReplyToDto(dto)).collect(Collectors.toList());
	}

	public NoticeReplyDto noticeReplyToDto(NoticeReply noticeReply) {
		NoticeReplyDto noticeReplyDto = mapper.map(noticeReply, NoticeReplyDto.class);
		return noticeReplyDto;
	}

	public NoticeReply dtoToNoticeReply(NoticeReplyDto noticeReplyDto) {
		NoticeReply noticeReply = mapper.map(noticeReplyDto, NoticeReply.class);
		return noticeReply;
	}
}
