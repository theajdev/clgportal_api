package com.aj.clgportal.serviceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aj.clgportal.dto.AttachmentDto;
import com.aj.clgportal.dto.AttachmentEventDto;
import com.aj.clgportal.dto.NoticeDto;
import com.aj.clgportal.dto.NoticeEvent;
import com.aj.clgportal.dto.NoticeReadDto;
import com.aj.clgportal.entity.Department;
import com.aj.clgportal.entity.Notice;
import com.aj.clgportal.entity.NoticeAttachment;
import com.aj.clgportal.entity.NoticeRead;
import com.aj.clgportal.entity.Student;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.exception.DuplicateResourceException;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.DeptRespository;
import com.aj.clgportal.repository.NoticeAttachmentRepository;
import com.aj.clgportal.repository.NoticeReadRepository;
import com.aj.clgportal.repository.NoticeRepository;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.service.NoticeService;
import com.aj.clgportal.util.NoticeType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
public class NoticeServiceImpl implements NoticeService {

	private final NoticeRepository noticeRepo;
	private final DeptRespository deptRepo;
	private final EntityManager entityManager;
	private final NoticeReadRepository noticeReadRepo;
	private final StudentRepository studRepo;
	private final TeacherRepository teacherRepo;
	private final NoticeProducer producer;
	private final NoticeAttachmentRepository attachmentRepository;
	private final String uploadDir;

	public NoticeServiceImpl(NoticeRepository noticeRepo, DeptRespository deptRepo, EntityManager entityManager,
			NoticeReadRepository noticeReadRepo, StudentRepository studRepo, TeacherRepository teacherRepo,
			NoticeProducer producer, NoticeAttachmentRepository attachmentRepository,
			@Value("${file.upload-dir}") String uploadDir // 🔥 FIX HERE
	) {
		this.noticeRepo = noticeRepo;
		this.deptRepo = deptRepo;
		this.entityManager = entityManager;
		this.noticeReadRepo = noticeReadRepo;
		this.studRepo = studRepo;
		this.teacherRepo = teacherRepo;
		this.producer = producer;
		this.attachmentRepository = attachmentRepository;
		this.uploadDir = uploadDir;
	}

	@Override
	public NoticeDto newNotice(NoticeDto dto, List<MultipartFile> files) {

		if (noticeRepo.existsByNoticeTitle(dto.getNoticeTitle())) {
			throw new DuplicateResourceException(dto.getNoticeTitle() + " notice already exists.");
		} else {
			List<Department> departments = deptRepo.findAllById(dto.getDeptId());

			Date currentDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = formatter.format(currentDate);

			Date postedDate = null;
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
			notice.setSentBy(dto.getSentBy());
			notice.setDepts(departments);
			notice.setPostedOn(postedDate);
			notice.setUpdatedOn(null);
			notice.setSendersUsername(dto.getSendersUsername());
			notice.setSendersProfilePic(dto.getSendersProfilePic());
			dto.setDeptId(dto.getDeptId());
			Notice newNotice = noticeRepo.save(notice);

			// 🔥 Send Kafka Event
			NoticeEvent event = new NoticeEvent();
			event.setNoticeId(newNotice.getId());
			event.setTitle(newNotice.getNoticeTitle());
			event.setDescription(newNotice.getNoticeDesc());
			event.setSentBy(newNotice.getSentBy());
			event.setDeptIds(dto.getDeptId());
			event.setSendersUsername(newNotice.getSendersUsername());
			event.setSendersProfilePic(newNotice.getSendersProfilePic());
			event.setType(NoticeType.CREATE);

			if (files != null && !files.isEmpty()) {
				
				
				List<NoticeAttachment> attachments = new ArrayList<>();

				String baseDir = uploadDir + File.separator + "notices" + File.separator + newNotice.getId();

				for (MultipartFile file : files) {
					try {
						String cleanFileName = StringUtils.cleanPath(file.getOriginalFilename());

						if (cleanFileName.contains("..")) {
							throw new RuntimeException("Invalid file name");
						}

						String fileName = System.currentTimeMillis() + "_" + cleanFileName;

						Path dirPath = Paths.get(baseDir);
						Files.createDirectories(dirPath); // 🔥 creates /noticeId folder

						Path filePath = dirPath.resolve(fileName);

						Files.write(filePath, file.getBytes());

						NoticeAttachment att = new NoticeAttachment();
						att.setFileName(cleanFileName);
						att.setContentType(file.getContentType());
						att.setSize(file.getSize());
						att.setFilePath(filePath.toString());

						// 🔥 IMPORTANT: include noticeId in URL
						att.setFileUrl("/attachment/" + newNotice.getId() + "/" + fileName);

						att.setNotice(newNotice);

						attachments.add(att);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				attachmentRepository.saveAll(attachments);
				newNotice.setAttachments(attachments);

				List<AttachmentEventDto> attachmentEvents = attachments.stream().map(att -> {
					AttachmentEventDto eventDto = new AttachmentEventDto();
					eventDto.setFileName(att.getFileName());
					eventDto.setContentType(att.getContentType());
					eventDto.setSize(att.getSize());
					eventDto.setFileUrl(att.getFileUrl());
					eventDto.setNoticeId(att.getNotice().getId());
					return eventDto;
				}).collect(Collectors.toList());

				event.setAttachments(attachmentEvents);
			}

			producer.sendNotice(event);
			return noticeToDto(newNotice);
		}
	}

	@Override
	public NoticeDto updateNotice(NoticeDto dto, Long id, List<MultipartFile> files) {

		List<Department> departments = deptRepo.findAllById(dto.getDeptId());

		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate = formatter.format(currentDate);

		Date updatedDate = null;
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
		notice.setSentBy(dto.getSentBy());
		notice.setDepts(departments);
		notice.setPostedOn(dto.getPostedOn());
		notice.setUpdatedOn(updatedDate);
		notice.setSendersUsername(dto.getSendersUsername());
		notice.setSendersProfilePic(dto.getSendersProfilePic());
		dto.setDeptId(dto.getDeptId());
		Notice updatedNotice = noticeRepo.save(notice);
		NoticeDto noticeDto = noticeToDto(updatedNotice);

		// 🔥 Send Kafka Event
		NoticeEvent event = new NoticeEvent();
		event.setNoticeId(updatedNotice.getId());
		event.setTitle(updatedNotice.getNoticeTitle());
		event.setDescription(updatedNotice.getNoticeDesc());
		event.setSentBy(updatedNotice.getSentBy());
		event.setDeptIds(dto.getDeptId());
		event.setSendersUsername(updatedNotice.getSendersUsername());
		event.setSendersProfilePic(updatedNotice.getSendersProfilePic());
		event.setType(NoticeType.UPDATE);

		if (files != null && !files.isEmpty()) {

			// 🔥 1. DELETE OLD FILES

			List<NoticeAttachment> oldAttachments = attachmentRepository.findByNoticeId(id);

			for (NoticeAttachment old : oldAttachments) {
				try {
					Files.deleteIfExists(Paths.get(old.getFilePath()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			attachmentRepository.deleteAllInBatch(oldAttachments);

			// 🔥 2. SAVE NEW FILES
			List<NoticeAttachment> attachments = new ArrayList<>();

			String baseDir = uploadDir + File.separator + "notices" + File.separator + updatedNotice.getId();

			for (MultipartFile file : files) {
				try {
					String cleanFileName = StringUtils.cleanPath(file.getOriginalFilename());

					if (cleanFileName.contains("..")) {
						throw new RuntimeException("Invalid file name");
					}

					String fileName = System.currentTimeMillis() + "_" + cleanFileName;

					Path dirPath = Paths.get(baseDir);
					Files.createDirectories(dirPath);

					Path filePath = dirPath.resolve(fileName);
					Files.write(filePath, file.getBytes());

					NoticeAttachment att = new NoticeAttachment();
					att.setFileName(cleanFileName);
					att.setContentType(file.getContentType());
					att.setSize(file.getSize());
					att.setFilePath(filePath.toString());
					att.setFileUrl("/attachment/" + updatedNotice.getId() + "/" + fileName);
					att.setNotice(updatedNotice);

					attachments.add(att);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			attachmentRepository.saveAll(attachments);
			updatedNotice.setAttachments(attachments);

			List<AttachmentEventDto> attachmentEvents = attachments.stream().map(atts -> {
				AttachmentEventDto eventDto = new AttachmentEventDto();
				eventDto.setFileName(atts.getFileName());
				eventDto.setContentType(atts.getContentType());
				eventDto.setSize(atts.getSize());
				eventDto.setFileUrl(atts.getFileUrl());
				eventDto.setNoticeId(atts.getNotice().getId());
				return eventDto;
			}).collect(Collectors.toList());

			event.setAttachments(attachmentEvents);
		}
		producer.sendNotice(event);
		return noticeDto;
	}

	@Override
	public void removeNotice(Long id) {
		Notice notice = noticeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("notice", "notice id", id));
		noticeRepo.delete(notice);

		String folderPath = uploadDir + File.separator + "notices" + File.separator + id;

		try {
			Path dirPath = Paths.get(folderPath);

			if (Files.exists(dirPath)) {
				Files.walk(dirPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Transactional
	@Override
	public NoticeDto getNoticeById(Long id) {
		Notice notice = noticeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notice", "notice id", id));

		return noticeToDto(notice); // All deptIds already handled in this method
	}

	@Override
	public List<NoticeDto> getAllNotices() {
		List<Notice> notices = noticeRepo.findAll();
		return notices.stream().map(this::noticeToDto).collect(Collectors.toList());
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
		entityManager.createNativeQuery(sql).setParameter("noticeId", id).executeUpdate();
	}

	@Override
	public List<NoticeDto> getNoticeByStatus(Character status) {
		List<Notice> list = noticeRepo.findByStatus(status);
		return list.stream().map(this::noticeToDto).collect(Collectors.toList());
	}

	@Transactional
	public NoticeDto noticeToDto(Notice notice) {
		NoticeDto dto = new NoticeDto();
		dto.setId(notice.getId());
		dto.setNoticeTitle(notice.getNoticeTitle());
		dto.setNoticeDesc(notice.getNoticeDesc());
		dto.setStatus(notice.getStatus());
		dto.setSendersUsername(notice.getSendersUsername());
		dto.setSendersProfilePic(notice.getSendersProfilePic());
		dto.setSentBy(notice.getSentBy());
		dto.setPostedOn(notice.getPostedOn());
		dto.setUpdatedOn(notice.getUpdatedOn());
		if (notice.getDepts() != null) {
			dto.setDeptId(notice.getDepts().stream().map(Department::getId) // Assuming Department has getId()
					.collect(Collectors.toList()));
		}

		if (notice.getAttachments() != null) {
			dto.setAttachments(notice.getAttachments().stream()
					.map(att -> new AttachmentDto(att.getId(), att.getFileName(), att.getFileUrl()))
					.collect(Collectors.toList()));
		}

		return dto;
	}

	public Notice dtoToNotice(NoticeDto dto) {
		Notice notice = new Notice();
		notice.setId(dto.getId());
		notice.setNoticeTitle(dto.getNoticeTitle());
		notice.setNoticeDesc(dto.getNoticeDesc());
		notice.setStatus(dto.getStatus());
		notice.setSendersUsername(dto.getSendersUsername());
		notice.setSendersProfilePic(dto.getSendersProfilePic());
		notice.setSentBy(dto.getSentBy());
		notice.setPostedOn(dto.getPostedOn());
		notice.setUpdatedOn(notice.getUpdatedOn());
		if (dto.getDeptId() != null) {
			List<Department> departments = dto.getDeptId().stream().map(id -> {
				Department d = new Department();
				d.setId(id);
				return d;
			}).collect(Collectors.toList());
			notice.setDepts(departments);
		}
		return notice;
	}

	@Override
	public Long getCountOfNotice(Character status) {
		long validNotices = noticeRepo.countByStatus(status);

		return validNotices;

	}

	@Override
	public List<NoticeDto> getNoticesByDepts(Long deptId) {
		List<Notice> notices = noticeRepo.findByDeptsNotices(deptId);
		return notices.stream().map(this::noticeToDto).collect(Collectors.toList());
	}

	@Override
	public void markNoticeAsRead(Long noticeId, Long userId, String userType) {
		// Notice exists?
		Notice notice = noticeRepo.findById(noticeId).orElseThrow(() -> new RuntimeException("Notice not found"));

		// ---------------------------------------------------------
		// FOR STUDENT
		// ---------------------------------------------------------
		if ("STUDENT".equalsIgnoreCase(userType)) {

			if (noticeReadRepo.existsByNoticeIdAndStudentId(noticeId, userId)) {
				return; // Already read
			}

			Student student = studRepo.findById(userId).orElseThrow(() -> new RuntimeException("Student not found"));

			NoticeRead read = new NoticeRead();
			read.setNotice(notice);
			read.setStudent(student);
			read.setReadAt(new Date());

			noticeReadRepo.save(read);
			return;
		}

		// ---------------------------------------------------------
		// FOR TEACHER
		// ---------------------------------------------------------
		if ("TEACHER".equalsIgnoreCase(userType)) {

			if (noticeReadRepo.existsByNoticeIdAndTeacherId(noticeId, userId)) {
				return; // Already read
			}

			Teacher teacher = teacherRepo.findById(userId).orElseThrow(() -> new RuntimeException("Teacher not found"));

			NoticeRead read = new NoticeRead();
			read.setNotice(notice);
			read.setTeacher(teacher);
			read.setReadAt(new Date());

			noticeReadRepo.save(read);
			return;
		}

		throw new RuntimeException("Invalid user type! Use STUDENT or TEACHER");
	}

	@Override
	public List<NoticeReadDto> getNoticeReadDetails(Long noticeId) {

		return noticeReadRepo.getNoticeReadDetails(noticeId);
	}

	@Transactional
	@Override
	public void deleteNoticeReadDetails(Long noticeId) {

		String sql = "DELETE FROM tbl_notice_read WHERE notice_id = :noticeId";
		entityManager.createNativeQuery(sql).setParameter("noticeId", noticeId).executeUpdate();
	}

}
