package com.aj.clgportal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aj.clgportal.dto.ApiResponse;
import com.aj.clgportal.dto.NoticeDto;
import com.aj.clgportal.dto.NoticeReadDto;
import com.aj.clgportal.service.NoticeService;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeServ;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@PostMapping("/")
	public ResponseEntity<NoticeDto> createNotice(@RequestPart("notice") NoticeDto dto,
			@RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

		NoticeDto saved = noticeServ.newNotice(dto, files);

		return ResponseEntity.ok(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<NoticeDto> updateNotice(@PathVariable Long id, @RequestPart("notice") NoticeDto dto,
			@RequestPart(value = "files", required = false) List<MultipartFile> files) {
		System.out.println("Profile Pic: " + dto.getSendersProfilePic());
		NoticeDto updatedNotice = noticeServ.updateNotice(dto, id, files);

		return ResponseEntity.ok(updatedNotice);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteNotice(@PathVariable Long id) {
		noticeServ.removeDepartmentNotice(id);
		noticeServ.deleteNoticeReadDetails(id);
		noticeServ.removeNotice(id);
		Long maxNoticeId = noticeServ.getMaxNoticeId();
		noticeServ.resetNoticeSequence(maxNoticeId + 1);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Notice deleted.", true), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NoticeDto> getNoticeById(@PathVariable Long id) {
		NoticeDto notice = noticeServ.getNoticeById(id);
		return new ResponseEntity<>(notice, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<NoticeDto>> getAllNotices() {
		List<NoticeDto> notices = noticeServ.getAllNotices();
		return ResponseEntity.ok(notices);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<NoticeDto>> getAllNotices(@PathVariable Character status) {
		List<NoticeDto> notices = noticeServ.getNoticeByStatus(status);
		return ResponseEntity.ok(notices);
	}

	@GetMapping("/count")
	public ResponseEntity<Long> getNoticeCount() {
		Long countOfNotice = noticeServ.getCountOfNotice('V');
		return ResponseEntity.ok(countOfNotice);
	}

	@GetMapping("/depts/{id}")
	public ResponseEntity<List<NoticeDto>> getAllNoticesByDepts(@PathVariable Long id) {
		List<NoticeDto> notices = noticeServ.getNoticesByDepts(id);
		return ResponseEntity.ok(notices);
	}

	@PostMapping("/{id}/read/{userId}")
	public ResponseEntity<String> markAsRead(@PathVariable Long id, @PathVariable Long userId,
			@RequestParam String userType) {
		noticeServ.markNoticeAsRead(id, userId, userType);
		return ResponseEntity.ok("Notice marked as read");
	}

	@GetMapping("/read/{noticeId}")
	public ResponseEntity<List<NoticeReadDto>> getNoticeReadDetails(@PathVariable Long noticeId) {
		List<NoticeReadDto> noticeReadDetails = noticeServ.getNoticeReadDetails(noticeId);
		return ResponseEntity.ok(noticeReadDetails);
	}

	@GetMapping("/attachment/{noticeId}/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long noticeId, @PathVariable String fileName) {

		// 🔐 1. Basic validation
		if (fileName.contains("..")) {
			throw new RuntimeException("Invalid file name");
		}

		try {
			// 📁 Build path safely
			Path basePath = Paths.get(uploadDir).resolve("notices").resolve(String.valueOf(noticeId)).normalize();

			Path filePath = basePath.resolve(fileName).normalize();

			// 🔐 2. Prevent path traversal attack
			if (!filePath.startsWith(basePath)) {
				throw new RuntimeException("Invalid file path");
			}

			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists() || !resource.isReadable()) {
				throw new RuntimeException("File not found");
			}

			// 📄 3. Detect content type
			String contentType = Files.probeContentType(filePath);
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			// 🔥 4. INLINE preview (IMPORTANT)
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
					.body(resource);

		} catch (Exception e) {
			throw new RuntimeException("Error while downloading file", e);
		}
	}
}
